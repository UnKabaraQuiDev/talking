package lu.kbra.talking.packets;

import java.util.UUID;

import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.packets.impl.C2S_Talking_Packet;
import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.client.TalkingServerClient;

public class C2S_S2C_MessagePacket implements C2S_Talking_Packet<Pair<UUID, String>>, S2C_Talking_Packet<String> {

	private String text;

	public C2S_S2C_MessagePacket() {
	}

	public C2S_S2C_MessagePacket(String text) {
		this.text = text;
	}

	// C2S - - - - - -

	@Override
	public Pair<UUID, String> clientWrite(P4JClient client) {
		return Pairs.readOnly(TalkingClient.INSTANCE.getServerData().getCurrentChannelUuid(), text);
	}

	@Override
	public void serverRead(TalkingServerClient sclient, Pair<UUID, String> obj) {
		GlobalLogger.log("Received: " + obj);
		final UUID channel = obj.getKey();
		sclient.getServer().broadcastIf(new C2S_S2C_MessagePacket(obj.getValue()), (s) -> !s.equals(sclient) && ((TalkingServerClient) s).getUserData().getCurrentChannelUuid().equals(channel));
	}

	// S2C - - - - - -

	@Override
	public void clientRead(P4JClient client, String obj) {
		System.out.println("[userName] << " + obj);
	}

	@Override
	public String serverWrite(TalkingServerClient client) {
		GlobalLogger.log("Sending: " + text + " to: " + client.getUUID() + "(" + client.getUserData().getUserName() + ")");
		return text;
	}

}
