package lu.kbra.talking.packets;

import java.util.UUID;

import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.client.TalkingServerClient;

public class S2C_ChangeChannel implements S2C_Talking_Packet<Pair<Boolean, Object>> {

	private Pair<Boolean, Object> response;

	public S2C_ChangeChannel() {
	}

	private S2C_ChangeChannel(boolean success, Object obj) {
		this.response = Pairs.readOnly(success, obj);
	}

	public static S2C_ChangeChannel accepted(UUID target) {
		return new S2C_ChangeChannel(true, target);
	}

	public static S2C_ChangeChannel denied(Object reason) {
		return new S2C_ChangeChannel(false, reason);
	}

	@Override
	public void clientRead(P4JClient client, Pair<Boolean, Object> obj) {
		if (obj.getKey()) {
			TalkingClient.INSTANCE.getServerData().setCurrentChannelUuid((UUID) obj.getValue());
			System.out.println();
			System.out.println("Changed channel to: " + TalkingClient.INSTANCE.getServerData().getCurrentChannel().getName());
			TalkingClient.INSTANCE.getConsoleClient().print();
		} else {
			System.out.println("Couldn't change channel: " + obj.getValue());
		}
	}

	@Override
	public Pair<Boolean, Object> serverWrite(TalkingServerClient client) {
		return response;
	}

}
