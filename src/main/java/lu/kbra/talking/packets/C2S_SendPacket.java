package lu.kbra.talking.packets;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.server.client.TalkingServerClient;
import lu.pcy113.p4j.packets.c2s.C2SPacket;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.p4j.socket.server.ServerClient;
import lu.pcy113.pclib.logger.GlobalLogger;

public class C2S_SendPacket implements C2SPacket<String> {

	private String text;

	public C2S_SendPacket() {
	}

	public C2S_SendPacket(String text) {
		super();
		this.text = text;
	}

	@Override
	public String clientWrite(P4JClient client) {
		System.err.println("send: " + text);
		try {
			Cipher encryptCipher = Cipher.getInstance("RSA");
			encryptCipher.init(Cipher.ENCRYPT_MODE, TalkingClient.INSTANCE.getUserData().getPrivateKey()); // need to use public key of other
			System.err.println("into: " + Base64.getEncoder().encodeToString(encryptCipher.doFinal(text.getBytes())));
			return Base64.getEncoder().encodeToString(encryptCipher.doFinal(text.getBytes()));
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void serverRead(ServerClient sclient, String obj) {
		try {
			Cipher encryptCipher = Cipher.getInstance("RSA");
			encryptCipher.init(Cipher.DECRYPT_MODE, ((TalkingServerClient) sclient).getUserData().getPublicKey()); // need to use public key of other
			GlobalLogger.log(new String(encryptCipher.doFinal(Base64.getDecoder().decode(obj.getBytes()))));
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RuntimeException(e);
		}
	}

}
