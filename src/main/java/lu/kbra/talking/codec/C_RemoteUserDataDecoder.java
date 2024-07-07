package lu.kbra.talking.codec;

import java.nio.ByteBuffer;
import java.security.PublicKey;
import java.util.UUID;

import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;

import lu.kbra.talking.client.data.C_RemoteUserData;

public class C_RemoteUserDataDecoder extends DefaultObjectDecoder<C_RemoteUserData> {

	@Override
	public C_RemoteUserData decode(boolean head, ByteBuffer bb) {
		super.verifyHeader(head, bb);
		
		UUID uuid = cm.getDecoderByClass(UUID.class).decode(false, bb);
		PublicKey pk = (PublicKey) cm.decode(bb);

		return new C_RemoteUserData(uuid, pk);
	}

}
