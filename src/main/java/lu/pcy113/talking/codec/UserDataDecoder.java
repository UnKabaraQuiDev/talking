package lu.pcy113.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;
import lu.pcy113.talking.data.UserData;

public class UserDataDecoder extends DefaultObjectDecoder<UserData> {

	@Override
	public UserData decode(boolean head, ByteBuffer bb) {
		super.verifyHeader(head, bb);
		
		String userName = (String) cm.getDecoderByClass(String.class).decode(false, bb);
		String hash = (String) cm.getDecoderByClass(String.class).decode(false, bb);
		String privateKey = (String) cm.getDecoderByClass(String.class).decode(false, bb);
		String publicKey = (String) cm.getDecoderByClass(String.class).decode(false, bb);
		
		return new UserData(userName, hash, privateKey, publicKey);
	}
	
}
