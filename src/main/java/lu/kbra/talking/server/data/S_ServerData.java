package lu.kbra.talking.server.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lu.kbra.talking.client.data.C_ServerData;
import lu.kbra.talking.data.Channel;

public class S_ServerData {

	private UUID defaultChannelUuid;

	private Map<String, Channel> channels;

	public S_ServerData(List<Channel> channels) {
		this.channels = channels.stream().collect(Collectors.toMap(Channel::getName, a -> a));
		this.defaultChannelUuid = channels.get(0).getUuid();
	}

	public S_ServerData(String... strings) {
		this.channels = new HashMap<String, Channel>();
		for (String s : strings) {
			this.channels.put(s, new Channel(s, UUID.randomUUID()));
		}
		this.defaultChannelUuid = channels.values().iterator().next().getUuid();
	}

	public Map<String, Channel> getChannels() {
		return channels;
	}

	public UUID getDefaultChannelUuid() {
		return defaultChannelUuid;
	}

	public void setDefaultChannelUuid(UUID defaultChannelUuid) {
		this.defaultChannelUuid = defaultChannelUuid;
	}

	public Channel getDefaultChannel() {
		return channels.values().stream().filter(c -> c.getUuid().equals(defaultChannelUuid)).findFirst().get();
	}

	public void setDefaultChannel(Channel obj) {
		this.defaultChannelUuid = this.channels.values().stream().filter(c -> c.equals(obj)).findFirst().get().getUuid();
	}

	public C_ServerData getClientView(S_UserData obj) {
		return new C_ServerData(channels.values().stream().collect(Collectors.toList()), defaultChannelUuid);
	}

	public Channel getChannel(UUID obj) {
		return channels.values().stream().filter(c -> c.getUuid().equals(obj)).findFirst().get();
	}

}
