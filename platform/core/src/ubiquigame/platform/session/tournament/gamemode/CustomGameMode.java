package ubiquigame.platform.session.tournament.gamemode;

import ubiquigame.common.UbiquiGame;

public class CustomGameMode implements GameModeStrategy {

	@Override
	public UbiquiGame getNextGame() {
		return null;
	}

	@Override
	public boolean hasNextGame() {
		return false;
	}

	@Override
	public UbiquiGame getCurrentGame() {
		return null;
	}

	@Override
	public int getTotalRounds() {
		return 0;
	}

	@Override
	public int getRound() {
		return 0;
	}

}
