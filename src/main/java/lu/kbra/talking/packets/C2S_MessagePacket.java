package lu.kbra.talking.packets;

import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.packets.impl.C2S_Talking_Packet;
import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.client.TalkingServerClient;

public class C2S_MessagePacket implements C2S_Talking_Packet<String>, S2C_Talking_Packet<String> {

	private String text;

	public C2S_MessagePacket() {
	}

	public C2S_MessagePacket(String text) {
		this.text = text;
	}

	@Override
	public String clientWrite(P4JClient client) {
		GlobalLogger.log("Sending: " + text);
		return text;
	}

	@Override
	public void serverRead(TalkingServerClient sclient, String obj) {
		GlobalLogger.log("Received: " + obj);
		sclient.getServer().broadcastIf(new C2S_MessagePacket(obj), (s) -> !s.equals(sclient));
	}

	@Override
	public void clientRead(P4JClient client, String obj) {
		GlobalLogger.log("Received: " + obj);
	}

	@Override
	public String serverWrite(TalkingServerClient client) {
		GlobalLogger.log("Sending: " + text);
		return text;
	}

}
