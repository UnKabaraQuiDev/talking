package lu.kbra.talking.server.conn.verifier;

import java.util.Objects;

import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;

import lu.kbra.talking.server.client.TalkingServerClient;

public class VersionConnectionVerifier implements ConnectionVerifier {

	private String version;

	public VersionConnectionVerifier(String version) {
		this.version = version;
	}

	@Override
	public Pair<Boolean, String> verify(TalkingServerClient tsclient) {
		if (!Objects.equals(tsclient.getUserData().getVersion(), version)) {
			return Pairs.readOnly(false, "Version mismatch between client (" + tsclient.getUserData().getVersion() + ") and server (" + version + ")");
		} else {
			return Pairs.readOnly(true, null);
		}
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
