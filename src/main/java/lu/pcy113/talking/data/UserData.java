package lu.pcy113.talking.data;

public class UserData {
	
	private String userName;
	private String hash;
	private String privateKey;
	private String publicKey;
	
	public UserData(String userName2, String hash2, String privateKey2, String publicKey2) {
		this.userName = userName2;
		this.hash = hash2;
		this.privateKey = privateKey2;
		this.publicKey = publicKey2;
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
