package lu.kbra.talking.client.data;

import java.security.PublicKey;
import java.util.UUID;

public class C_RemoteUserData {

	private UUID uuid;
	private PublicKey publicKey;

	public C_RemoteUserData(UUID uuid, PublicKey publicKey) {
		super();
		this.uuid = uuid;
		this.publicKey = publicKey;
	}

	public UUID getUUID() {
		return uuid;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

}
