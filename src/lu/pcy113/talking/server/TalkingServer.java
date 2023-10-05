package lu.pcy113.talking.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;

import lu.pcy113.jb.codec.CodecManager;
import lu.pcy113.p4j.compress.CompressionManager;
import lu.pcy113.p4j.crypto.EncryptionManager;
import lu.pcy113.p4j.socket.events.ClientInstanceConnectedEvent;
import lu.pcy113.p4j.socket.server.P4JServer;
import lu.pcy113.p4j.socket.server.ServerClient;
import lu.pcy113.talking.TalkingInstance;
import lu.pcy113.talking.data.UserData;
import lu.pcy113.talking.packets.HandshakePacket;
import lu.pcy113.talking.packets.S2C_LoginPacket;
import lu.pcy113.talking.server.client.TalkingServerClient;
import lu.pcy113.talking.server.conn.ConnectionManager;
import lu.pcy113.talking.server.data.ServerData;

public class TalkingServer implements TalkingInstance {
	
	private String host;
	private int port;
	private P4JServer server;
	
	private List<UserData> connectedUsers;
	
	private CodecManager codec;
	private EncryptionManager encryption;
	private CompressionManager compression;
	
	private ConnectionManager connectionManager;
	
	private ServerData serverData;
	
	public TalkingServer(String host, int port) {
		this.host = host;
		this.port = port;
		
		codec = CodecManager.base();
		encryption = EncryptionManager.raw();
		compression = CompressionManager.raw();
		
		TalkingServer ts = this;
		this.server = new P4JServer(codec, encryption, compression) {
			@Override
			public void clientConnection(SocketChannel sc) {
				TalkingServerClient sclient = new TalkingServerClient(sc, this, ts);
		        registerClient(sclient);
		        listenersConnected.handle(new ClientInstanceConnectedEvent(sclient, this));
			}
		};
		
		registerPackets();
	}
	
	protected void registerPackets() {
		server.registerPacket(HandshakePacket.class, 0x01);
	}
	
	public void connect() throws IOException {
		server.bind(new InetSocketAddress(host, port));
	}

	public void incomingHandshake(ServerClient sclient, UserData obj) {
		boolean accept = connectionManager.verify(sclient);
		if(accept) {
			connectionManager.accept(sclient);
			sclient.write(new S2C_LoginPacket(serverData.getView(obj)));
		}
	}

}
