package lu.kbra.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;

import lu.kbra.talking.packets.C2S_HandshakePacket.HandShakeData;

/**
 * BB1 x- String VERSION<br>
 * BB2 x- UserData USER_DATA<br>
 */
public class HandShakeDataEncoder extends DefaultObjectEncoder<HandShakeData> {

	@Override
	public ByteBuffer encode(boolean head, HandShakeData obj) {
		final int length = estimateSize(head, obj);
		final ByteBuffer bb = ByteBuffer.allocate(length);

		putHeader(head, bb);

		bb.put(cm.encode(false, obj.userData));
		bb.flip();

		return bb;
	}

	@Override
	public int estimateSize(boolean head, HandShakeData obj) {
		return (head ? CodecManager.HEAD_SIZE : 0) + cm.estimateSize(false, obj.userData);
	}

}
