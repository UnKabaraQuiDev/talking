package lu.pcy113.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jb.codec.encoder.DefaultObjectEncoder;
import lu.pcy113.talking.packets.C2S_HandshakePacket.HandShakeData;

/**
 * BB1 x- String VERSION<br>
 * BB2 x- UserData USER_DATA<br>
 */
public class HandShakeDataEncoder extends DefaultObjectEncoder<HandShakeData> {

	public HandShakeDataEncoder() {
		super(HandShakeData.class);
	}

	@Override
	public ByteBuffer encode(boolean head, HandShakeData obj) {
		byte[] bb1 = cm.encode(false, obj.version).array();
		byte[] bb2 = cm.encode(false, obj.userData).array();
		return (ByteBuffer) ByteBuffer.allocateDirect(bb1.length+bb2.length).put(bb1).put(bb2).flip();
	}

}
