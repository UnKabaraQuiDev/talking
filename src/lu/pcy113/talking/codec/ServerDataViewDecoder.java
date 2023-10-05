package lu.pcy113.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jb.codec.decoder.DefaultObjectDecoder;
import lu.pcy113.talking.server.data.ServerDataView;

public class ServerDataViewDecoder extends DefaultObjectDecoder<ServerDataView> {

	public ServerDataViewDecoder() {
		super(ServerDataView.class);
	}

	@Override
	public ServerDataView decode(boolean head, ByteBuffer bb) {
		// TODO Auto-generated method stub
		return null;
	}

}
