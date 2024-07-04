package lu.kbra.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;

import lu.kbra.talking.server.data.S_UserData;

/**
 * BB1 x- String USER_NAME<br>
 * BB2 x- String HASH<br>
 * BB3 v- String VERSION<br>
 * BB3 v- String PUBLIC_KEY<br>
 */
public class S_UserDataEncoder extends DefaultObjectEncoder<S_UserData> {

	@Override
	public ByteBuffer encode(boolean head, S_UserData obj) {
		final int length = estimateSize(head, obj);
		final ByteBuffer bb = ByteBuffer.allocate(length);

		super.putHeader(head, bb);

		bb.put(cm.encode(false, obj.getUserName()));
		bb.put(cm.encode(false, obj.getHash()));
		bb.put(cm.encode(true, obj.getVersion()));
		bb.put(cm.encode(true, obj.getPublicKey()));

		bb.flip();

		return bb;
	}

	@Override
	public int estimateSize(boolean head, S_UserData obj) {
		return super.estimateHeaderSize(head) + cm.estimateSize(false, obj.getUserName()) + cm.estimateSize(false, obj.getHash()) + cm.estimateSize(true, obj.getVersion()) + cm.estimateSize(true, obj.getPublicKey());
	}

}
