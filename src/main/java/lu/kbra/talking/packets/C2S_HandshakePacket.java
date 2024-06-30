package lu.kbra.talking.packets;

import lu.pcy113.p4j.packets.c2s.C2SPacket;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.p4j.socket.server.ServerClient;

import lu.kbra.talking.TalkingMain;
import lu.kbra.talking.consts.Consts;
import lu.kbra.talking.data.UserData;
import lu.kbra.talking.packets.C2S_HandshakePacket.HandShakeData;
import lu.kbra.talking.server.TalkingServer;
import lu.kbra.talking.server.client.TalkingServerClient;

public class C2S_HandshakePacket implements C2SPacket<HandShakeData> {

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
		this.handShakeData = new HandShakeData(userData);
		System.err.println("init usr data: " + userData + " = " + handShakeData);
	}

	@Override
	public HandShakeData clientWrite(P4JClient client) {
		System.err.println("send: " + handShakeData);
		return handShakeData;
	}

	@Override
	public void serverRead(ServerClient sclient, HandShakeData obj) {
		System.err.println(obj);
		((TalkingServer) TalkingMain.getInstance()).incomingHandshake((TalkingServerClient) sclient, obj);
	}

}
