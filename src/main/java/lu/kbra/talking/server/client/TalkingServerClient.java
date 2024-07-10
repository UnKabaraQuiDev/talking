package lu.kbra.talking.server.client;

import java.nio.channels.SocketChannel;

import lu.pcy113.p4j.socket.server.P4JServer;
import lu.pcy113.p4j.socket.server.ServerClient;

import lu.kbra.talking.server.TalkingServer;
import lu.kbra.talking.server.data.S_UserData;

public class TalkingServerClient extends ServerClient {

	private S_UserData userData;
	private TalkingServer server;

	public TalkingServerClient(SocketChannel sc, P4JServer server, TalkingServer ts) {
		super(sc, server);
		this.server = ts;
	}

	public S_UserData getUserData() {
		return userData;
	}

	public void setUserData(S_UserData userData) {
		this.userData = userData;
	}

	public TalkingServer getTalkingServer() {
		return server;
	}

	public boolean hasUserData() {
		return userData != null;
	}

}
