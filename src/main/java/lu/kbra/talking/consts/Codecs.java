package lu.kbra.talking.consts;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.jbcodec.decoder.PairDecoder;
import lu.pcy113.jbcodec.decoder.SingleArrayListDecoder;
import lu.pcy113.jbcodec.decoder.SingleHashMapDecoder;
import lu.pcy113.jbcodec.decoder.TripletDecoder;
import lu.pcy113.jbcodec.decoder.UUIDDecoder;
import lu.pcy113.jbcodec.encoder.PairEncoder;
import lu.pcy113.jbcodec.encoder.SingleArrayListEncoder;
import lu.pcy113.jbcodec.encoder.SingleHashMapEncoder;
import lu.pcy113.jbcodec.encoder.TripletEncoder;
import lu.pcy113.jbcodec.encoder.UUIDEncoder;

import lu.kbra.talking.codec.C_RemoteUserDataDecoder;
import lu.kbra.talking.codec.C_RemoteUserDataEncoder;
import lu.kbra.talking.codec.C_ServerDataDecoder;
import lu.kbra.talking.codec.C_ServerDataEncoder;
import lu.kbra.talking.codec.ChannelDecoder;
import lu.kbra.talking.codec.ChannelEncoder;
import lu.kbra.talking.codec.RSAPublicKeyDecoder;
import lu.kbra.talking.codec.RSAPublicKeyEncoder;
import lu.kbra.talking.codec.S_UserDataDecoder;
import lu.kbra.talking.codec.S_UserDataEncoder;

public class Codecs {

	public static CodecManager instance() {
		CodecManager codec = CodecManager.base();

		codec.register(new SingleHashMapEncoder(), new SingleHashMapDecoder(), (short) 11);
		codec.register(new UUIDEncoder(), new UUIDDecoder(), (short) 12);
		codec.register(new PairEncoder(), new PairDecoder(), (short) 13);
		codec.register(new TripletEncoder(), new TripletDecoder(), (short) 14);
		codec.register(new SingleArrayListEncoder(), new SingleArrayListDecoder(), (short) 15);

		codec.register(new S_UserDataEncoder(), new S_UserDataDecoder(), (short) 20);
		codec.register(new C_ServerDataEncoder(), new C_ServerDataDecoder(), (short) 21);
		codec.register(new C_RemoteUserDataEncoder(), new C_RemoteUserDataDecoder(), (short) 22);
		codec.register(new ChannelEncoder(), new ChannelDecoder(), (short) 23);
		codec.register(new RSAPublicKeyEncoder(), new RSAPublicKeyDecoder(), (short) 24);

		return codec;
	}

}
