package lu.kbra.talking.server;

import lu.pcy113.p4j.events.C2SReadPacketEvent;
import lu.pcy113.p4j.events.ClientConnectedEvent;
import lu.pcy113.p4j.events.ClosedSocketEvent;
import lu.pcy113.p4j.events.S2CWritePacketEvent;
import lu.pcy113.pclib.listener.EventHandler;
import lu.pcy113.pclib.listener.EventListener;
import lu.pcy113.pclib.logger.GlobalLogger;

public class DefaultServerListener implements EventListener {

	@EventHandler
	public void onConnect(ClientConnectedEvent event) {
		GlobalLogger.log("connected: " + event);
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
		GlobalLogger.log("disconnected: " + event);
	}

}
