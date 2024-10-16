package lu.kbra.talking.client;

import lu.pcy113.p4j.events.client.P4JConnectionEvent.ClientConnectedEvent;
import lu.pcy113.p4j.events.client.P4JConnectionEvent.ClientDisconnectedEvent;
import lu.pcy113.p4j.events.packets.PacketEvent.ReadFailedPacketEvent;
import lu.pcy113.p4j.events.packets.PacketEvent.WriteFailedPacketEvent;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.listener.EventHandler;
import lu.pcy113.pclib.listener.EventListener;

public class DefaultClientListener implements EventListener {

	@EventHandler
	public void onConnect(ClientConnectedEvent event) {
		System.out.println("connected: " + ((P4JClient) event.getClient()));
	}

	@EventHandler
	public void onPacketRead(ReadFailedPacketEvent event) {
		event.getException().printStackTrace(System.out);
	}

	@EventHandler
	public void onPacketWrite(WriteFailedPacketEvent event) {
		event.getException().printStackTrace(System.out);
	}

	@EventHandler
	public void onDisconnect(ClientDisconnectedEvent event) {
		System.out.println("disconnected: " + ((P4JClient) event.getClient()));
	}

}
