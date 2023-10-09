package lu.pcy113.talking.consts;

import lu.pcy113.p4j.packets.PacketManager;
import lu.pcy113.talking.packets.C2S_HandshakePacket;
import lu.pcy113.talking.packets.S2C_LoginPacket;
import lu.pcy113.talking.packets.S2C_LoginRefusedPacket;

public final class Packets {

	public static void registerPackets(PacketManager packet) {
		packet.register(C2S_HandshakePacket.class, 0x01);
		packet.register(S2C_LoginPacket.class, 0x02);
		packet.register(S2C_LoginRefusedPacket.class, 0x03);
	}

}
