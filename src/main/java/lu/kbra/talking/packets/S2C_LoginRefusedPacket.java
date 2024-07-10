package lu.kbra.talking.packets;

import lu.pcy113.p4j.socket.client.P4JClient;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.client.TalkingServerClient;

public class S2C_LoginRefusedPacket implements S2C_Talking_Packet<String> {

	private String reason;

	public S2C_LoginRefusedPacket() {
	}

	public S2C_LoginRefusedPacket(String reason) {
		this.reason = reason;
	}

	@Override
	public String serverWrite(TalkingServerClient client) {
		return reason;
	}

	@Override
	public void clientRead(P4JClient client, String obj) {
		System.out.println("Login refused: " + obj);
		TalkingClient.INSTANCE.getClient().disconnect();
	}

}
