package ubiquigame.platform.session.tournament.gamemode;

import java.util.ArrayList;
import java.util.List;
import ubiquigame.common.UbiquiGame;
import ubiquigame.platform.GameManager;
import ubiquigame.platform.PlatformImpl;

public class RandomGameMode implements GameModeStrategy {

	private GameManager gameManager;
	private UbiquiGame currentGame = null;
	private int currentRound = 0;
	private int maxRounds = 2;

	private UbiquiGame nextGame = null;
	private boolean gameMultipleTimesInARow;

	public RandomGameMode() {
		gameManager = PlatformImpl.getInstance().getGameManager();
	}

	@Override
	public UbiquiGame getNextGame() {
		if (!hasNextGame())
			return null;
		currentGame = nextGame;
		currentRound++;
		return currentGame;
	}

	@Override
	public boolean hasNextGame() {
		return currentRound < maxRounds;
	}

	public List<UbiquiGame> getAllowedGames() {
		ArrayList<UbiquiGame> allowed = new ArrayList<>(gameManager.getGames());
		if (!gameMultipleTimesInARow && currentGame != null) {
			String currentGameTitle = currentGame.getGameInfo().getGameTitle();
			allowed.removeIf(game -> game.getGameInfo().getGameTitle().equals(currentGameTitle));
		}

		return allowed;
	}

	@Override
	public UbiquiGame getCurrentGame() {
		return currentGame;
	}

	public void setMaxRounds(int maxRounds) {
		this.maxRounds = maxRounds;
	}

	@Override
	public int getTotalRounds() {
		return maxRounds;
	}

	@Override
	public int getRound() {
		return currentRound;
	}

	public void setNextGame(UbiquiGame ubiquiGame) {
		nextGame = ubiquiGame;
	}

	public void setGameMultipleTimesInARow(boolean checked) {
		gameMultipleTimesInARow = checked;

	}
}
