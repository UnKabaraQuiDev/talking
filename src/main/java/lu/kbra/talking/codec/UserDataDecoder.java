package lu.kbra.talking.codec;

import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.security.PublicKey;

import lu.kbra.talking.data.UserData;
import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;

public class UserDataDecoder extends DefaultObjectDecoder<UserData> {

	@Override
	public UserData decode(boolean head, ByteBuffer bb) {
		super.verifyHeader(head, bb);

		String userName = (String) cm.getDecoderByClass(String.class).decode(false, bb);
		String hash = (String) cm.getDecoderByClass(String.class).decode(false, bb);
		String version = (String) cm.decode(bb);
		PublicKey publicKey = (PublicKey) cm.decode(bb);
		PrivateKey privateKey = (PrivateKey) cm.decode(bb);

		return new UserData(userName, hash, version, publicKey, privateKey);
	}

}
