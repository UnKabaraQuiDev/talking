package lu.kbra.talking.client;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.p4j.compress.CompressionManager;
import lu.pcy113.p4j.crypto.EncryptionManager;
import lu.pcy113.p4j.socket.client.ClientStatus;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.TalkingInstance;
import lu.kbra.talking.client.data.C_RemoteUserData;
import lu.kbra.talking.client.data.C_ServerData;
import lu.kbra.talking.client.data.C_UserData;
import lu.kbra.talking.client.frame.AppFrame;
import lu.kbra.talking.consts.Codecs;
import lu.kbra.talking.consts.Consts;
import lu.kbra.talking.consts.Packets;
import lu.kbra.talking.packets.C2S_HandshakePacket;

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

	private AppFrame frame;

	private Set<PublicKey> trustedPublicKeys;

	public TalkingClient(int localPort) throws IOException {
		INSTANCE = this;

		this.localPort = localPort;

		codec = Codecs.instance();
		encryption = EncryptionManager.raw();
		compression = CompressionManager.raw();

		this.frame = new AppFrame();

		this.loadTrustedPublicKeys();
	}

	private void createClient() {
		client = new P4JClient(codec, encryption, compression);
		this.client.getEventManager().register(new DefaultClientListener());
		Packets.registerPackets(client.getPackets());
	}

	public static KeyPair genKeys(byte[] seed) {
		try {
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(seed);

			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048, secureRandom); // 2048-bit key size

			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}

	public void disconnect() {
		if (client != null && client.getClientStatus().equals(ClientStatus.LISTENING)) {
			client.disconnect();
			client = null;
		}
	}

	public void connect(String username, String password, String host, int port) throws Exception {
		if (client != null && client.getClientStatus().equals(ClientStatus.LISTENING)) {
			System.out.println("Disconnect before reconnecting");
			return;
		}

		final String privateHash = PCUtils.hashString(username + password, "SHA-256");
		KeyPair keys = genKeys(privateHash.getBytes());
		final String publicHash = PCUtils.hashString(username + keys.getPublic().getEncoded(), "SHA-256");
		this.userData = new C_UserData(username, publicHash, Consts.VERSION, keys.getPublic(), keys.getPrivate());
		System.out.println("User data: " + userData);

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

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public AppFrame getFrame() {
		return frame;
	}

	public boolean isConnected() {
		return client != null && client.getClientStatus().equals(ClientStatus.LISTENING);
	}

	private void loadTrustedPublicKeys() {
		try {
			File file = new File("./config/trusted.txt");

			trustedPublicKeys = new HashSet<PublicKey>();
			if (!file.exists()) {
				file.createNewFile();
				return;
			} else {
				List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
				for (String str : lines) {
					byte[] keyBytes = Base64.getDecoder().decode(str.trim());
					X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
					KeyFactory keyFactory = KeyFactory.getInstance("RSA");
					trustedPublicKeys.add(keyFactory.generatePublic(keySpec));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveTrustedPublicKeys() {
		try {
			File file = new File("./config/trusted.txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			List<String> lines = new ArrayList<String>(trustedPublicKeys.size());
			for (PublicKey pk : trustedPublicKeys) {
				byte[] keyBytes = pk.getEncoded();
				String keyString = Base64.getEncoder().encodeToString(keyBytes);
				lines.add(keyString);
			}

			Files.write(Paths.get(file.getPath()), lines);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isClientTrusted(C_RemoteUserData u) {
		return trustedPublicKeys.contains(u.getPublicKey());
	}

	public void addClientTrusted(UUID from) {
		Optional<C_RemoteUserData> remoteUser = serverData.getRemoteUsers().stream().filter((c) -> c.getUUID().equals(from)).findAny();
		if (remoteUser.isPresent()) {
			trustedPublicKeys.add(remoteUser.get().getPublicKey());
			System.out.println("Added user: " + from + " to trusted users");
		}
	}

}
