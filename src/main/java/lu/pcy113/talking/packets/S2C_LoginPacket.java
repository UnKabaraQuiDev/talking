package lu.pcy113.talking.packets;

import lu.pcy113.p4j.packets.s2c.S2CPacket;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.p4j.socket.server.ServerClient;
import lu.pcy113.talking.TalkingMain;
import lu.pcy113.talking.client.TalkingClient;
import lu.pcy113.talking.server.data.ServerDataView;

public class S2C_LoginPacket implements S2CPacket<ServerDataView> {
	
	private ServerDataView view;
	
	public S2C_LoginPacket(ServerDataView view) {
		this.view = view;
	}

	@Override
	public ServerDataView serverWrite(ServerClient client) {
		return view;
	}

	@Override
	public void clientRead(P4JClient client, ServerDataView obj) {
		((TalkingClient) TalkingMain.getInstance()).setServerDataView(obj);
	}

}
