package lu.kbra.talking.server;

import java.util.Arrays;

import lu.pcy113.p4j.events.client.P4JConnectionEvent.ClientConnectedEvent;
import lu.pcy113.p4j.events.client.P4JConnectionEvent.ClientDisconnectedEvent;
import lu.pcy113.p4j.events.packets.PacketEvent.ReadFailedPacketEvent;
import lu.pcy113.p4j.events.packets.PacketEvent.WriteFailedPacketEvent;
import lu.pcy113.pclib.listener.EventHandler;
import lu.pcy113.pclib.listener.EventListener;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.packets.S2C_ChannelLeavePacket;
import lu.kbra.talking.packets.S2C_UpdateRemoteUserDataListPacket;
import lu.kbra.talking.server.client.TalkingServerClient;

public class DefaultServerListener implements EventListener {

	@EventHandler
	public void onConnect(ClientConnectedEvent event) {
		TalkingServerClient sclient = (TalkingServerClient) event.getClient();

		GlobalLogger.log("Client connected: " + sclient.getUUID());
	}

	@EventHandler
	public void onPacketRead(ReadFailedPacketEvent event) {
		GlobalLogger.severe(event.getException());
	}

	@EventHandler
	public void onPacketWrite(WriteFailedPacketEvent event) {
		GlobalLogger.severe(event.getException());
	}

	@EventHandler
	public void onDisconnect(ClientDisconnectedEvent event) {
		TalkingServerClient sclient = (TalkingServerClient) event.getClient();
		GlobalLogger.log("Client disconnected: " + sclient.getUUID() + " (" + (sclient.hasUserData() ? "logged in" : "not logged in") + ")");

		if (!sclient.hasUserData())
			return;

		TalkingServer.INSTANCE.getServer().broadcastIf(Arrays.asList(S2C_ChannelLeavePacket.disconnected(sclient.getUserData().getUserName()), S2C_UpdateRemoteUserDataListPacket.remove(sclient.getUserData().getRemoteUserData(sclient))),
				c -> !c.equals(sclient) && ((TalkingServerClient) c).getUserData().getCurrentChannelUuid().equals(sclient.getUserData().getCurrentChannelUuid()));
	}

}
