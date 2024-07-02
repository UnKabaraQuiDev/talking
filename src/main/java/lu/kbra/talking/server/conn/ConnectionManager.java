package lu.kbra.talking.server.conn;

import java.util.ArrayList;
import java.util.List;

import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;

import lu.kbra.talking.server.client.TalkingServerClient;
import lu.kbra.talking.server.conn.verifier.ConnectionVerifier;

public class ConnectionManager implements ConnectionVerifier {

	public List<Pair<ConnectionVerifier, Boolean>> connectionVerifiers = new ArrayList<>();

	public Pair<Boolean, String> verify(TalkingServerClient tsclient) {
		return connectionVerifiers
				.stream()
				.map(v -> v.getValue() ? v.getKey().verify(tsclient) : null)
				.filter((c) -> !c.getKey())
				.reduce((a, b) -> Pairs.pair(false, a.getValue() + System.lineSeparator() + b.getValue()))
				.orElseGet(() -> Pairs.readOnly(true, null));
	}

	public void registerVerifier(boolean enabled, ConnectionVerifier cv) {
		connectionVerifiers.add(new Pair<>(cv, enabled));
	}

}
