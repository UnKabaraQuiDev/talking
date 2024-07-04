package lu.kbra.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;

import lu.kbra.talking.server.data.Channel;

public class ChannelEncoder extends DefaultObjectEncoder<Channel> {

	@Override
	public ByteBuffer encode(boolean head, Channel obj) {
		final int length = estimateSize(head, obj);
		final ByteBuffer bb = ByteBuffer.allocate(length);

		super.putHeader(head, bb);

		bb.put(cm.encode(false, obj.getName()));
		bb.put(cm.encode(false, obj.getUuid()));

		bb.flip();
		return bb;
	}

	@Override
	public int estimateSize(boolean head, Channel obj) {
		return super.estimateHeaderSize(head) + cm.estimateSize(false, obj.getName()) + cm.estimateSize(false, obj.getUuid());
	}

}
