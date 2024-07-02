package lu.kbra.talking.packets.impl;

import lu.pcy113.p4j.packets.s2c.S2CPacket;
import lu.pcy113.p4j.socket.server.ServerClient;

import lu.kbra.talking.server.client.TalkingServerClient;

public interface S2C_Talking_Packet<T> extends S2CPacket<T> {

	@Override
	default T serverWrite(ServerClient client) {
		return serverWrite((TalkingServerClient) client);
	}

	T serverWrite(TalkingServerClient client);

}
