package lu.kbra.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;

import lu.kbra.talking.data.UserData;

/**
 * BB1 x- String USER_NAME<br>
 * BB2 x- String HASH<br>
 * BB3 x- String PRIVATE_KEY<br>
 * BB4 x- String PUBLIC_KEY<br>
 */
public class UserDataEncoder extends DefaultObjectEncoder<UserData> {

	@Override
	public ByteBuffer encode(boolean head, UserData obj) {
		final int length = estimateSize(head, obj);
		final ByteBuffer bb = ByteBuffer.allocate(length);

		putHeader(head, bb);
		
		bb.put(cm.encode(false, obj.getUserName()));
		bb.put(cm.encode(false, obj.getHash()));
		bb.put(cm.encode(false, obj.getPrivateKey()));
		bb.put(cm.encode(false, obj.getPublicKey()));
		bb.flip();

		return bb;
	}

	@Override
	public int estimateSize(boolean head, UserData obj) {
		return (head ? CodecManager.HEAD_SIZE : 0) + cm.estimateSize(false, obj.getUserName()) + cm.estimateSize(false, obj.getHash()) + cm.estimateSize(false, obj.getPrivateKey()) + cm.estimateSize(false, obj.getPublicKey());
	}

}
