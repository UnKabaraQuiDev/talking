package lu.pcy113.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jb.codec.encoder.DefaultObjectEncoder;
import lu.pcy113.talking.server.data.ServerDataView;

public class ServerDataViewEncoder extends DefaultObjectEncoder<ServerDataView> {

	public ServerDataViewEncoder() {
		super(ServerDataView.class);
	}

	@Override
	public ByteBuffer encode(boolean head, ServerDataView obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
