package lu.kbra.talking;

import java.io.File;
import java.io.IOException;

import lu.pcy113.pclib.builder.ThreadBuilder;
import lu.pcy113.pclib.logger.GlobalLogger;
import lu.pcy113.pclib.logger.PCLogger;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.consts.Consts;
import lu.kbra.talking.server.TalkingServer;

public class TalkingMain {

	private static TalkingInstance instance;

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			GlobalLogger.info("Usage: java -jar TalkingMain.jar <type [server|client]> (<local port>)");
			return;
		}

		TalkingInstanceType type = TalkingInstanceType.valueOf(args[0].toUpperCase());
		if (type == null) {
			System.err.print("Unknown type: " + args[0] + ", valid types are: " + TalkingInstanceType.values());
			System.exit(1);
		}

		int port = Consts.DEFAULT_PORT;
		if (type.equals(TalkingInstanceType.CLIENT)) {
			port = 0;
		}
		if (args.length > 1) {
			port = Integer.parseInt(args[1]);
		}

		final int finalPort = port;

		switch (type) {
		case SERVER:
			try {
				PCLogger.exportDefaultConfig("./config/logs.properties");
				GlobalLogger.init(new File("./config/logs.properties"));
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			GlobalLogger.info("Starting server on: 0.0.0.0:" + finalPort);
			instance = new TalkingServer("0.0.0.0", finalPort);

//			new java.util.Timer().schedule(new java.util.TimerTask() {
//				@Override
//				public void run() {
//					TalkingServer.INSTANCE.stop();
//				}
//			}, 5000);

			// starting server watchdog thread
			ThreadBuilder.create(() -> {
				while (TalkingServer.INSTANCE.shouldRun()) {
					if (!TalkingServer.INSTANCE.getServer().isAlive()) {
						GlobalLogger.info("Restarting server on: 0.0.0.0:" + finalPort);
						try {
							instance = new TalkingServer("0.0.0.0", finalPort);
						} catch (IOException e) {
							GlobalLogger.log(e);
						}
					}
					try {
						Thread.sleep(30_000);
					} catch (Exception e) {
						GlobalLogger.log(e);
					}
				}
			}).daemon(true).name("TalkingServer - Watchdog").start();
			break;
		case CLIENT:
			instance = new TalkingClient(port);
			break;
		}
	}

	public static TalkingInstance getInstance() {
		return instance;
	}

}
