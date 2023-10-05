package lu.pcy113.talking.server.client;

import java.nio.channels.SocketChannel;

import lu.pcy113.p4j.socket.server.P4JServer;
import lu.pcy113.p4j.socket.server.ServerClient;
import lu.pcy113.talking.data.UserData;
import lu.pcy113.talking.server.TalkingServer;

public class TalkingServerClient extends ServerClient {
	
	private UserData userData;
	private TalkingServer server;
	
	public TalkingServerClient(SocketChannel sc, P4JServer server, TalkingServer ts) {
		super(sc, server);
		this.server = ts;
	}

}
