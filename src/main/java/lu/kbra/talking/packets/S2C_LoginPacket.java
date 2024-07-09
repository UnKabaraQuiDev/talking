package lu.kbra.talking.packets;

import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.TalkingMain;
import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.client.data.C_ServerData;
import lu.kbra.talking.client.frame.AppFrame;
import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.client.TalkingServerClient;

public class S2C_LoginPacket implements S2C_Talking_Packet<C_ServerData> {

	private C_ServerData view;

	public S2C_LoginPacket() {
	}

	public S2C_LoginPacket(C_ServerData view) {
		this.view = view;
	}

	@Override
	public C_ServerData serverWrite(TalkingServerClient client) {
		return view;
	}

	@Override
	public void clientRead(P4JClient client, C_ServerData obj) {
		((TalkingClient) TalkingMain.getInstance()).setServerDataView(obj);
		GlobalLogger.info("Successfull login: " + obj.getChannels());
		AppFrame.INSTANCE.getChannelsPanel().updateList();
	}

}
