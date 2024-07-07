package lu.kbra.talking.packets;

import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.datastructure.triplet.Triplet;
import lu.pcy113.pclib.datastructure.triplet.Triplets;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.client.TalkingServerClient;

/**
 * username, joined 1/switched 0, reason/channel
 */
public class S2C_ChannelJoinPacket implements S2C_Talking_Packet<Triplet<String, Boolean, Object>> {

	public S2C_ChannelJoinPacket() {
	}

	private Triplet<String, Boolean, Object> data;

	public S2C_ChannelJoinPacket(Triplet<String, Boolean, Object> data) {
		this.data = data;
	}

	@Override
	public void clientRead(P4JClient client, Triplet<String, Boolean, Object> obj) {
		if (obj.getSecond()) {
			System.out.println("User: " + obj.getFirst() + " joined: " + obj.getThird());
		} else {
			System.out.println("User: " + obj.getFirst() + " joined channel (source): " + obj.getThird());
		}
		TalkingClient.INSTANCE.getConsoleClient().print();
	}

	@Override
	public Triplet<String, Boolean, Object> serverWrite(TalkingServerClient client) {
		return data;
	}

	public static S2C_ChannelJoinPacket join(String username, String reason) {
		return new S2C_ChannelJoinPacket(Triplets.readOnly(username, true, reason));
	}
	
	public static S2C_ChannelJoinPacket switch_(String username, String previousChannel) {
		return new S2C_ChannelJoinPacket(Triplets.readOnly(username, false, previousChannel));
	}

}
