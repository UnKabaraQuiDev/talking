package lu.kbra.talking.server.conn;

import lu.kbra.talking.server.client.TalkingServerClient;

@FunctionalInterface
public interface ConnectionVerifier {
	
	boolean verify(TalkingServerClient tsclient);
	
}
