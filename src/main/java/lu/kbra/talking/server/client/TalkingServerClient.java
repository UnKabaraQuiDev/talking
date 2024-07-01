package lu.kbra.talking.server.client;

import java.nio.channels.SocketChannel;

import lu.kbra.talking.data.UserData;
import lu.kbra.talking.server.TalkingServer;
import lu.pcy113.p4j.socket.server.P4JServer;
import lu.pcy113.p4j.socket.server.ServerClient;

public class TalkingServerClient extends ServerClient {

	private UserData userData;
	private TalkingServer server;

	public TalkingServerClient(SocketChannel sc, P4JServer server, TalkingServer ts) {
		super(sc, server);
		this.server = ts;
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public TalkingServer getTalkingServer() {
		return server;
	}
	
}