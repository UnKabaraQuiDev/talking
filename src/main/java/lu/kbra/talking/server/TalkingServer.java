package lu.kbra.talking.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.p4j.compress.CompressionManager;
import lu.pcy113.p4j.crypto.EncryptionManager;
import lu.pcy113.p4j.socket.server.ClientManager;
import lu.pcy113.p4j.socket.server.P4JServer;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.listener.AsyncEventManager;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.TalkingInstance;
import lu.kbra.talking.consts.Codecs;
import lu.kbra.talking.consts.Consts;
import lu.kbra.talking.consts.Packets;
import lu.kbra.talking.packets.C2S_HandshakePacket.HandShakeData;
import lu.kbra.talking.packets.S2C_LoginPacket;
import lu.kbra.talking.packets.S2C_LoginRefusedPacket;
import lu.kbra.talking.server.client.TalkingServerClient;
import lu.kbra.talking.server.conn.ConnectionManager;
import lu.kbra.talking.server.conn.verifier.BlacklistConnectionVerifier;
import lu.kbra.talking.server.conn.verifier.VersionConnectionVerifier;
import lu.kbra.talking.server.data.ServerData;

public class TalkingServer implements TalkingInstance {

	public static TalkingServer INSTANCE = null;

	private P4JServer server;

	private CodecManager codec;
	private EncryptionManager encryption;
	private CompressionManager compression;

	private ConnectionManager connectionManager;

	private ServerData serverData;

	public TalkingServer(String host, int port) throws IOException {
		INSTANCE = this;

		codec = Codecs.instance();
		encryption = EncryptionManager.raw();
		compression = CompressionManager.raw();

		this.serverData = new ServerData("test0", "test1");

		this.connectionManager = new ConnectionManager();
		this.connectionManager.registerVerifier(true, new BlacklistConnectionVerifier("./config/blacklist.json"));
		this.connectionManager.registerVerifier(true, new VersionConnectionVerifier(Consts.VERSION));

		this.server = new P4JServer(codec, encryption, compression);
		this.server.setClientManager(new ClientManager(server, (socket) -> new TalkingServerClient(socket, this.server, this)));

		this.server.setEventManager(new AsyncEventManager(false));
		this.server.getEventManager().register(new DefaultServerListener());

		Packets.registerPackets(server.getPackets());

		server.bind(new InetSocketAddress(host, port));
		this.server.setAccepting();
	}

	public void incomingHandshake(TalkingServerClient sclient, HandShakeData obj) {
		sclient.setUserData(obj.userData);
		sclient.getUserData().setCurrentChannelUuid(serverData.getDefaultChannelUuid());
		Pair<Boolean, String> refusalReason = connectionManager.verify(sclient);
		if (refusalReason.getKey()) {
			GlobalLogger.info("Accepted connection: " + sclient.getUUID() + " = '" + sclient.getUserData().getUserName() + "' @ "
					+ PCUtils.try_(() -> sclient.getSocketChannel().getRemoteAddress(), (e) -> e.getClass().getName() + ": " + e.getMessage()));
			sclient.write(new S2C_LoginPacket(serverData.getView(obj.userData)));
		} else {
			GlobalLogger.info("Refused connection: " + refusalReason.getValue());
			sclient.write(new S2C_LoginRefusedPacket(refusalReason.getValue()));
			sclient.disconnect();
		}
	}

	public P4JServer getServer() {
		return server;
	}

	public ServerData getServerData() {
		return serverData;
	}

}
