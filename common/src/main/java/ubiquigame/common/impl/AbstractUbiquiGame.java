package ubiquigame.common.impl;

import ubiquigame.common.UbiquiGamePlatform;

import ubiquigame.common.UbiquiGame;

public abstract class AbstractUbiquiGame implements UbiquiGame {

	private final UbiquiGamePlatform platformInstance;

	public AbstractUbiquiGame(UbiquiGamePlatform platform) {
		platformInstance = platform;
	}

	/**
	 * Get the instance of the platform, to access relevant resources for the game
	 * 
	 * @return GamePlatform the instance of the platform
	 */
	public final UbiquiGamePlatform getPlatformInstance() {
		return platformInstance;
	}

	public abstract void create();

	public abstract void update(float delta);

	@Override
	public void gameover(GameOverMessage state) {
		platformInstance.notifyCurrGameOver(state);
	}

}
