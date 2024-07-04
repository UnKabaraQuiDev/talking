package lu.kbra.talking.client;

import java.util.Scanner;
import java.util.stream.Collectors;

import lu.kbra.talking.data.Channel;
import lu.kbra.talking.packets.C2S_S2C_ChangeChannel;
import lu.kbra.talking.packets.C2S_S2C_MessagePacket;

public class ConsoleClient extends Thread implements Runnable {

	private volatile boolean running = true;

	private Scanner scanner;

	public ConsoleClient() {
		this.scanner = new Scanner(System.in);
		start();
	}

	@Override
	public void run() {
		while (running && this.scanner.hasNextLine()) {
			try {
				String line = scanner.nextLine();
				computeCommand(line);
				print();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void computeCommand(String line) {
		String[] tokens = line.split(" ");
		if (tokens[0].equalsIgnoreCase("send")) {
			sendCommand(line.replaceFirst("send ", ""));
		} else if (tokens[0].equalsIgnoreCase("cd")) {
			cdCommand(line.replaceFirst("cd ", ""));
		} else if (tokens[0].equalsIgnoreCase("ls")) {
			lsCommand();
		} else if (tokens[0].equalsIgnoreCase("ll")) {
			llCommand();
		}
	}

	private void llCommand() {
		System.out.println(TalkingClient.INSTANCE.getServerData().getChannels().keySet().stream().collect(Collectors.joining("\n")));
	}

	private void lsCommand() {
		System.out.println(TalkingClient.INSTANCE.getServerData().getChannels().keySet().stream().collect(Collectors.joining(" ")));
	}

	private void cdCommand(String replaceFirst) {
		Channel channel = TalkingClient.INSTANCE.getServerData().getChannels().values().stream().filter(c -> c.getName().equalsIgnoreCase(replaceFirst)).findFirst().get();
		// TalkingClient.INSTANCE.getServerData().setCurrentChannelUuid(channel.getUuid());
		TalkingClient.INSTANCE.getClient().write(C2S_S2C_ChangeChannel.to(channel.getUuid()));
	}

	private void sendCommand(String txt) {
		TalkingClient.INSTANCE.getClient().write(new C2S_S2C_MessagePacket(txt));
	}

	public void print() {
		System.out.print("/" + TalkingClient.INSTANCE.getServerData().getCurrentChannel().getName() + ">");
	}

	public void update() {
		System.out.println();
		print();
	}

}
