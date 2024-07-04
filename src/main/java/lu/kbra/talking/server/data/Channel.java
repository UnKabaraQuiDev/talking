package lu.kbra.talking.server.data;

import java.util.Objects;
import java.util.UUID;

import lu.kbra.talking.data.UserData;

public class Channel {

	private String name;
	private UUID uuid;

	public Channel(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
	}

	public boolean hasAccess(UserData userData) {
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return name + "[" + uuid + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof Channel))
			return false;

		return Objects.equals(((Channel) obj).uuid, uuid) && Objects.equals(((Channel) obj).name, name);
	}

}
