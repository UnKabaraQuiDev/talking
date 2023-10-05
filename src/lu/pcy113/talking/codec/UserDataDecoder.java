package lu.pcy113.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jb.codec.decoder.DefaultObjectDecoder;
import lu.pcy113.talking.data.UserData;

public class UserDataDecoder extends DefaultObjectDecoder<UserData> {

	public UserDataDecoder() {
		super(UserData.class);
	}

	@Override
	public UserData decode(boolean head, ByteBuffer bb) {
		return null;
	}
	
}
