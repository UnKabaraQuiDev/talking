package lu.kbra.talking.data;

import java.security.PrivateKey;
import java.security.PublicKey;

public class UserData {

	private final String userName;
	private final String hash;
	private final String version;
	private final PublicKey publicKey;
	private final PrivateKey privateKey;

	public UserData(String userName, String hash, String version, PublicKey publicKey2, PrivateKey privateKey2) {
		this.userName = userName;
		this.hash = hash;
		this.version = version;
		this.publicKey = publicKey2;
		this.privateKey = privateKey2;
	}

	public UserData getPublicUserData() {
		return new UserData(userName, hash, version, publicKey, null);
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

}
