package lu.kbra.talking.client.data;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lu.kbra.talking.data.Channel;

public class C_ServerData {

	private UUID currentChannelUuid;

	private Map<String, Channel> channels;

	public C_ServerData(Map<String, Channel> channels, UUID current) {
		this.channels = channels;
		this.currentChannelUuid = current;
	}

	public C_ServerData(List<Channel> channels, UUID current) {
		this.channels = channels.stream().collect(Collectors.toMap(Channel::getName, a -> a));
		this.currentChannelUuid = current;
	}

	public Map<String, Channel> getChannels() {
		return channels;
	}

	public UUID getCurrentChannelUuid() {
		return currentChannelUuid;
	}

	public void setCurrentChannelUuid(UUID currentChannelUuid) {
		this.currentChannelUuid = currentChannelUuid;
	}

	public Channel getCurrentChannel() {
		return channels.values().stream().filter(c -> c.getUuid().equals(currentChannelUuid)).findFirst().get();
	}

}
