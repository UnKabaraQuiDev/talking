package lu.kbra.talking.codec;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.UUID;

import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;

import lu.kbra.talking.client.data.C_ServerData;
import lu.kbra.talking.data.Channel;

public class C_ServerDataDecoder extends DefaultObjectDecoder<C_ServerData> {

	@Override
	public C_ServerData decode(boolean head, ByteBuffer bb) {
		super.verifyHeader(head, bb);

		@SuppressWarnings("unchecked")
		HashMap<String, Channel> channels = (HashMap<String, Channel>) cm.getDecoderByClass(HashMap.class).decode(false, bb);
		UUID default_ = (UUID) cm.decode(bb);

		return new C_ServerData(channels, default_);
	}

}
