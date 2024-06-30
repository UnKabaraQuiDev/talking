package lu.kbra.talking.codec;

import java.nio.ByteBuffer;

import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;

import lu.kbra.talking.data.UserData;
import lu.kbra.talking.packets.C2S_HandshakePacket.HandShakeData;

public class HandShakeDataDecoder extends DefaultObjectDecoder<HandShakeData> {

	@Override
	public HandShakeData decode(boolean head, ByteBuffer bb) {
		super.verifyHeader(head, bb);
		String version = (String) cm.getDecoderByClass(String.class).decode(false, bb);
		UserData userData = (UserData) cm.getDecoderByClass(UserData.class).decode(false, bb);
		return new HandShakeData(version, userData);
	}

}
