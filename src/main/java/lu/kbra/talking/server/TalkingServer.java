package lu.kbra.talking.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.p4j.compress.CompressionManager;
import lu.pcy113.p4j.crypto.EncryptionManager;
import lu.pcy113.p4j.socket.server.ClientManager;
import lu.pcy113.p4j.socket.server.P4JServer;

import lu.kbra.talking.TalkingInstance;
import lu.kbra.talking.consts.Codecs;
import lu.kbra.talking.consts.Packets;
import lu.kbra.talking.packets.S2C_LoginPacket;
import lu.kbra.talking.packets.S2C_LoginRefusedPacket;
import lu.kbra.talking.packets.C2S_HandshakePacket.HandShakeData;
import lu.kbra.talking.server.client.TalkingServerClient;
import lu.kbra.talking.server.conn.ConnectionManager;
import lu.kbra.talking.server.data.ServerData;

public class TalkingServer implements TalkingInstance {
	
	private String host;
	private int port;
	private P4JServer server;
	
	private CodecManager codec;
	private EncryptionManager encryption;
	private CompressionManager compression;
	
	private ConnectionManager connectionManager;
	
	private ServerData serverData;
	
	public TalkingServer(String host, int port) throws IOException {
		this.host = host;
		this.port = port;
		
		codec = Codecs.instance();
		encryption = EncryptionManager.raw();
		compression = CompressionManager.raw();
		
		this.server = new P4JServer(codec, encryption, compression);
		this.server.setClientManager(new ClientManager(server, (socket) -> new TalkingServerClient(socket, this.server, this)));
		
		Packets.registerPackets(server.getPackets());
		
		connect();
		this.server.setAccepting();
	}
	
	public void connect() throws IOException {
		server.bind(new InetSocketAddress(host, port));
	}

	public void incomingHandshake(TalkingServerClient sclient, HandShakeData obj) {
		boolean accept = connectionManager.verify(sclient);
		if(accept) {
			sclient.write(new S2C_LoginPacket(serverData.getView(obj.userData)));
		}else {
			sclient.write(new S2C_LoginRefusedPacket());
			sclient.close();
		}
	}

}
