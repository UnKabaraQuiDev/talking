package lu.kbra.talking.codec;

import java.nio.ByteBuffer;
import java.util.UUID;

import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;

import lu.kbra.talking.server.data.Channel;

public class ChannelDecoder extends DefaultObjectDecoder<Channel> {

	@Override
	public Channel decode(boolean head, ByteBuffer bb) {
		return new Channel(cm.getDecoderByClass(String.class).decode(false, bb), cm.getDecoderByClass(UUID.class).decode(false, bb));
	}

}
