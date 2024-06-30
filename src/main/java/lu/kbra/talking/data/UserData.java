package lu.kbra.talking.data;

public class UserData {
	
	private String userName;
	private String hash;
	private String privateKey;
	private String publicKey;
	
	public UserData(String userName, String hash, String privateKey, String publicKey) {
		this.userName = userName;
		this.hash = hash;
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}
	
	public String getUserName() {
		return userName;
	}
	public String getHash() {
		return hash;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public String getPublicKey() {
		return publicKey;
	}
	
}
