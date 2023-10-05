package lu.pcy113.talking.client;

import java.io.IOException;
import java.net.InetSocketAddress;

import lu.pcy113.jb.codec.CodecManager;
import lu.pcy113.p4j.compress.CompressionManager;
import lu.pcy113.p4j.crypto.EncryptionManager;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.talking.TalkingInstance;
import lu.pcy113.talking.data.UserData;
import lu.pcy113.talking.packets.HandshakePacket;
import lu.pcy113.talking.server.data.ServerDataView;

public class TalkingClient implements TalkingInstance {
	
	private String host;
	private int port;
	private P4JClient client;
	
	private UserData userData;
	
	private CodecManager codec;
	private EncryptionManager encryption;
	private CompressionManager compression;
	
	private ServerDataView serverData;
	
	public TalkingClient(String host, int port) {
		this.host = host;
		this.port = port;
		
		codec = CodecManager.base();
		encryption = EncryptionManager.raw();
		compression = CompressionManager.raw();
		
		client = new P4JClient(codec, encryption, compression);
		
		registerPackets();
	}
	
	protected void registerPackets() {
		client.registerPacket(HandshakePacket.class, 0x01);
	}
	
	public void connect() throws IOException {
		client.connect(new InetSocketAddress(host, port));
		
		client.write(new HandshakePacket(userData));
	}
	
	public String getHost() {return host;}
	public void setHost(String host) {this.host = host;}

	public void setServerDataView(ServerDataView obj) {
		this.serverData = obj;	
	}

}
