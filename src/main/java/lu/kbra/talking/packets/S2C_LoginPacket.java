package lu.kbra.talking.packets;

import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.TalkingMain;
import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.client.TalkingServerClient;
import lu.kbra.talking.server.data.ServerDataView;

public class S2C_LoginPacket implements S2C_Talking_Packet<ServerDataView> {

	private ServerDataView view;

	public S2C_LoginPacket() {
	}

	public S2C_LoginPacket(ServerDataView view) {
		this.view = view;
	}

	@Override
	public ServerDataView serverWrite(TalkingServerClient client) {
		return view;
	}

	@Override
	public void clientRead(P4JClient client, ServerDataView obj) {
		((TalkingClient) TalkingMain.getInstance()).setServerDataView(obj);
		GlobalLogger.info("Successfull login: " + obj.getChannels());
		TalkingClient.INSTANCE.getConsoleClient().update();
	}

}
