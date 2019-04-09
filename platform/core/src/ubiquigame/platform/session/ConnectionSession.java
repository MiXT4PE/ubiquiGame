package ubiquigame.platform.session;

import java.net.InetAddress;
import java.util.*;

import ubiquigame.platform.session.player.ControllerInternal;
import ubiquigame.platform.session.player.PlayerInternal;

public class ConnectionSession {

	private static ConnectionSession currentSession;

	public static final int MAX_PLAYERS = 4;

	private int playersConnected = 0;
	private Map<InetAddress, ControllerInternal> ipMapping = new HashMap<>();

	protected PlayerInternal[] playerSlots = new PlayerInternal[MAX_PLAYERS];

	public void kickPlayer(PlayerInternal player) {
		for (int i = 0; i < playerSlots.length; i++) {
			if (playerSlots[i].equals(player)) {
				playerSlots[i] = null;
				playersConnected--;
				return;
			}
		}
	}

	public void addPlayer(PlayerInternal player) {
		if (playersConnected < MAX_PLAYERS) {
			for (int i = 0; i < playerSlots.length; i++) {
				if (playerSlots[i] == null) {
					playerSlots[i] = player;
					playersConnected++;
					return;
				}
			}
		}

	}

	public boolean allPlayersReady() {
		int playersReady = 0;
		for (int i = 0; i < playerSlots.length; i++) {
			if (playerSlots[i] != null && playerSlots[i].isReady()) {
				playersReady++;
			}
		}
		return playersReady == playersConnected;
	}

	public int getPlayerCount() {
		return playersConnected;
	}

	/**
	 * 
	 * @return copy of array with player references
	 */
	public PlayerInternal[] getPlayers() {
		return Arrays.copyOf(playerSlots, playersConnected);
	}

	public static ConnectionSession getCurrent() {
		return currentSession;
	}

	public PlayerInternal getPlayer(int slotId) {
		return playerSlots[slotId];
	}

	public boolean isPlayerConnected(int slotId) {
		return playerSlots[slotId] != null;
	}

	public static ConnectionSession create() {
		currentSession = new ConnectionSession();
		return currentSession;
	}

	public void kickAll() {
		for (int a = 0; a < playerSlots.length; a++) {
			playerSlots[a] = null;
			playersConnected = 0;
		}
	}

	public Map<InetAddress, ControllerInternal> getIpMapping() {
		return ipMapping;
	}
}
