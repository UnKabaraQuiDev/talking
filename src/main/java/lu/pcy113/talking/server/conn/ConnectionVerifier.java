package lu.pcy113.talking.server.conn;

import lu.pcy113.talking.server.client.TalkingServerClient;

public interface ConnectionVerifier {
	
	boolean verify(TalkingServerClient tsclient);
	
}
