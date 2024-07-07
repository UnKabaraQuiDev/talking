package lu.kbra.talking.client;

import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Collectors;

import lu.kbra.talking.consts.Consts;
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
			} catch (Exception e) {
				e.printStackTrace();
			}
			print();
		}
	}

	protected void computeCommand(String line) throws Exception {
		String[] tokens = line.split(" ");
		if (tokens[0].equalsIgnoreCase("send")) {
			sendCommand(line.replaceFirst("send ", ""));
		} else if (tokens[0].equalsIgnoreCase("cd")) {
			cdCommand(line.replaceFirst("cd ", ""));
		} else if (tokens[0].equalsIgnoreCase("ls")) {
			lsCommand();
		} else if (tokens[0].equalsIgnoreCase("ll")) {
			llCommand();
		} else if (tokens[0].equalsIgnoreCase("exit")) {
			disconnectCommand();
			running = false;
		} else if (tokens[0].equalsIgnoreCase("connect")) {
			connectCommand(tokens[1]);
		} else if (tokens[0].equalsIgnoreCase("disconnect")) {
			disconnectCommand();
		} else if (tokens[0].equalsIgnoreCase("bind")) {
			bindCommand(Integer.parseInt(tokens[1]));
		}
	}

	private void bindCommand(int localPort) {
		TalkingClient.INSTANCE.setLocalPort(localPort);
		System.out.println("Local port: " + localPort + " will be used for the next connection");
	}

	private void disconnectCommand() {
		if (!TalkingClient.INSTANCE.isConnected()) {
			System.out.println("You are not connected to a server, connect first");
			return;
		}
		TalkingClient.INSTANCE.disconnect();
	}

	private void connectCommand(String addr) throws IOException {
		if (TalkingClient.INSTANCE.isConnected()) {
			System.out.println("You are already connected to a server, disconnect first");
			return;
		}
		if (!addr.contains(":")) {
			addr += ":" + Consts.DEFAULT_PORT;
		}
		String tokens[] = addr.split(":");
		String host = tokens[0];
		int port = Integer.parseInt(tokens[1]);
		TalkingClient.INSTANCE.connect(host, port);
	}

	private void llCommand() {
		if (!TalkingClient.INSTANCE.isConnected()) {
			System.out.println("Connect to a server first: connect <host>(:<port>)");
			return;
		}
		System.out.println(TalkingClient.INSTANCE.getServerData().getChannels().keySet().stream().collect(Collectors.joining("\n")));
	}

	private void lsCommand() {
		if (!TalkingClient.INSTANCE.isConnected()) {
			System.out.println("Connect to a server first: connect <host>(:<port>)");
			return;
		}
		System.out.println(TalkingClient.INSTANCE.getServerData().getChannels().keySet().stream().collect(Collectors.joining(" ")));
	}

	private void cdCommand(String replaceFirst) {
		if (!TalkingClient.INSTANCE.isConnected()) {
			System.out.println("Connect to a server first: connect <host>(:<port>)");
			return;
		}
		Channel channel = TalkingClient.INSTANCE.getServerData().getChannels().values().stream().filter(c -> c.getName().equalsIgnoreCase(replaceFirst)).findFirst().get();
		// TalkingClient.INSTANCE.getServerData().setCurrentChannelUuid(channel.getUuid());
		TalkingClient.INSTANCE.getClient().write(C2S_S2C_ChangeChannel.to(channel.getUuid()));
	}

	private void sendCommand(String txt) {
		if (!TalkingClient.INSTANCE.isConnected()) {
			System.out.println("Connect to a server first: connect <host>(:<port>)");
			return;
		}
		TalkingClient.INSTANCE.getClient().write(new C2S_S2C_MessagePacket(txt));
	}

	public void print() {
		if (TalkingClient.INSTANCE.getServerData() != null && TalkingClient.INSTANCE.getClient() != null && TalkingClient.INSTANCE.getClient().getClientServer() != null) {
			System.out.print(TalkingClient.INSTANCE.getClient().getClientServer().getRemoteInetSocketAddress().toString() + "/" + TalkingClient.INSTANCE.getServerData().getCurrentChannel().getName() + ">");
		} else {
			System.out.print("/>");
		}
	}

	public void update() {
		System.out.println();
		print();
	}

}
