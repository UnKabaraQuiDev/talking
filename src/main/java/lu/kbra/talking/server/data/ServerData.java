package lu.kbra.talking.server.data;

import java.util.HashMap;
import java.util.UUID;

import lu.kbra.talking.data.UserData;

public class ServerData {

	private HashMap<UUID, Channel> channels;

	public ServerData(HashMap<UUID, Channel> channels) {
		super();
		this.channels = channels;
	}
	
	public ServerData(String... strings) {
		super();
		this.channels = new HashMap<UUID, Channel>();
		for(String s : strings) {
			this.channels.put(UUID.randomUUID(), new Channel(s));
		}
	}

	public HashMap<UUID, Channel> getChannels() {
		return channels;
	}

	public ServerDataView getView(UserData obj) {
		return new ServerDataView(channels);
	}

}
