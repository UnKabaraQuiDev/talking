package lu.kbra.talking.packets.impl;

import lu.pcy113.p4j.packets.c2s.C2SPacket;
import lu.pcy113.p4j.socket.server.ServerClient;

import lu.kbra.talking.server.client.TalkingServerClient;

public interface C2S_Talking_Packet<T> extends C2SPacket<T> {

	@Override
	default void serverRead(ServerClient sclient, T obj) {
		serverRead((TalkingServerClient) sclient, obj);
	}

	void serverRead(TalkingServerClient sclient, T obj);

}
