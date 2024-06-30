package lu.pcy113.talking;

import lu.pcy113.talking.client.TalkingClient;
import lu.pcy113.talking.consts.Consts;
import lu.pcy113.talking.server.TalkingServer;

public class TalkingMain {
	
	private static TalkingInstance instance;
	
	public static void main(String[] args) {
		if(args.length < 2) {
			System.out.println("Usage: java -jar TalkingMain.jar <type [server|client]> <host>:<port>");
			return;
		}
		
		TalkingInstanceType type = TalkingInstanceType.valueOf(args[0].toLowerCase());
		if(type == null) {
			System.err.print("Unknown type: "+args[0]+", valid types are: "+TalkingInstanceType.values());
			System.exit(1);
		}
		
		String host = args[1];
		int port = Consts.DEFAULT_PORT;
		if(host.contains(":")) {
			String[] p = host.split(":");
			host = p[0];
			port = Integer.parseInt(p[1]);
		}
		
		switch(type) {
		case SERVER:
			instance = new TalkingServer(host, port);
			break;
		case CLIENT:
			
			instance = new TalkingClient(host, port);
			break;
		}
	}
	
	public static TalkingInstance getInstance() {
		return instance;
	}
	
}
