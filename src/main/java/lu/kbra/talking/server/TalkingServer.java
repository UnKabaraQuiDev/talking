package lu.kbra.talking.server;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;

import org.json.JSONObject;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.p4j.compress.CompressionManager;
import lu.pcy113.p4j.crypto.EncryptionManager;
import lu.pcy113.p4j.socket.server.P4JServer;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.builder.ThreadBuilder;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.TalkingInstance;
import lu.kbra.talking.consts.Codecs;
import lu.kbra.talking.consts.Consts;
import lu.kbra.talking.consts.Packets;
import lu.kbra.talking.packets.C2S_S2C_ChangeChannelPacket;
import lu.kbra.talking.packets.S2C_LoginPacket;
import lu.kbra.talking.packets.S2C_LoginRefusedPacket;
import lu.kbra.talking.server.client.TalkingClientManager;
import lu.kbra.talking.server.client.TalkingServerClient;
import lu.kbra.talking.server.conn.ConnectionManager;
import lu.kbra.talking.server.conn.verifier.VersionConnectionVerifier;
import lu.kbra.talking.server.data.S_ServerData;
import lu.kbra.talking.server.data.S_UserData;

public class TalkingServer implements TalkingInstance {

	public static TalkingServer INSTANCE = null;

	private P4JServer server;

	private CodecManager codec;
	private EncryptionManager encryption;
	private CompressionManager compression;

	private ConnectionManager connectionManager;

	private boolean shouldRun = true;

	private S_ServerData serverData;

	public TalkingServer(String host, int port) throws IOException {
		INSTANCE = this;

		codec = Codecs.instance();
		encryption = EncryptionManager.raw();
		compression = CompressionManager.raw();

		if (!loadServerData()) {
			GlobalLogger.severe("Stopping.");
			return;
		}

		this.connectionManager = new ConnectionManager();
		// this.connectionManager.registerVerifier(true, new BlacklistConnectionVerifier("./config/blacklist.json"));
		this.connectionManager.registerVerifier(true, new VersionConnectionVerifier(Consts.VERSION));

		this.server = new P4JServer(codec, encryption, compression);
		this.server.setClientManager(new TalkingClientManager(this));

		// this.server.setEventManager(new AsyncEventManager(false));
		this.server.getEventManager().register(new DefaultServerListener());
		this.server.getEventManager().setExceptionHandler(GlobalLogger::severe);

		Packets.registerPackets(server.getPackets());

		server.bind(new InetSocketAddress(InetAddress.getByName(host), port));
		this.server.setAccepting();

		Runtime.getRuntime().addShutdownHook(ThreadBuilder.create(() -> TalkingServer.INSTANCE.getServerData().saveTrustedPublicKeys()).build());
	}

	private boolean loadServerData() {
		try {
			final File dataFile = new File("./config/serverdata.json");

			if (!dataFile.exists()) {
				dataFile.createNewFile();
				Files.write(Paths.get(dataFile.getPath()), "{\"defaultChannel\": \"\", \"channels\": []}".getBytes());
				GlobalLogger.info("Created ServerData configuration file, please fill it.");
				return false;
			}

			final JSONObject config = new JSONObject(new String(Files.readAllBytes(Paths.get(dataFile.getPath()))));

			if (config.getJSONArray("channels").isEmpty()) {
				GlobalLogger.info("No channel configured.");
				return false;
			}

			if (!config.has("defaultChannel") || config.getString("defaultChannel") == null || config.getString("defaultChannel").trim().isEmpty()) {
				GlobalLogger.info("No default channel configured.");
				return false;
			}

			this.serverData = new S_ServerData(config.getJSONArray("channels").toList().stream().map(Objects::toString).distinct().collect(Collectors.toList()));
			this.serverData.setDefaultChannel(this.serverData.getChannelByName(config.getString("defaultChannel")));

			return true;
		} catch (IOException e) {
			GlobalLogger.severe(e);
			return false;
		}
	}

	public void incomingHandshake(TalkingServerClient sclient, S_UserData obj) {
		sclient.setUserData(obj);

		Pair<Boolean, String> refusalReason = connectionManager.verify(sclient);
		if (refusalReason.getKey()) {
			GlobalLogger.info("Accepted connection: " + sclient.getUUID() + " = '" + sclient.getUserData().getUserName() + "' @ "
					+ PCUtils.try_(() -> sclient.getSocketChannel().getRemoteAddress(), (e) -> e.getClass().getName() + ": " + e.getMessage()));

			obj.verifyServerTrusted(serverData);

			sclient.write(new S2C_LoginPacket(serverData.getClientView(obj)));
			new C2S_S2C_ChangeChannelPacket().serverRead(sclient, TalkingServer.INSTANCE.getServerData().getDefaultChannelUuid()); // fake packet received from client
		} else {
			GlobalLogger.info("Refused connection: " + refusalReason.getValue());
			sclient.write(new S2C_LoginRefusedPacket(refusalReason.getValue()));
			sclient.disconnect();
		}
	}

	public P4JServer getServer() {
		return server;
	}

	public S_ServerData getServerData() {
		return serverData;
	}

	public void stop() {
		shouldRun = false;
		server.disconnectAll();
		server.close();
		GlobalLogger.info("Stopped.");
	}

	public boolean shouldRun() {
		return shouldRun;
	}

}
