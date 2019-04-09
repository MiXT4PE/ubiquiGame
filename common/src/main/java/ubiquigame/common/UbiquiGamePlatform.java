package ubiquigame.common;

import com.badlogic.gdx.ApplicationListener;

import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.common.impl.ScreenDimension;

/**
 * Interface of the Platform visible to an {@link UbiquiGame}
 */
public interface UbiquiGamePlatform extends ApplicationListener {
	/**
	 * The players participating the current game
	 * 
	 * @return array of players, order is relevant!
	 */
	Player[] getPlayers();

	/**
	 * Notifies the platform an {@link UbiquiGame} has ended, with a given result
	 * 
	 * @see GameOverMessage
	 * @param message containing highscore and ranking
	 */
	void notifyCurrGameOver(GameOverMessage message);

	/**
	 * Gets the screen dimensions of the platform
	 * 
	 * @return {@link ScreenDimension}
	 */
	ScreenDimension getScreenDimension();

	/**
	 * The current volume for music
	 */
	float getMusicVolume();

	/**
	 * The current volume for sound
	 */
	float getSoundVolume();

}
