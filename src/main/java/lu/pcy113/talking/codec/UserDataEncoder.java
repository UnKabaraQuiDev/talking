package lu.pcy113.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;
import lu.pcy113.talking.data.UserData;

/**
 * BB1 x- String USER_NAME<br>
 * BB2 x- String HASH<br>
 * BB3 x- String PRIVATE_KEY<br>
 * BB4 x- String PUBLIC_KEY<br>
 */
public class UserDataEncoder extends DefaultObjectEncoder<UserData> {

	@Override
	public ByteBuffer encode(boolean head, UserData obj) {
		byte[] bb1 = cm.encode(false, obj.getUserName()).array();
		byte[] bb2 = cm.encode(false, obj.getHash()).array();
		byte[] bb3 = cm.encode(false, obj.getPrivateKey()).array();
		byte[] bb4 = cm.encode(false, obj.getPublicKey()).array();
		return (ByteBuffer) ByteBuffer.allocateDirect(bb1.length+bb2.length+bb3.length+bb4.length).put(bb1).put(bb2).put(bb3).put(bb4).flip();
	}

}
