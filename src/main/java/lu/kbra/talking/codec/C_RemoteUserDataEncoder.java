package lu.kbra.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;

import lu.kbra.talking.client.data.C_RemoteUserData;

public class C_RemoteUserDataEncoder extends DefaultObjectEncoder<C_RemoteUserData> {

	@Override
	public ByteBuffer encode(boolean head, C_RemoteUserData obj) {
		final ByteBuffer bb = ByteBuffer.allocate(estimateSize(head, obj));

		super.putHeader(head, bb);

		bb.put(cm.encode(false, obj.getUuid()));
		bb.put(cm.encode(true, obj.getPublicKey()));

		bb.flip();

		return bb;
	}

	@Override
	public int estimateSize(boolean head, C_RemoteUserData obj) {
		return super.estimateHeaderSize(head) + cm.estimateSize(false, obj.getUuid()) + cm.estimateSize(true, obj.getPublicKey());
	}

}
