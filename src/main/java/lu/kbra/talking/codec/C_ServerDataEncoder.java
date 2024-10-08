package lu.kbra.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;

import lu.kbra.talking.client.data.C_ServerData;

public class C_ServerDataEncoder extends DefaultObjectEncoder<C_ServerData> {

	@Override
	public ByteBuffer encode(boolean head, C_ServerData obj) {
		final int length = estimateSize(head, obj);
		final ByteBuffer bb = ByteBuffer.allocate(length);

		super.putHeader(head, bb);

		bb.put(cm.encode(false, obj.getChannels()));
		bb.put(cm.encode(true, obj.getCurrentChannelUuid()));

		bb.flip();

		return bb;
	}

	@Override
	public int estimateSize(boolean head, C_ServerData obj) {
		return super.estimateHeaderSize(head) + cm.estimateSize(false, obj.getChannels()) + cm.estimateSize(true, obj.getCurrentChannelUuid());
	}

}
