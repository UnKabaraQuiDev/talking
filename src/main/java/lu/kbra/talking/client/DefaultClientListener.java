package lu.kbra.talking.client;

import lu.pcy113.p4j.events.C2SReadPacketEvent;
import lu.pcy113.p4j.events.ClientConnectedEvent;
import lu.pcy113.p4j.events.ClosedSocketEvent;
import lu.pcy113.p4j.events.S2CWritePacketEvent;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.listener.EventHandler;
import lu.pcy113.pclib.listener.EventListener;
import lu.pcy113.pclib.logger.GlobalLogger;

public class DefaultClientListener implements EventListener {

	@EventHandler
	public void onConnect(ClientConnectedEvent event) {
		GlobalLogger.log("connected: " + ((P4JClient) event.getClient()));
	}

	@EventHandler
	public void onPacketRead(C2SReadPacketEvent event) {
		GlobalLogger.log("read: " + event.getPacketClass());
		if(event.hasFailed()) {
			event.getException().printStackTrace();
		}
	}
	
	@EventHandler
	public void onPacketWrite(S2CWritePacketEvent event) {
		GlobalLogger.log("wrote: " + event.getPacket().getClass());
		if(event.hasFailed()) {
			event.getException().printStackTrace();
		}
	}

	@EventHandler
	public void onDisconnect(ClosedSocketEvent event) {
		GlobalLogger.log("disconnected: " + ((P4JClient) event.getClient()));
	}

}
