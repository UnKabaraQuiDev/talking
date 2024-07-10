package lu.kbra.talking.server.client;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lu.pcy113.p4j.socket.server.ClientManager;
import lu.pcy113.p4j.socket.server.ServerClient;
import lu.pcy113.p4j.socket.server.ServerClientStatus;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.talking.server.TalkingServer;

public class TalkingClientManager extends ClientManager {

	private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private static int handshakeTimeoutDelay = 3000;

	public TalkingClientManager(TalkingServer server) {
		super(server.getServer(), (socket) -> new TalkingServerClient(socket, server.getServer(), server));
	}

	@Override
	public void registerClient(ServerClient sclient) {
		super.registerClient(sclient);

		ScheduledFuture<?> scheduledFuture = scheduler.schedule(() -> {
			if (((TalkingServerClient) sclient).getServerClientStatus().equals(ServerClientStatus.LISTENING) && !((TalkingServerClient) sclient).hasUserData()) {
				sclient.disconnect();
				GlobalLogger.log("Disconnected user: " + sclient.getUUID() + ": handshake timeout");
			}
		}, handshakeTimeoutDelay, TimeUnit.MILLISECONDS);
	}

}
