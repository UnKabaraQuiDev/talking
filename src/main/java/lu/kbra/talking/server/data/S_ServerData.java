package lu.kbra.talking.server.data;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.client.data.C_ServerData;
import lu.kbra.talking.data.Channel;

public class S_ServerData {

	private UUID defaultChannelUuid;

	private Map<String, Channel> channels;

	private Set<PublicKey> trustedPublicKeys = new HashSet<>();

	public S_ServerData(List<Channel> channels) {
		this.channels = channels.stream().collect(Collectors.toMap(Channel::getName, a -> a));
		this.defaultChannelUuid = channels.get(0).getUuid();

		loadTrustedPublicKeys();
	}

	public S_ServerData(String... strings) {
		this.channels = new HashMap<String, Channel>();
		for (String s : strings) {
			this.channels.put(s, new Channel(s, UUID.randomUUID()));
		}
		this.defaultChannelUuid = channels.values().iterator().next().getUuid();

		loadTrustedPublicKeys();
	}

	public void addTrustedPublicKey(PublicKey pk) {
		this.trustedPublicKeys.add(pk);
	}

	public boolean isTrustedPublicKey(PublicKey pk) {
		return this.trustedPublicKeys.contains(pk);
	}

	public Map<String, Channel> getChannels() {
		return channels;
	}

	public UUID getDefaultChannelUuid() {
		return defaultChannelUuid;
	}

	public void setDefaultChannelUuid(UUID defaultChannelUuid) {
		this.defaultChannelUuid = defaultChannelUuid;
	}

	public Channel getDefaultChannel() {
		return channels.values().stream().filter(c -> c.getUuid().equals(defaultChannelUuid)).findFirst().get();
	}

	public void setDefaultChannel(Channel obj) {
		this.defaultChannelUuid = this.channels.values().stream().filter(c -> c.equals(obj)).findFirst().get().getUuid();
	}

	public C_ServerData getClientView(S_UserData obj) {
		return new C_ServerData(channels.values().stream().collect(Collectors.toList()), defaultChannelUuid);
	}

	public Channel getChannel(UUID obj) {
		return channels.values().stream().filter(c -> c.getUuid().equals(obj)).findFirst().get();
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

			GlobalLogger.log("Loaded trusted public keys (" + trustedPublicKeys.size() + ")");
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

			GlobalLogger.log("Saved trusted public keys (" + trustedPublicKeys.size() + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
