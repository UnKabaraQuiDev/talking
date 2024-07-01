package lu.kbra.talking.codec;

import java.nio.ByteBuffer;
import java.security.interfaces.RSAPublicKey;

import lu.pcy113.jbcodec.encoder.DefaultObjectEncoder;

public class RSAPublicKeyEncoder extends DefaultObjectEncoder<RSAPublicKey> {

	@Override
	public ByteBuffer encode(boolean head, RSAPublicKey obj) {
		final int size = estimateSize(head, obj);
		final ByteBuffer bb = ByteBuffer.allocate(size);

		super.putHeader(head, bb);

		bb.putInt(obj.getEncoded().length);
		bb.put(obj.getEncoded());

		bb.flip();

		return bb;
	}

	@Override
	public int estimateSize(boolean head, RSAPublicKey obj) {
		return super.estimateHeaderSize(head) + Integer.BYTES + obj.getEncoded().length;
	}

	@Override
	public boolean confirmClassType(Class<?> clazz) {
		return RSAPublicKey.class.isAssignableFrom(clazz);
	}

	@Override
	public boolean confirmType(Object obj) {
		return obj instanceof RSAPublicKey;
	}

}
