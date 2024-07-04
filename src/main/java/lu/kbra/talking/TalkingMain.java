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
		System.setProperty("java.net.preferIPv4Stack" , "true");
		
		try {
			PCLogger.exportDefaultConfig("./config/logs.properties");
			GlobalLogger.init(new File("./config/logs.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		if (args.length < 1) {
			GlobalLogger.info("Usage: java -jar TalkingMain.jar <type [server|client]> (<port>)");
			return;
		}

		TalkingInstanceType type = TalkingInstanceType.valueOf(args[0].toUpperCase());
		if (type == null) {
			System.err.print("Unknown type: " + args[0] + ", valid types are: " + TalkingInstanceType.values());
			System.exit(1);
		}

		int port = Consts.DEFAULT_PORT;
		if (args.length > 1) {
			port = Integer.parseInt(args[1]);
		}

		switch (type) {
		case SERVER:
			GlobalLogger.info("Starting server on: 0.0.0.0:" + port);
			instance = new TalkingServer("0.0.0.0", port);
			break;
		case CLIENT:
			instance = new TalkingClient();
			break;
		}
	}

	public static TalkingInstance getInstance() {
		return instance;
	}

}
