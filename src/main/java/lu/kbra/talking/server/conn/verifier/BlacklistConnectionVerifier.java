package lu.kbra.talking.server.conn.verifier;

import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;

import lu.kbra.talking.server.client.TalkingServerClient;

public class BlacklistConnectionVerifier implements ConnectionVerifier {

	public BlacklistConnectionVerifier(String file) {
		
	}
	
	@Override
	public Pair<Boolean, String> verify(TalkingServerClient tsclient) {
		try {
			//if (file.contains(((InetSocketAddress) tsclient.getSocketChannel().getRemoteAddress()).getHostString())) {
			if(false) {
				return Pairs.readOnly(false, "Blacklisted");
			} else {
				return Pairs.readOnly(true, null);
			}
		} catch (Exception e) {
			return Pairs.readOnly(false, e.getMessage());
		}
	}

}
