package lu.kbra.talking.consts;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.decoder.HashMapDecoder;
import lu.pcy113.jbcodec.decoder.UUIDDecoder;
import lu.pcy113.jbcodec.encoder.HashMapEncoder;
import lu.pcy113.jbcodec.encoder.UUIDEncoder;

import lu.kbra.talking.codec.ChannelDecoder;
import lu.kbra.talking.codec.ChannelEncoder;
import lu.kbra.talking.codec.HandShakeDataDecoder;
import lu.kbra.talking.codec.HandShakeDataEncoder;
import lu.kbra.talking.codec.RSAPublicKeyDecoder;
import lu.kbra.talking.codec.RSAPublicKeyEncoder;
import lu.kbra.talking.codec.ServerDataViewDecoder;
import lu.kbra.talking.codec.ServerDataViewEncoder;
import lu.kbra.talking.codec.UserDataDecoder;
import lu.kbra.talking.codec.UserDataEncoder;

public class Codecs {

	public static CodecManager instance() {
		CodecManager codec = CodecManager.base();

		codec.register(new HashMapEncoder(), new HashMapDecoder(), (short) 11);
		codec.register(new UUIDEncoder(), new UUIDDecoder(), (short) 12);

		codec.register(new UserDataEncoder(), new UserDataDecoder(), (short) 20);
		codec.register(new ServerDataViewEncoder(), new ServerDataViewDecoder(), (short) 21);
		codec.register(new HandShakeDataEncoder(), new HandShakeDataDecoder(), (short) 22);
		codec.register(new ChannelEncoder(), new ChannelDecoder(), (short) 23);
		codec.register(new RSAPublicKeyEncoder(), new RSAPublicKeyDecoder(), (short) 24);

		return codec;
	}

}
