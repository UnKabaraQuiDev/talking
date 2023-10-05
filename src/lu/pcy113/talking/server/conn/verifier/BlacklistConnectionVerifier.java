package lu.pcy113.talking.server.conn.verifier;

import java.io.IOException;
import java.net.InetSocketAddress;

import lu.pcy113.talking.data.db.JsonDbKvFile;
import lu.pcy113.talking.server.client.TalkingServerClient;
import lu.pcy113.talking.server.conn.ConnectionVerifier;

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
