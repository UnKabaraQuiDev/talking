package lu.pcy113.talking.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import lu.pcy113.jb.codec.CodecManager;
import lu.pcy113.p4j.compress.CompressionManager;
import lu.pcy113.p4j.crypto.EncryptionManager;
import lu.pcy113.p4j.socket.server.ClientManager;
import lu.pcy113.p4j.socket.server.P4JServer;
import lu.pcy113.talking.TalkingInstance;
import lu.pcy113.talking.consts.Codecs;
import lu.pcy113.talking.consts.Packets;
import lu.pcy113.talking.data.UserData;
import lu.pcy113.talking.packets.S2C_LoginPacket;
import lu.pcy113.talking.packets.S2C_LoginRefusedPacket;
import lu.pcy113.talking.server.client.TalkingServerClient;
import lu.pcy113.talking.server.conn.ConnectionManager;
import lu.pcy113.talking.server.data.ServerData;

public class TalkingServer implements TalkingInstance {
	
	private String host;
	private int port;
	private P4JServer server;
	
	private CodecManager codec;
	private EncryptionManager encryption;
	private CompressionManager compression;
	
	private ConnectionManager connectionManager;
	
	private ServerData serverData;
	
	public TalkingServer(String host, int port) {
		this.host = host;
		this.port = port;
		
		codec = Codecs.instance();
		encryption = EncryptionManager.raw();
		compression = CompressionManager.raw();
		
		this.server = new P4JServer(codec, encryption, compression);
		this.server.setClientManager(new ClientManager(server, (socket) -> new TalkingServerClient(socket, this.server, this)));
		
		Packets.registerPackets(server.getPackets());
	}
	
	public void connect() throws IOException {
		server.bind(new InetSocketAddress(host, port));
	}

	public void incomingHandshake(TalkingServerClient sclient, UserData obj) {
		boolean accept = connectionManager.verify(sclient);
		if(accept) {
			sclient.write(new S2C_LoginPacket(serverData.getView(obj)));
		}else {
			sclient.write(new S2C_LoginRefusedPacket());
			sclient.close();
		}
	}

}
