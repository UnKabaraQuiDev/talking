package lu.kbra.talking.packets;

import lu.pcy113.p4j.socket.client.P4JClient;

import lu.kbra.talking.packets.impl.C2S_Talking_Packet;
import lu.kbra.talking.server.TalkingServer;
import lu.kbra.talking.server.client.TalkingServerClient;
import lu.kbra.talking.server.data.S_UserData;

public class C2S_HandshakePacket implements C2S_Talking_Packet<S_UserData> {

	private S_UserData userData;

	public C2S_HandshakePacket() {
	}

	public C2S_HandshakePacket(S_UserData userData) {
		this.userData = userData;
	}

	@Override
	public S_UserData clientWrite(P4JClient client) {
		return userData;
	}

	@Override
	public void serverRead(TalkingServerClient sclient, S_UserData obj) {
		TalkingServer.INSTANCE.incomingHandshake(sclient, obj);
	}

}
