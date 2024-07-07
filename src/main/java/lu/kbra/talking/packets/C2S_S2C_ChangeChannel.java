package lu.kbra.talking.packets;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.datastructure.triplet.Triplet;
import lu.pcy113.pclib.datastructure.triplet.Triplets;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.client.data.C_RemoteUserData;
import lu.kbra.talking.data.Channel;
import lu.kbra.talking.packets.impl.C2S_Talking_Packet;
import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.TalkingServer;
import lu.kbra.talking.server.client.TalkingServerClient;

public class C2S_S2C_ChangeChannel implements C2S_Talking_Packet<UUID>, S2C_Talking_Packet<Triplet<Boolean, Object, List<C_RemoteUserData>>> {

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
		Channel channel = TalkingServer.INSTANCE.getServerData().getChannel(obj);
		if (channel.hasAccess(sclient.getUserData())) {
			sclient.getUserData().setCurrentChannelUuid(obj);
			sclient.write(C2S_S2C_ChangeChannel.accepted(obj, TalkingServer.INSTANCE.getServer().getConnectedClients().stream().filter((c) -> ((TalkingServerClient) c).getUserData().getCurrentChannelUuid().equals(obj))
					.filter((c) -> !c.equals(sclient)).map((c) -> ((TalkingServerClient) c).getUserData().getRemoteUserData((TalkingServerClient) c)).collect(Collectors.toList())));
		} else {
			sclient.write(C2S_S2C_ChangeChannel.denied("Access denied."));
		}
	}

	public static C2S_S2C_ChangeChannel to(UUID target) {
		return new C2S_S2C_ChangeChannel(target);
	}

	// S2C side - - - - - -

	private Triplet<Boolean, Object, List<C_RemoteUserData>> response;

	private C2S_S2C_ChangeChannel(boolean success, Object obj, List<C_RemoteUserData> memberList) {
		this.response = Triplets.readOnly(success, obj, memberList);
	}

	public static C2S_S2C_ChangeChannel accepted(UUID target, List<C_RemoteUserData> memberList) {
		return new C2S_S2C_ChangeChannel(true, target, memberList);
	}

	public static C2S_S2C_ChangeChannel denied(Object reason) {
		return new C2S_S2C_ChangeChannel(false, reason, null);
	}

	@Override
	public void clientRead(P4JClient client, Triplet<Boolean, Object, List<C_RemoteUserData>> obj) {
		if (obj.getFirst()) {
			TalkingClient.INSTANCE.getServerData().setCurrentChannelUuid((UUID) obj.getSecond());
			TalkingClient.INSTANCE.getServerData().setRemoteUsers(obj.getThird());
			System.out.println("Changed channel to: " + TalkingClient.INSTANCE.getServerData().getCurrentChannel().getName() + ", total users: " + obj.getThird().size());
			TalkingClient.INSTANCE.getConsoleClient().print();
		} else {
			System.out.println("Couldn't change channel: " + obj.getSecond());
		}
	}

	@Override
	public Triplet<Boolean, Object, List<C_RemoteUserData>> serverWrite(TalkingServerClient client) {
		return response;
	}

}
