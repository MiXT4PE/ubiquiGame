package ubiquigame.platform.session.tournament;

import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGame;
import ubiquigame.platform.session.GameSession;
import ubiquigame.platform.session.tournament.gamemode.Gamemode;
import ubiquigame.platform.session.tournament.gamemode.RandomGameMode;
import ubiquigame.platform.session.tournament.gamemode.AllGamesGameMode;
import ubiquigame.platform.session.tournament.gamemode.CustomGameMode;
import ubiquigame.platform.session.tournament.gamemode.GameModeStrategy;

/**
 * Session describing a tournament being played
 *
 */
public class TournamentSession extends GameSession {
	private GameModeStrategy gameModeStrategy;
	private Gamemode gamemode;
	private Score totalScore;
	private Score lastGameScore;

	public TournamentSession() {
		setGameMode(Gamemode.ALLGAMES);
	}

	public static TournamentSession getCurrent() {
		if (current.isTournament())
			return (TournamentSession) current;
		else
			return null;
	}

	public void setGameMode(Gamemode gamemode) {
		this.gamemode = gamemode;
		switch (gamemode) {
		case RANDOM:
			this.gameModeStrategy = new RandomGameMode();
			break;
		case ALLGAMES:
			this.gameModeStrategy = new AllGamesGameMode();
			break;
		case CUSTOM:
			this.gameModeStrategy = new CustomGameMode();
			break;
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getGameModeStrategy(Class<T> clazz) {
		return (T) gameModeStrategy;
	}

	public Gamemode getGamemode() {
		return gamemode;
	}

	public boolean isTournament() {
		return true;
	}

	public UbiquiGame getNextGame() {
		return gameModeStrategy.getNextGame();
	}

	public boolean hasNextGame() {
		return gameModeStrategy.hasNextGame();
	}

	@Override
	public UbiquiGame getCurrentGame() {
		return gameModeStrategy.getCurrentGame();
	}

	public void setLastGameScore(Score score) {
		this.lastGameScore = score;
		int maxScore = 300;
		for (Player player : lastGameScore.getPlayersRanked()) {
			totalScore.addTo(player, maxScore);
			maxScore -= 100;
		}
	}

	public Score getTotalScore() {
		return totalScore;
	}

	public void resetTotalScore() {
		totalScore = new Score(this.connectionSession.getPlayers());
		lastGameScore = new Score(this.connectionSession.getPlayers());
	}

	public int getTotalRounds() {
		return gameModeStrategy.getTotalRounds();
	}

	public int getCurrentRound() {
		return gameModeStrategy.getRound();
	}
}
