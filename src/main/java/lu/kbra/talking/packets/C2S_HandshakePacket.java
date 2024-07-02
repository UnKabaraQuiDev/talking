package lu.kbra.talking.packets;

import lu.pcy113.p4j.socket.client.P4JClient;

import lu.kbra.talking.consts.Consts;
import lu.kbra.talking.data.UserData;
import lu.kbra.talking.packets.C2S_HandshakePacket.HandShakeData;
import lu.kbra.talking.packets.impl.C2S_Talking_Packet;
import lu.kbra.talking.server.TalkingServer;
import lu.kbra.talking.server.client.TalkingServerClient;

public class C2S_HandshakePacket implements C2S_Talking_Packet<HandShakeData> {

	public static class HandShakeData {
		public String version = Consts.VERSION;
		public UserData userData;

		public HandShakeData(UserData userData) {
			this.userData = userData;
		}

		public HandShakeData(String version, UserData userData) {
			this.version = version;
			this.userData = userData;
		}
	}

	private HandShakeData handShakeData;

	public C2S_HandshakePacket() {
	}

	public C2S_HandshakePacket(UserData userData) {
		this.handShakeData = new HandShakeData("1.000", userData);
	}

	@Override
	public HandShakeData clientWrite(P4JClient client) {
		return handShakeData;
	}

	@Override
	public void serverRead(TalkingServerClient sclient, HandShakeData obj) {
		TalkingServer.INSTANCE.incomingHandshake(sclient, obj);
	}

}
