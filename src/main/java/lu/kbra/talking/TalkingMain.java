package lu.kbra.talking;

import java.io.File;
import java.io.IOException;

import lu.pcy113.pclib.logger.GlobalLogger;
import lu.pcy113.pclib.logger.PCLogger;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.consts.Consts;
import lu.kbra.talking.server.TalkingServer;

public class TalkingMain {

	private static TalkingInstance instance;

	public static void main(String[] args) throws IOException {
		try {
			PCLogger.exportDefaultConfig("./config/logs.properties");
			GlobalLogger.init(new File("./config/logs.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		if (args.length < 2) {
			GlobalLogger.info("Usage: java -jar TalkingMain.jar <type [server|client]> <host>:<port>");
			return;
		}

		TalkingInstanceType type = TalkingInstanceType.valueOf(args[0].toUpperCase());
		if (type == null) {
			System.err.print("Unknown type: " + args[0] + ", valid types are: " + TalkingInstanceType.values());
			System.exit(1);
		}

		String host = args[1];
		int port = Consts.DEFAULT_PORT;
		if (host.contains(":")) {
			String[] p = host.split(":");
			host = p[0];
			port = Integer.parseInt(p[1]);
		}

		switch (type) {
		case SERVER:
			GlobalLogger.info("Starting server on: " + host + ":" + port);
			instance = new TalkingServer(host, port);
			break;
		case CLIENT:
			GlobalLogger.info("Starting client to: " + host + ":" + port);
			instance = new TalkingClient(host, port);
			break;
		}
	}

	public static TalkingInstance getInstance() {
		return instance;
	}

}
