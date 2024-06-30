package lu.kbra.talking.consts;

import lu.pcy113.jbcodec.CodecManager;

import lu.kbra.talking.codec.HandShakeDataDecoder;
import lu.kbra.talking.codec.HandShakeDataEncoder;
import lu.kbra.talking.codec.ServerDataViewDecoder;
import lu.kbra.talking.codec.ServerDataViewEncoder;
import lu.kbra.talking.codec.UserDataDecoder;
import lu.kbra.talking.codec.UserDataEncoder;

public class Codecs {

	public static CodecManager instance() {
		CodecManager codec = CodecManager.base();
		
		codec.register(new UserDataEncoder(), new UserDataDecoder(), (short) 20);
		codec.register(new ServerDataViewEncoder(), new ServerDataViewDecoder(), (short) 21);
		codec.register(new HandShakeDataEncoder(), new HandShakeDataDecoder(), (short) 22);
		
		return codec;
	}

}
