package lu.pcy113.talking.server.conn;

import java.util.ArrayList;
import java.util.List;

import lu.pcy113.jb.utils.Pair;
import lu.pcy113.talking.server.client.TalkingServerClient;
import lu.pcy113.talking.server.conn.verifier.BlacklistConnectionVerifier;

public class ConnectionManager {
	
	public ConnectionManager(boolean blacklist, String file) {
		connectionVerifier.add(new Pair<>(new BlacklistConnectionVerifier(file), blacklist));
	}
	
	public List<Pair<ConnectionVerifier, Boolean>> connectionVerifier = new ArrayList<>();
	public boolean verify(TalkingServerClient tsclient) {
		return connectionVerifier.stream().allMatch(v -> v.getValue() ? v.getKey().verify(tsclient) : true);
	}
	public void add(TalkingServerClient tsclient) {
		// TODO Auto-generated method stub
		
	}
	
}
