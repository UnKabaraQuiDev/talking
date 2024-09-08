package lu.kbra.talking.client;

import lu.pcy113.p4j.events.C2SReadPacketEvent;
import lu.pcy113.p4j.events.S2CWritePacketEvent;
import lu.pcy113.p4j.events.client.ClientConnectedEvent;
import lu.pcy113.p4j.events.client.ClientDisconnectedEvent;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.listener.EventHandler;
import lu.pcy113.pclib.listener.EventListener;

public class DefaultClientListener implements EventListener {

	@EventHandler
	public void onConnect(ClientConnectedEvent event) {
		System.out.println("connected: " + ((P4JClient) event.getClient()));
	}

	@EventHandler
	public void onPacketRead(C2SReadPacketEvent event) {
		if (event.hasFailed()) {
			event.getException().printStackTrace(System.out);
		}
	}

	@EventHandler
	public void onPacketWrite(S2CWritePacketEvent event) {
		if (event.hasFailed()) {
			event.getException().printStackTrace(System.out);
		}
	}

	@EventHandler
	public void onDisconnect(ClientDisconnectedEvent event) {
		System.out.println("disconnected: " + ((P4JClient) event.getClient()));
	}

}
