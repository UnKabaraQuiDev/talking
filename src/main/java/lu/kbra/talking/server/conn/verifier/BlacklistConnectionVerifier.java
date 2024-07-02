package lu.kbra.talking.server.conn.verifier;

import java.io.IOException;
import java.net.InetSocketAddress;

import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;

import lu.kbra.talking.data.db.JsonDbKvFile;
import lu.kbra.talking.server.client.TalkingServerClient;

public class BlacklistConnectionVerifier implements ConnectionVerifier {

	private JsonDbKvFile file;

	public BlacklistConnectionVerifier(String file) {
		this.file = new JsonDbKvFile(file);
	}

	@Override
	public Pair<Boolean, String> verify(TalkingServerClient tsclient) {
		try {
			if (file.contains(((InetSocketAddress) tsclient.getSocketChannel().getRemoteAddress()).getHostString())) {
				return Pairs.readOnly(false, "Blacklisted");
			} else {
				return Pairs.readOnly(true, null);
			}
		} catch (IOException e) {
			return Pairs.readOnly(false, e.getMessage());
		}
	}

	public JsonDbKvFile getFile() {
		return file;
	}

}
