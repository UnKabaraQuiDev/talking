package lu.pcy113.talking.packets;

import lu.pcy113.p4j.packets.c2s.C2SPacket;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.p4j.socket.server.ServerClient;
import lu.pcy113.talking.TalkingMain;
import lu.pcy113.talking.consts.Consts;
import lu.pcy113.talking.data.UserData;
import lu.pcy113.talking.packets.C2S_HandshakePacket.HandShakeData;
import lu.pcy113.talking.server.TalkingServer;
import lu.pcy113.talking.server.client.TalkingServerClient;

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
	
	public C2S_HandshakePacket(UserData userData) {
		this.handShakeData = new HandShakeData(userData);
	}

	@Override
	public HandShakeData clientWrite(P4JClient client) {
		return handShakeData;
	}

	@Override
	public void serverRead(ServerClient sclient, HandShakeData obj) {
		((TalkingServer) TalkingMain.getInstance()).incomingHandshake((TalkingServerClient) sclient, obj);
	}

}
