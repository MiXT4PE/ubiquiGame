package ubiquigame.platform.session.tournament.gamemode;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import ubiquigame.common.UbiquiGame;
import ubiquigame.platform.GameManager;
import ubiquigame.platform.PlatformImpl;

public class AllGamesGameMode implements GameModeStrategy {

	private Queue<UbiquiGame> games = new LinkedBlockingQueue<>();
	private UbiquiGame currentGame = null;

	private GameManager gameManager;
	private int totalRounds;
	private int currentRound;
	public AllGamesGameMode() {
		gameManager = PlatformImpl.getInstance().getGameManager();
		games.addAll(gameManager.getGames());
		totalRounds = games.size();
		currentRound = 0;
	}

	/**
	 * Consumes current game and sets next game as current
	 */
	@Override
	public UbiquiGame getNextGame() {
		if(!hasNextGame())
			return null;
		
		currentGame = gameManager.createNewInstance(games.poll());
		currentRound++;
		return currentGame;
	}

	@Override
	public boolean hasNextGame() {
		return games.size() > 0;
	}

	@Override
	public UbiquiGame getCurrentGame() {
		return currentGame;
	}

	@Override
	public int getTotalRounds() {
		return totalRounds;
	}

	@Override
	public int getRound() {
		return currentRound;
	}

}
