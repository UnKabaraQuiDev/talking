package lu.kbra.talking.client;

import java.util.Scanner;

import lu.kbra.talking.packets.C2S_SendPacket;

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
			String line = scanner.nextLine();
			computeCommand(line);
		}
	}

	protected void computeCommand(String line) {
		String[] tokens = line.split(" ");
		if(tokens[0].equalsIgnoreCase("send")) {
			sendCommand(line.replaceFirst("send ", ""));
		}
	}

	private void sendCommand(String txt) {
		TalkingClient.INSTANCE.getClient().write(new C2S_SendPacket(txt));
	}

}
