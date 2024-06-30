package lu.pcy113.talking.consts;

import lu.pcy113.jbcodec.CodecManager;
import lu.pcy113.talking.codec.HandShakeDataDecoder;
import lu.pcy113.talking.codec.HandShakeDataEncoder;
import lu.pcy113.talking.codec.ServerDataViewDecoder;
import lu.pcy113.talking.codec.ServerDataViewEncoder;
import lu.pcy113.talking.codec.UserDataDecoder;
import lu.pcy113.talking.codec.UserDataEncoder;

public class Codecs {

	public static CodecManager instance() {
		CodecManager codec = CodecManager.base();
		
		codec.register(new UserDataEncoder(), new UserDataDecoder(), (short) 20);
		codec.register(new ServerDataViewEncoder(), new ServerDataViewDecoder(), (short) 21);
		codec.register(new HandShakeDataEncoder(), new HandShakeDataDecoder(), (short) 22);
		
		return codec;
	}

}
