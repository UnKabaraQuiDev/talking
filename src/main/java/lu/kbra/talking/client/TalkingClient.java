package lu.kbra.talking.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import lu.kbra.talking.TalkingInstance;
import lu.kbra.talking.consts.Codecs;
import lu.kbra.talking.consts.Packets;
import lu.kbra.talking.data.UserData;
import lu.kbra.talking.packets.C2S_HandshakePacket;
import lu.kbra.talking.server.data.ServerDataView;
import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.p4j.compress.CompressionManager;
import lu.pcy113.p4j.crypto.EncryptionManager;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;

public class TalkingClient implements TalkingInstance {

	public static TalkingClient INSTANCE = null;

	private String host;
	private int port;
	private P4JClient client;

	private UserData userData;

	private CodecManager codec;
	private EncryptionManager encryption;
	private CompressionManager compression;

	private ServerDataView serverData;

	private ConsoleClient consoleClient;

	public TalkingClient(String host, int port) throws IOException {
		INSTANCE = this;

		this.host = host;
		this.port = port;

		codec = Codecs.instance();
		encryption = EncryptionManager.raw();
		compression = CompressionManager.raw();

		KeyPair keys = genKeys();
		this.userData = new UserData("name", "hash", keys.getPublic(), keys.getPrivate());

		client = new P4JClient(codec, encryption, compression);

		this.consoleClient = new ConsoleClient();

		Packets.registerPackets(client.getPackets());

		connect();
	}

	private KeyPair genKeys() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");

			kpg.initialize(2048);
			return kpg.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public void connect() throws IOException {
		client.bind();

		client.connect(new InetSocketAddress(host, port));

		GlobalLogger.log("Public key: " + PCUtils.byteArrayToHexString(userData.getPublicKey().getEncoded()));
		client.write(new C2S_HandshakePacket(userData.getPublicUserData()));
	}

	public UserData getUserData() {
		return userData;
	}

	public P4JClient getClient() {
		return client;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setServerDataView(ServerDataView obj) {
		this.serverData = obj;
	}

}
