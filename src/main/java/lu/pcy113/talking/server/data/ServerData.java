package lu.pcy113.talking.server.data;

import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.UUID;

import lu.pcy113.talking.data.UserData;

public class ServerData {
	
	private HashMap<UUID, Channel> channels;
	
	public ServerDataView getView(UserData obj) {
		return null;
	}

}
