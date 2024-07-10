package lu.kbra.talking.client.data;

import java.security.PublicKey;
import java.util.UUID;

public class C_RemoteUserData {

	private final boolean serverTrusted;
	private final UUID uuid;
	private final String publicHash;
	private final PublicKey publicKey;

	public C_RemoteUserData(boolean serverTrusted, UUID uuid, String publicHash, PublicKey publicKey) {
		this.serverTrusted = serverTrusted;
		this.uuid = uuid;
		this.publicHash = publicHash;
		this.publicKey = publicKey;
	}

	public UUID getUUID() {
		return uuid;
	}

	public String getPublicHash() {
		return publicHash;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public boolean isServerTrusted() {
		return serverTrusted;
	}

}
