package lu.pcy113.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jb.codec.encoder.DefaultObjectEncoder;
import lu.pcy113.talking.data.UserData;

public class UserDataEncoder extends DefaultObjectEncoder<UserData> {

	public UserDataEncoder() {
		super(UserData.class);
	}

	@Override
	public ByteBuffer encode(boolean head, UserData obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
