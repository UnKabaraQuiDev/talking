package lu.kbra.talking.packets;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.data.Channel;
import lu.kbra.talking.packets.impl.C2S_Talking_Packet;
import lu.kbra.talking.packets.impl.S2C_Talking_Packet;
import lu.kbra.talking.server.TalkingServer;
import lu.kbra.talking.server.client.TalkingServerClient;
import lu.pcy113.p4j.packets.s2c.S2CPacket;
import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.datastructure.pair.Pair;
import lu.pcy113.pclib.datastructure.pair.Pairs;

public class C2S_S2C_ChangeChannelPacket implements C2S_Talking_Packet<UUID>, S2C_Talking_Packet<Pair<Boolean, Object>> {

	public C2S_S2C_ChangeChannelPacket() {
	}

	// C2S side - - - - - -

	private UUID target;

	private C2S_S2C_ChangeChannelPacket(UUID target) {
		this.target = target;
	}

	@Override
	public UUID clientWrite(P4JClient client) {
		return target;
	}

	@Override
	public void serverRead(TalkingServerClient sclient, UUID targetChannelUuid) {
		final Channel channel = TalkingServer.INSTANCE.getServerData().getChannel(targetChannelUuid);
		final boolean firstTransfer = sclient.getUserData().getCurrentChannelUuid() == null;

		if (channel.hasAccess(sclient.getUserData())) {
			sclient.write(C2S_S2C_ChangeChannelPacket.accepted(targetChannelUuid));
			sclient.write(S2C_UpdateRemoteUserDataListPacket
					.replace(TalkingServer.INSTANCE.getServer().getConnectedClients().stream().filter((c) -> Objects.equals(((TalkingServerClient) c).getUserData().getCurrentChannelUuid(), targetChannelUuid))
							.map((c) -> ((TalkingServerClient) c).getUserData().getRemoteUserData((TalkingServerClient) c)).collect(Collectors.toList())));

			// signal channel left
			if (!firstTransfer) { // transfer to default channel
				TalkingServer.INSTANCE.getServer().broadcastIf(
						PCUtils.asArrayList(S2C_ChannelLeavePacket.switch_(sclient.getUserData().getUserName(), channel.getName()), S2C_UpdateRemoteUserDataListPacket.remove(sclient.getUserData().getRemoteUserData(sclient))),
						(c) -> ((TalkingServerClient) c).getUserData().getCurrentChannelUuid().equals(sclient.getUserData().getCurrentChannelUuid()));
			}

			// signal channel joined
			List<S2CPacket<?>> joinPackets = PCUtils.asArrayList(S2C_UpdateRemoteUserDataListPacket.add(sclient.getUserData().getRemoteUserData(sclient)));
			if (firstTransfer) {
				joinPackets.add(S2C_ChannelJoinPacket.join(sclient.getUserData().getUserName(), null));
			} else { // transfer to default channel
				joinPackets.add(S2C_ChannelJoinPacket.switch_(sclient.getUserData().getUserName(), sclient.getUserData().getCurrentChannel(TalkingServer.INSTANCE.getServerData()).getName()));
			}
			TalkingServer.INSTANCE.getServer().broadcastIf(joinPackets, (c) -> !c.equals(sclient) && ((TalkingServerClient) c).getUserData().getCurrentChannelUuid().equals(targetChannelUuid));

			sclient.write(S2C_UpdateRemoteUserDataListPacket.add(sclient.getUserData().getRemoteUserData(sclient)));

			sclient.getUserData().setCurrentChannelUuid(targetChannelUuid);
		} else {
			sclient.write(C2S_S2C_ChangeChannelPacket.denied("Access denied."));
		}
	}

	public static C2S_S2C_ChangeChannelPacket to(UUID target) {
		return new C2S_S2C_ChangeChannelPacket(target);
	}

	// S2C side - - - - - -

	private Pair<Boolean, Object> response;

	private C2S_S2C_ChangeChannelPacket(boolean success, Object obj) {
		this.response = Pairs.readOnly(success, obj);
	}

	public static C2S_S2C_ChangeChannelPacket accepted(UUID target) {
		return new C2S_S2C_ChangeChannelPacket(true, target);
	}

	public static C2S_S2C_ChangeChannelPacket denied(Object reason) {
		return new C2S_S2C_ChangeChannelPacket(false, reason);
	}

	@Override
	public void clientRead(P4JClient client, Pair<Boolean, Object> obj) {
		if (obj.getKey()) {
			TalkingClient.INSTANCE.getServerData().setCurrentChannelUuid((UUID) obj.getValue());
			System.out.println("Changed channel to: " + TalkingClient.INSTANCE.getServerData().getCurrentChannel().getName());
			TalkingClient.INSTANCE.getFrame().getMessagesPanel().clearMessages();
		} else {
			System.out.println("Couldn't change channel: " + obj.getValue());
		}
	}

	@Override
	public Pair<Boolean, Object> serverWrite(TalkingServerClient client) {
		return response;
	}

}
