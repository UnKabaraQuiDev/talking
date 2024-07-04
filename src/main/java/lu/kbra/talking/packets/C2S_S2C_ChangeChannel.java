package lu.kbra.talking.packets;

import java.util.UUID;

import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.packets.impl.C2S_Talking_Packet;
import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.TalkingServer;
import lu.kbra.talking.server.client.TalkingServerClient;

public class C2S_S2C_ChangeChannel implements C2S_Talking_Packet<UUID>, S2C_Talking_Packet<Pair<Boolean, Object>> {

	public C2S_S2C_ChangeChannel() {
	}

	// C2S side - - - - - -

	private UUID target;

	private C2S_S2C_ChangeChannel(UUID target) {
		this.target = target;
	}

	@Override
	public UUID clientWrite(P4JClient client) {
		return target;
	}

	@Override
	public void serverRead(TalkingServerClient sclient, UUID obj) {
		if (TalkingServer.INSTANCE.getServerData().getChannel(obj).hasAccess(sclient.getUserData())) {
			sclient.getUserData().setCurrentChannelUuid(obj);
			sclient.write(C2S_S2C_ChangeChannel.accepted(obj));
		} else {
			sclient.write(C2S_S2C_ChangeChannel.denied("Access denied."));
		}
	}

	public static C2S_S2C_ChangeChannel to(UUID target) {
		return new C2S_S2C_ChangeChannel(target);
	}

	// S2C side - - - - - -

	private Pair<Boolean, Object> response;

	private C2S_S2C_ChangeChannel(boolean success, Object obj) {
		this.response = Pairs.readOnly(success, obj);
	}

	public static C2S_S2C_ChangeChannel accepted(UUID target) {
		return new C2S_S2C_ChangeChannel(true, target);
	}

	public static C2S_S2C_ChangeChannel denied(Object reason) {
		return new C2S_S2C_ChangeChannel(false, reason);
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
