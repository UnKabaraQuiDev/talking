package lu.kbra.talking.client;

import java.io.IOException;
import java.net.InetSocketAddress;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.p4j.compress.CompressionManager;
import lu.pcy113.p4j.crypto.EncryptionManager;
import lu.pcy113.p4j.socket.client.P4JClient;

import lu.kbra.talking.TalkingInstance;
import lu.kbra.talking.consts.Packets;
import lu.kbra.talking.data.UserData;
import lu.kbra.talking.packets.C2S_HandshakePacket;
import lu.kbra.talking.server.data.ServerDataView;

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
		
		Packets.registerPackets(client.getPackets());
	}
	
	public void connect() throws IOException {
		client.connect(new InetSocketAddress(host, port));
		
		client.write(new C2S_HandshakePacket(userData));
	}
	
	public String getHost() {return host;}
	public void setHost(String host) {this.host = host;}

	public void setServerDataView(ServerDataView obj) {
		this.serverData = obj;	
	}

}
