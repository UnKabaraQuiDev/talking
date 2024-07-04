package lu.kbra.talking.codec;

import java.nio.ByteBuffer;
import java.security.PublicKey;

import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;

import lu.kbra.talking.server.data.S_UserData;

public class S_UserDataDecoder extends DefaultObjectDecoder<S_UserData> {

	@Override
	public S_UserData decode(boolean head, ByteBuffer bb) {
		super.verifyHeader(head, bb);

		String userName = (String) cm.getDecoderByClass(String.class).decode(false, bb);
		String hash = (String) cm.getDecoderByClass(String.class).decode(false, bb);
		String version = (String) cm.decode(bb);
		PublicKey publicKey = (PublicKey) cm.decode(bb);

		return new S_UserData(userName, hash, version, publicKey);
	}

}
