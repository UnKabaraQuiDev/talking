package lu.kbra.talking.packets;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.client.data.C_RemoteUserData;
import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.client.TalkingServerClient;

/**
 * Action: 0: remove 1: add 2: replace
 */
public class S2C_UpdateRemoteUserDataListPacket implements S2C_Talking_Packet<Pair<Byte, List<C_RemoteUserData>>> {

	public S2C_UpdateRemoteUserDataListPacket() {
	}

	private Pair<Byte, List<C_RemoteUserData>> data;

	private S2C_UpdateRemoteUserDataListPacket(Pair<Byte, List<C_RemoteUserData>> data) {
		this.data = data;
	}

	public static S2C_UpdateRemoteUserDataListPacket replace(List<C_RemoteUserData> data) {
		return new S2C_UpdateRemoteUserDataListPacket(Pairs.readOnly((byte) 2, data));
	}

	public static S2C_UpdateRemoteUserDataListPacket add(C_RemoteUserData data) {
		return new S2C_UpdateRemoteUserDataListPacket(Pairs.readOnly((byte) 1, PCUtils.asArrayList(data)));
	}

	public static S2C_UpdateRemoteUserDataListPacket remove(C_RemoteUserData data) {
		return new S2C_UpdateRemoteUserDataListPacket(Pairs.readOnly((byte) 0, PCUtils.asArrayList(data)));
	}

	@Override
	public void clientRead(P4JClient client, Pair<Byte, List<C_RemoteUserData>> obj) {
		final byte code = obj.getKey();
		final List<C_RemoteUserData> list = obj.getValue();

		if ((code == 0 || code == 1) && list.isEmpty()) {
			return;
		}

		if (code == 0 || code == 1) { // delete (also for add)
			final Set<UUID> targetUuids = list.stream().map(C_RemoteUserData::getUUID).collect(Collectors.toSet());
			TalkingClient.INSTANCE.getServerData().getRemoteUsers().removeIf((c) -> targetUuids.contains(c.getUUID()));
			System.out.println("Removed: " + list.size() + " remote users");
		}
		if (code == 1) { // add
			TalkingClient.INSTANCE.getServerData().getRemoteUsers().addAll(list);
			System.out.println("Added: " + list.size() + " remote users");
		} else if (code == 2) { // replace
			TalkingClient.INSTANCE.getServerData().setRemoteUsers(list);
			System.out.println("Replaced remote users for: " + list.size() + " new ones");
		}

		TalkingClient.INSTANCE.getFrame().getStatisticsPanel().set("channel.users.count", TalkingClient.INSTANCE.getServerData().getRemoteUsers().size());
	}

	@Override
	public Pair<Byte, List<C_RemoteUserData>> serverWrite(TalkingServerClient client) {
		return data;
	}

}
