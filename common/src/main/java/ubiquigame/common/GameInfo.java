package ubiquigame.common;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Contains important information and resources about the game for the platform's UI  
 */
public interface GameInfo {
	
	/**
	 * @return String with game title
	 */
	String getGameTitle();

	/**
	 * Text which describes the game's core mechanics and theme
	 * @return String with description
	 */
	String getDescription();
	/**
	 * Thumbnail of the game, which is used by the platform's game library UI. 
	 * Minimal size of 256x256px recommended!
	 * @return Image object with a sqaure thumbnail
	 */
	Image getThumbnail();
	
	/**
	 * An image, which illustrates a brief tutorial of how to play and control the game
	 * Full HD (1920x1080px) size recommended!
	 * @return Image object with a Full HD tutorial
	 */
	Image getTutorialManual();
}
