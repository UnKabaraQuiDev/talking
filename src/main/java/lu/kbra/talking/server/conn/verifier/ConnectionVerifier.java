package lu.kbra.talking.server.conn.verifier;

import lu.pcy113.pclib.datastructure.pair.Pair;

import lu.kbra.talking.server.client.TalkingServerClient;

@FunctionalInterface
public interface ConnectionVerifier {
	
	/**
	 * @return False + reason if rejected
	 */
	Pair<Boolean, String> verify(TalkingServerClient tsclient);
	
}
