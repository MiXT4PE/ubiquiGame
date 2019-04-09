package ubiquigame.common;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Provides necessary player information to the {@link UbiquiGame}
 */
public interface Player {
	/**
	 * The players name
	 * @return the player's name
	 */
	String getName();

	/**
	 * The player's controller instance
	 * @see Controller
	 * @return the player's controller instance
	 */
	Controller getController();

	/**
	 * The player's individual avatar
	 * @return the player's avatar
	 */
	Image getAvatar();

}
