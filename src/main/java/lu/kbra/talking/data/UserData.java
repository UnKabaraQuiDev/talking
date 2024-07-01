package lu.kbra.talking.data;

import java.security.PrivateKey;
import java.security.PublicKey;

public class UserData {

	private String userName;
	private String hash;
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public UserData(String userName, String hash, PublicKey publicKey2, PrivateKey privateKey2) {
		this.userName = userName;
		this.hash = hash;
		this.publicKey = publicKey2;
		this.privateKey = privateKey2;
	}

	public UserData getPublicUserData() {
		return new UserData(userName, hash, publicKey, null);
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
