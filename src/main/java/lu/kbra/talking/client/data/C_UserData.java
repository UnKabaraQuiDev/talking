package lu.kbra.talking.client.data;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

import lu.kbra.talking.server.data.S_UserData;

public class C_UserData {

	// client set
	private final String userName;
	private final String privateHash, publicHash;
	private final String version;
	private final PublicKey publicKey;
	private final PrivateKey privateKey;

	// server set
	private UUID uuid;
	private boolean serverTrusted = false;
	private UUID currentChannelUuid;

	public C_UserData(String userName, String privateHash, String publicHash, String version, PublicKey publicKey2, PrivateKey privateKey2) {
		this.userName = userName;
		this.privateHash = privateHash;
		this.publicHash = publicHash;
		this.version = version;
		this.publicKey = publicKey2;
		this.privateKey = privateKey2;
	}

	public S_UserData getPublicUserData() {
		return new S_UserData(userName, publicHash, version, publicKey);
	}

	public String getVersion() {
		return version;
	}

	public String getUserName() {
		return userName;
	}

	public String getPrivateHash() {
		return privateHash;
	}

	public String getPublicHash() {
		return publicHash;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public UUID getCurrentChannelUuid() {
		return currentChannelUuid;
	}

	public void setCurrentChannelUuid(UUID currentChannel) {
		this.currentChannelUuid = currentChannel;
	}

	public boolean isServerTrusted() {
		return serverTrusted;
	}

	public UUID getUuid() {
		return uuid;
	}

	@Override
	public String toString() {
		return "User: " + userName + " - " + publicHash + " - " + version + " - " + publicKey + " - " + privateKey + " - " + currentChannelUuid;
	}

	public void update(C_RemoteUserData u) {
		this.serverTrusted = u.isServerTrusted();
		this.uuid = u.getUUID();
	}

}
