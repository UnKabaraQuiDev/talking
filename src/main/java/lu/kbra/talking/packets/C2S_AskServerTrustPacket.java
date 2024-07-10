package lu.kbra.talking.packets;

import java.util.UUID;

import lu.pcy113.p4j.socket.client.P4JClient;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.packets.impl.C2S_Talking_Packet;
import lu.kbra.talking.server.TalkingServer;
import lu.kbra.talking.server.client.TalkingServerClient;
import lu.kbra.talking.server.data.S_UserData;

public class C2S_AskServerTrustPacket implements C2S_Talking_Packet<UUID> {

	private UUID uuid;

	public C2S_AskServerTrustPacket() {
	}

	private C2S_AskServerTrustPacket(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public UUID clientWrite(P4JClient client) {
		return uuid;
	}

	@Override
	public void serverRead(TalkingServerClient sclient, UUID obj) {
		if (obj == null) {
			obj = sclient.getUUID();
		}

		TalkingServerClient targetClient = (TalkingServerClient) TalkingServer.INSTANCE.getServer().getClientManager().get(obj);
		if (targetClient == null || targetClient.getUserData().isServerTrusted()) {
			return;
		}

		final S_UserData data = targetClient.getUserData();

		if (!TalkingServer.INSTANCE.getServerData().isTrustedPublicKey(data.getPublicKey())) {
			TalkingServer.INSTANCE.getServerData().addTrustedPublicKey(data.getPublicKey());

			data.verifyServerTrusted(TalkingServer.INSTANCE.getServerData());

			TalkingServer.INSTANCE.getServer().broadcastIf(S2C_UpdateRemoteUserDataListPacket.add(data.getRemoteUserData(targetClient)), (c) -> !c.equals(targetClient));
			GlobalLogger.info("Added user: " + data.getUserName() + " (" + data.getPublicHash() + ") to trusted users");
		}
	}

	public static C2S_AskServerTrustPacket ask(UUID uuid) {
		return new C2S_AskServerTrustPacket(uuid);
	}

}
