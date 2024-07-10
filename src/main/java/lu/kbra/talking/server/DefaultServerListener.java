package lu.kbra.talking.server;

import lu.pcy113.p4j.events.C2SReadPacketEvent;
import lu.pcy113.p4j.events.ClientConnectedEvent;
import lu.pcy113.p4j.events.ClosedSocketEvent;
import lu.pcy113.p4j.events.S2CWritePacketEvent;
import lu.pcy113.pclib.listener.EventHandler;
import lu.pcy113.pclib.listener.EventListener;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.packets.S2C_ChannelLeavePacket;
import lu.kbra.talking.server.client.TalkingServerClient;

public class DefaultServerListener implements EventListener {

	@EventHandler
	public void onConnect(ClientConnectedEvent event) {
		TalkingServerClient sclient = (TalkingServerClient) event.getClient();

		GlobalLogger.log("connected: " + sclient.getUUID());
	}

	@EventHandler
	public void onPacketRead(C2SReadPacketEvent event) {
		GlobalLogger.log("read: " + event.getPacketClass());
		if (event.hasFailed()) {
			event.getException().printStackTrace();
		}
	}

	@EventHandler
	public void onPacketWrite(S2CWritePacketEvent event) {
		GlobalLogger.log("wrote: " + event.getPacket().getClass());
		if (event.hasFailed()) {
			event.getException().printStackTrace();
		}
	}

	@EventHandler
	public void onDisconnect(ClosedSocketEvent event) {
		TalkingServerClient sclient = (TalkingServerClient) event.getClient();
		GlobalLogger.log("disconnected: " + sclient.getUUID());

		if(!sclient.hasUserData())
			return;
		
		TalkingServer.INSTANCE.getServer().broadcastIf(S2C_ChannelLeavePacket.disconnected(sclient.getUserData().getUserName()),
				c -> ((TalkingServerClient) c).getUserData().getCurrentChannelUuid().equals(sclient.getUserData().getCurrentChannelUuid()));
	}

}
