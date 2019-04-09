package ubiquigame.platform.session.tournament.gamemode;

import ubiquigame.common.UbiquiGame;

/**
 * Base class for different game modes of tournament
 * 
 *
 */
public interface GameModeStrategy {

	UbiquiGame getNextGame();

	boolean hasNextGame();

	UbiquiGame getCurrentGame();

	int getTotalRounds();

	int getRound();

}
