package ubiquigame.platform.session;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import ubiquigame.common.UbiquiGame;
import ubiquigame.platform.session.tournament.TournamentSession;

/**
 * Describes a game session of the platform e.g. tournament or single game
 */
public class GameSession {

	protected static GameSession current = null;

	protected Queue<UbiquiGame> gameOrder = new LinkedBlockingQueue<>();
	protected final ConnectionSession connectionSession = ConnectionSession.getCurrent();

	protected GameSession() {

	}

	/**
	 * @return UbiquiGame instance of current game being set/played
	 */
	public UbiquiGame getCurrentGame() {
		return gameOrder.peek();
	}

	public ConnectionSession getConnectionSession() {
		return connectionSession;
	}

	public void setGameOrder(UbiquiGame... gameOrder) {
		this.gameOrder.clear();
		this.gameOrder.addAll(Arrays.asList(gameOrder));
	}

	public boolean isTournament() {
		return this instanceof TournamentSession;
	}

	public static GameSession getCurrent() {
		return current;
	}

	public static SingleGameSession getSingleGameSession() {
		return (SingleGameSession) current;
	}

	public static SingleGameSession createSingleGame() {
		current = new SingleGameSession();
		return (SingleGameSession) current;
	}

	public static TournamentSession createTournament() {
		current = new TournamentSession();
		return (TournamentSession) current;
	}

}
