package lu.pcy113.talking.packets;

import lu.pcy113.p4j.packets.s2c.S2CPacket;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.p4j.socket.server.ServerClient;

public class S2C_LoginRefusedPacket implements S2CPacket {

	@Override
	public Object serverWrite(ServerClient client) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clientRead(P4JClient client, Object obj) {
		// TODO Auto-generated method stub

	}

}
