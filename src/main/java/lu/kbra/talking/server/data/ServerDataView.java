package lu.kbra.talking.server.data;

import java.util.HashMap;
import java.util.UUID;

public class ServerDataView {

	private HashMap<UUID, Channel> channels;

	public ServerDataView(HashMap<UUID, Channel> channels) {
		super();
		this.channels = channels;
	}
	
	public HashMap<UUID, Channel> getChannels() {
		return channels;
	}

}
