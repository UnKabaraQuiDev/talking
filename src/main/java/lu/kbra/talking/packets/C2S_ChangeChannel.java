package lu.kbra.talking.packets;

import java.util.UUID;

import lu.pcy113.p4j.socket.client.P4JClient;

import lu.kbra.talking.packets.impl.C2S_Talking_Packet;
import lu.kbra.talking.server.TalkingServer;
import lu.kbra.talking.server.client.TalkingServerClient;

public class C2S_ChangeChannel implements C2S_Talking_Packet<UUID> {

	private UUID target;

	public C2S_ChangeChannel() {
	}

	private C2S_ChangeChannel(UUID target) {
		this.target = target;
	}

	@Override
	public UUID clientWrite(P4JClient client) {
		return target;
	}

	@Override
	public void serverRead(TalkingServerClient sclient, UUID obj) {
		if (TalkingServer.INSTANCE.getServerData().getChannel(obj).hasAccess(sclient.getUserData())) {
			sclient.getUserData().setCurrentChannel(obj);
			sclient.write(S2C_ChangeChannel.accepted(obj));
		} else {
			sclient.write(S2C_ChangeChannel.denied("Access denied."));
		}
	}

	public static C2S_ChangeChannel to(UUID target) {
		return new C2S_ChangeChannel(target);
	}

}
