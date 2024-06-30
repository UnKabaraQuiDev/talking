package lu.kbra.talking.server.conn.verifier;

import java.io.IOException;
import java.net.InetSocketAddress;

import lu.kbra.talking.data.db.JsonDbKvFile;
import lu.kbra.talking.server.client.TalkingServerClient;
import lu.kbra.talking.server.conn.ConnectionVerifier;

public class BlacklistConnectionVerifier implements ConnectionVerifier {
	
	private JsonDbKvFile file;
	
	public BlacklistConnectionVerifier(String file) {
		this.file = new JsonDbKvFile(file);
	}
	
	@Override
	public boolean verify(TalkingServerClient tsclient) {
		try {
			return file.contains(((InetSocketAddress) tsclient.getSocketChannel().getRemoteAddress()).getHostString());
		} catch (IOException e) {
			return false;
		}
	}
	
	public JsonDbKvFile getFile() {return file;}

}
