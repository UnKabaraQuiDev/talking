package lu.kbra.talking.server.data;

import java.security.PublicKey;
import java.util.UUID;

import lu.kbra.talking.client.data.C_RemoteUserData;
import lu.kbra.talking.data.Channel;
import lu.kbra.talking.server.client.TalkingServerClient;

public class S_UserData {

	private final String userName;
	private final String hash;
	private final String version;
	private final PublicKey publicKey;

	private UUID currentChannelUuid;

	public S_UserData(String userName, String hash, String version, PublicKey publicKey2) {
		this.userName = userName;
		this.hash = hash;
		this.version = version;
		this.publicKey = publicKey2;
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
		return new C_RemoteUserData(sclient.getUUID(), publicKey);
	}

}
