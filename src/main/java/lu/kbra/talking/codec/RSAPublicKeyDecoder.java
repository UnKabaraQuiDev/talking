package lu.kbra.talking.codec;

import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import lu.pcy113.jbcodec.decoder.DefaultObjectDecoder;

public class RSAPublicKeyDecoder extends DefaultObjectDecoder<RSAPublicKey> {

	@Override
	public RSAPublicKey decode(boolean head, ByteBuffer bb) {
		super.verifyHeader(head, bb);

		final int size = bb.getInt();

		byte[] content = new byte[size];
		bb.get(content);

		try {
			X509EncodedKeySpec ks = new X509EncodedKeySpec(content);
			KeyFactory kf = KeyFactory.getInstance("RSA");

			RSAPublicKey pub = (RSAPublicKey) kf.generatePublic(ks);

			return pub;
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
