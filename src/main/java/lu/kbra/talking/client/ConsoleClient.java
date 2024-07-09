package lu.kbra.talking.client;

import java.io.IOException;
import java.util.stream.Collectors;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.history.DefaultHistory;

import lu.kbra.talking.client.data.C_RemoteUserData;
import lu.kbra.talking.consts.Consts;
import lu.kbra.talking.data.Channel;
import lu.kbra.talking.packets.C2S_S2C_ChangeChannelPacket;
import lu.kbra.talking.packets.C2S_S2C_MessagePacket;

public class ConsoleClient extends Thread implements Runnable {

	private volatile boolean running = true;

	public ConsoleClient() {
		start();
	}

	private String prompt;

	@Override
	public void run() {
		LineReader reader = LineReaderBuilder.builder().history(new DefaultHistory()).build();

		while (running) {
			print();
			
			try {
				String line = reader.readLine(prompt);
				computeCommand(line);
			} catch (UserInterruptException e) {
				try {
					computeCommand("exit");
				} catch (Exception e1) {
					e1.printStackTrace();
					System.exit(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			if (TalkingClient.INSTANCE.isConnected()) {
				disconnectCommand();
			}
			running = false;
		} else if (tokens[0].equalsIgnoreCase("connect")) {
			connectCommand(tokens[1]);
		} else if (tokens[0].equalsIgnoreCase("disconnect")) {
			disconnectCommand();
		} else if (tokens[0].equalsIgnoreCase("bind")) {
			bindCommand(Integer.parseInt(tokens[1]));
		} else if (tokens[0].equalsIgnoreCase("clear")) {
			clear();
		}
	}

	public void clear() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
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
		TalkingClient.INSTANCE.getClient().write(C2S_S2C_ChangeChannelPacket.to(channel.getUuid()));
	}

	private void sendCommand(String txt) {
		if (!TalkingClient.INSTANCE.isConnected()) {
			System.out.println("Connect to a server first: connect <host>(:<port>)");
			return;
		}

		for (C_RemoteUserData u : TalkingClient.INSTANCE.getServerData().getRemoteUsers()) {
			TalkingClient.INSTANCE.getClient().write(C2S_S2C_MessagePacket.c2s(u, txt));
		}
	}

	public void print() {
		if (TalkingClient.INSTANCE.getServerData() != null && TalkingClient.INSTANCE.getClient() != null && TalkingClient.INSTANCE.getClient().getClientServer() != null) {
			prompt = TalkingClient.INSTANCE.getClient().getClientServer().getRemoteInetSocketAddress().toString() + "/" + TalkingClient.INSTANCE.getServerData().getCurrentChannel().getName() + ">";
		} else {
			prompt = "/>";
		}
	}

	public void update() {
		System.out.println();
		print();
	}

}
