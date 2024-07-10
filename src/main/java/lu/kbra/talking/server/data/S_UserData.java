package lu.kbra.talking.server.data;

import java.security.PublicKey;
import java.util.UUID;

import lu.kbra.talking.client.data.C_RemoteUserData;
import lu.kbra.talking.data.Channel;
import lu.kbra.talking.server.client.TalkingServerClient;

public class S_UserData {

	private boolean serverTrusted = false;

	private final String userName;
	private final String publicHash;
	private final String version;
	private final PublicKey publicKey;

	private UUID currentChannelUuid;

	public S_UserData(String userName, String hash, String version, PublicKey publicKey) {
		this.userName = userName;
		this.publicHash = hash;
		this.version = version;
		this.publicKey = publicKey;
	}

	public S_UserData(boolean serverTrusted, String userName, String publicHash, String version, PublicKey publicKey, UUID currentChannelUuid) {
		this.serverTrusted = serverTrusted;
		this.userName = userName;
		this.publicHash = publicHash;
		this.version = version;
		this.publicKey = publicKey;
		this.currentChannelUuid = currentChannelUuid;
	}

	public void setServerTrusted(boolean serverTrusted) {
		this.serverTrusted = serverTrusted;
	}

	public boolean isServerTrusted() {
		return serverTrusted;
	}

	public String getVersion() {
		return version;
	}

	public String getUserName() {
		return userName;
	}

	public String getPublicHash() {
		return publicHash;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public UUID getCurrentChannelUuid() {
		return currentChannelUuid;
	}

	public void setCurrentChannelUuid(UUID currentChannel) {
		this.currentChannelUuid = currentChannel;
	}

	public Channel getCurrentChannel(S_ServerData data) {
		return data.getChannel(currentChannelUuid);
	}

	public C_RemoteUserData getRemoteUserData(TalkingServerClient sclient) {
		return new C_RemoteUserData(serverTrusted, sclient.getUUID(), publicHash, publicKey);
	}

	public void verifyServerTrusted(S_ServerData serverData) {
		this.serverTrusted = serverData.isTrustedPublicKey(publicKey);
	}

}
