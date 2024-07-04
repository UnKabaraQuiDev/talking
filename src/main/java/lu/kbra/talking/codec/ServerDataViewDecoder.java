package lu.kbra.talking.codec;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.UUID;

import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;

import lu.kbra.talking.server.data.Channel;
import lu.kbra.talking.server.data.ServerDataView;

public class ServerDataViewDecoder extends DefaultObjectDecoder<ServerDataView> {

	@Override
	public ServerDataView decode(boolean head, ByteBuffer bb) {
		super.verifyHeader(head, bb);

		@SuppressWarnings("unchecked")
		HashMap<String, Channel> channels = (HashMap<String, Channel>) cm.getDecoderByClass(HashMap.class).decode(false, bb);
		UUID default_ = (UUID) cm.decode(bb);

		return new ServerDataView(channels, default_);
	}

}
