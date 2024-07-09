package lu.kbra.talking.consts;

import lu.pcy113.p4j.packets.PacketManager;

import lu.kbra.talking.packets.C2S_HandshakePacket;
import lu.kbra.talking.packets.C2S_S2C_ChangeChannelPacket;
import lu.kbra.talking.packets.C2S_S2C_MessagePacket;
import lu.kbra.talking.packets.S2C_ChannelJoinPacket;
import lu.kbra.talking.packets.S2C_ChannelLeavePacket;
import lu.kbra.talking.packets.S2C_LoginPacket;
import lu.kbra.talking.packets.S2C_LoginRefusedPacket;

public final class Packets {

	public static void registerPackets(PacketManager packets) {
		packets.register(C2S_HandshakePacket.class, 0x01);
		packets.register(S2C_LoginPacket.class, 0x02);
		packets.register(S2C_LoginRefusedPacket.class, 0x03);
		packets.register(C2S_S2C_MessagePacket.class, 0x04);
		packets.register(C2S_S2C_ChangeChannelPacket.class, 0x05);
		packets.register(S2C_ChannelLeavePacket.class, 0x06);
		packets.register(S2C_ChannelJoinPacket.class, 0x07);
	}

}
