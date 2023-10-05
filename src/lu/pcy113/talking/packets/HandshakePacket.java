package lu.pcy113.talking.packets;

import lu.pcy113.p4j.packets.c2s.C2SPacket;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.p4j.socket.server.ServerClient;
import lu.pcy113.talking.TalkingMain;
import lu.pcy113.talking.data.UserData;
import lu.pcy113.talking.server.TalkingServer;
import lu.pcy113.talking.server.client.TalkingServerClient;

public class HandshakePacket implements C2SPacket<UserData> {
	
	private UserData userData;
	
	public HandshakePacket(UserData userData) {
		this.userData = userData;
	}

	@Override
	public UserData clientWrite(P4JClient client) {
		return userData;
	}

	@Override
	public void serverRead(ServerClient sclient, UserData obj) {
		((TalkingServer) TalkingMain.getInstance()).incomingHandshake((TalkingServerClient) sclient, obj);
	}

}
