package lu.kbra.talking.server.data;

import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.UUID;

import lu.kbra.talking.data.UserData;

public class ServerData {
	
	private HashMap<UUID, Channel> channels;
	
	public ServerDataView getView(UserData obj) {
		return null;
	}

}
