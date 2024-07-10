package lu.kbra.talking.packets;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.datastructure.triplet.Triplet;
import lu.pcy113.pclib.datastructure.triplet.Triplets;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.client.data.C_RemoteUserData;
import lu.kbra.talking.packets.impl.C2S_Talking_Packet;
import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.TalkingServer;
import lu.kbra.talking.server.client.TalkingServerClient;

/**
 * C2S: channel, target, encoded text <br>
 * S2C: user, target, encoded text
 */
public class C2S_S2C_MessagePacket implements C2S_Talking_Packet<Triplet<UUID, UUID, String>>, S2C_Talking_Packet<Triplet<String, UUID, String>> {

	public C2S_S2C_MessagePacket() {
	}

	private UUID remoteTarget;
	private PublicKey remotePublicKey;
	private String text;

	public C2S_S2C_MessagePacket(UUID remoteTarget, PublicKey remotePublicKey, String text) {
		this.remoteTarget = remoteTarget;
		this.remotePublicKey = remotePublicKey;
		this.text = text;
	}

	// C2S - - - - - -

	@Override
	public Triplet<UUID, UUID, String> clientWrite(P4JClient client) {
		try {
			Cipher encryptCipher = Cipher.getInstance("RSA");
			encryptCipher.init(Cipher.ENCRYPT_MODE, remotePublicKey);
			return Triplets.readOnly(TalkingClient.INSTANCE.getServerData().getCurrentChannelUuid(), remoteTarget, Base64.getEncoder().encodeToString(encryptCipher.doFinal(text.getBytes())));
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void serverRead(TalkingServerClient sclient, Triplet<UUID, UUID, String> obj) {
		final UUID channel = obj.getFirst();
		final UUID targetRemote = obj.getSecond();

		TalkingServer.INSTANCE.getServer().getClientManager().get(targetRemote).write(C2S_S2C_MessagePacket.s2c(sclient.getUserData().getUserName(), sclient.getUUID(), obj.getThird()));
	}

	public static C2S_S2C_MessagePacket c2s(C_RemoteUserData remote, String rawText) {
		return new C2S_S2C_MessagePacket(remote.getUUID(), remote.getPublicKey(), rawText);
	}

	// S2C - - - - - -

	private String name;
	private UUID sender;

	public C2S_S2C_MessagePacket(String name, UUID sender, String text) {
		this.text = text;
		this.sender = sender;
		this.name = name;
	}

	@Override
	public void clientRead(P4JClient client, Triplet<String, UUID, String> obj) {
		try {
			final String username = obj.getFirst();
			final String content = obj.getThird();

			Cipher encryptCipher = Cipher.getInstance("RSA");
			encryptCipher.init(Cipher.DECRYPT_MODE, TalkingClient.INSTANCE.getUserData().getPrivateKey());

			TalkingClient.INSTANCE.getFrame().getMessagesPanel().addMessage(username, obj.getSecond(), new String(encryptCipher.doFinal(Base64.getDecoder().decode(content.getBytes()))), false);
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Triplet<String, UUID, String> serverWrite(TalkingServerClient client) {
		// GlobalLogger.log("Sending: " + text + " from: " + client.getUserData().getUserName() + " to: " + client.getUUID() + "(" + client.getUserData().getUserName() + ")");
		return Triplets.readOnly(name, sender, text);
	}

	public static C2S_S2C_MessagePacket s2c(String name, UUID sender, String text) {
		return new C2S_S2C_MessagePacket(name, sender, text);
	}

}
