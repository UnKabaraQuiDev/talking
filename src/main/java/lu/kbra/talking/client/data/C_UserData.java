package lu.kbra.talking.client.data;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

import lu.kbra.talking.server.data.S_UserData;

public class C_UserData {

	private final String userName;
	private final String hash;
	private final String version;
	private final PublicKey publicKey;
	private final PrivateKey privateKey;

	private UUID currentChannelUuid;

	public C_UserData(String userName, String hash, String version, PublicKey publicKey2, PrivateKey privateKey2) {
		this.userName = userName;
		this.hash = hash;
		this.version = version;
		this.publicKey = publicKey2;
		this.privateKey = privateKey2;
	}

	public S_UserData getPublicUserData() {
		return new S_UserData(userName, hash, version, publicKey);
	}

	public String getVersion() {
		return version;
	}

	public String getUserName() {
		return userName;
	}

	public String getHash() {
		return hash;
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

}
