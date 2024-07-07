package lu.kbra.talking.client;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import lu.kbra.talking.TalkingInstance;
import lu.kbra.talking.client.data.C_ServerData;
import lu.kbra.talking.client.data.C_UserData;
import lu.kbra.talking.consts.Codecs;
import lu.kbra.talking.consts.Consts;
import lu.kbra.talking.consts.Packets;
import lu.kbra.talking.packets.C2S_HandshakePacket;
import lu.kbra.talking.server.client.DefaultClientListener;
import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.p4j.compress.CompressionManager;
import lu.pcy113.p4j.crypto.EncryptionManager;
import lu.pcy113.p4j.socket.client.ClientStatus;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;

public class TalkingClient implements TalkingInstance {

	public static TalkingClient INSTANCE = null;

	private int localPort;
	
	private String remoteHost;
	private int remotePort;
	private P4JClient client;

	private C_UserData userData;

	private CodecManager codec;
	private EncryptionManager encryption;
	private CompressionManager compression;

	private C_ServerData serverData;

	private ConsoleClient consoleClient;

	public TalkingClient(int localPort) throws IOException {
		INSTANCE = this;

		this.localPort = localPort;
		
		codec = Codecs.instance();
		encryption = EncryptionManager.raw();
		compression = CompressionManager.raw();

		KeyPair keys = genKeys();
		this.userData = new C_UserData("name", "hash", Consts.VERSION, keys.getPublic(), keys.getPrivate());

		this.consoleClient = new ConsoleClient();
	}

	private void createClient() {
		client = new P4JClient(codec, encryption, compression);
		this.client.getEventManager().register(new DefaultClientListener());
		Packets.registerPackets(client.getPackets());
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

	public void disconnect() {
		if (client != null && client.getClientStatus().equals(ClientStatus.LISTENING)) {
			client.disconnect();
			client = null;
		}
	}

	public void connect(String host, int port) throws IOException {
		if (client != null && client.getClientStatus().equals(ClientStatus.LISTENING)) {
			System.out.println("Disconnect before reconnecting");
			return;
		}

		this.remoteHost = host;
		this.remotePort = port;

		if (client == null) {
			createClient();
		}

		client.bind(localPort);
		System.out.println("Client bound on: " + client.getLocalInetSocketAddress());

		System.out.println("connecting to: " + Inet4Address.getByName(remoteHost));
		client.connect(new InetSocketAddress(Inet4Address.getByName(remoteHost), remotePort));

		GlobalLogger.log("Public key: " + PCUtils.byteArrayToHexString(userData.getPublicKey().getEncoded()));
		client.write(new C2S_HandshakePacket(userData.getPublicUserData()));
	}

	public C_UserData getUserData() {
		return userData;
	}

	public P4JClient getClient() {
		return client;
	}

	public String getHost() {
		return remoteHost;
	}

	public void setHost(String host) {
		this.remoteHost = host;
	}

	public C_ServerData getServerData() {
		return serverData;
	}

	public void setServerDataView(C_ServerData obj) {
		this.serverData = obj;
	}

	public ConsoleClient getConsoleClient() {
		return consoleClient;
	}
	
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public boolean isConnected() {
		return client != null && client.getClientStatus().equals(ClientStatus.LISTENING);
	}

}
