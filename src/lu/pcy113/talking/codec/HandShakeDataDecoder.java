package lu.pcy113.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jb.codec.decoder.DefaultObjectDecoder;
import lu.pcy113.talking.data.UserData;
import lu.pcy113.talking.packets.C2S_HandshakePacket.HandShakeData;

public class HandShakeDataDecoder extends DefaultObjectDecoder<HandShakeData> {

	public HandShakeDataDecoder() {
		super(HandShakeData.class);
	}
	
	@Override
	public HandShakeData decode(boolean head, ByteBuffer bb) {
		super.verifyHeader(head, bb);
		String version = (String) cm.getDecoderByClass(String.class).decode(false, bb);
		UserData userData = (UserData) cm.getDecoderByClass(UserData.class).decode(false, bb);
		return new HandShakeData(version, userData);
	}

}
