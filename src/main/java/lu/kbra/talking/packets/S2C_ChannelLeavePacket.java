package lu.kbra.talking.packets;

import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.datastructure.triplet.Triplet;
import lu.pcy113.pclib.datastructure.triplet.Triplets;

import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.client.TalkingServerClient;

/**
 * username, left 1/switched 0, reason/channel
 */
public class S2C_ChannelLeavePacket implements S2C_Talking_Packet<Triplet<String, Boolean, Object>> {

	private Triplet<String, Boolean, Object> data;

	public S2C_ChannelLeavePacket() {
	}

	public S2C_ChannelLeavePacket(Triplet<String, Boolean, Object> data) {
		this.data = data;
	}

	@Override
	public void clientRead(P4JClient client, Triplet<String, Boolean, Object> obj) {
		if (obj.getSecond()) {
			System.out.println("User: " + obj.getFirst() + " left: " + obj.getThird());
		} else {
			System.out.println("User: " + obj.getFirst() + " left channel (target): " + obj.getThird());
		}
	}

	@Override
	public Triplet<String, Boolean, Object> serverWrite(TalkingServerClient client) {
		return data;
	}

	public static S2C_ChannelLeavePacket disconnected(String username) {
		return new S2C_ChannelLeavePacket(Triplets.readOnly(username, true, null));
	}

	public static S2C_ChannelLeavePacket switch_(String username, String targetChannel) {
		return new S2C_ChannelLeavePacket(Triplets.readOnly(username, false, targetChannel));
	}

}
