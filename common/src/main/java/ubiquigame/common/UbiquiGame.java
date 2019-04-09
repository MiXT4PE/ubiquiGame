package ubiquigame.common;

import ubiquigame.common.impl.GameOverMessage;
/**
 * An UbiquiGame defines the interface a minigame must implement 
 * to be able to be loaded by the platform's game manager
 */
public interface UbiquiGame {

	/**
	 * Tells the platform that the game has been finished. After this call the
	 * platform stops this game. Override 
	 * 
	 * @param state GameOverMessage object with game data about participated Players
	 * @see GameOverMessage
	 */
	void gameover(GameOverMessage message);


	/**
	 * Contains setup logic for the game. Separated from game's constructor 
	 * due to the underlying mechanism an external game class is loaded to the platform's classpath 
	 */
	void create();
	
	/**
	 * Called to update the games logic, if the game is not paused by the platform
	 * 
	 * @param delta time of last frame
	 */
	void update(float delta);
	
	/**
	 * Called after {@link #update(float)}. 
	 * While the game is show the render method is called every frame, 
	 * regardless if its update logic is paused
	 * 
	 * @param delta time of last frame
	 */
	void render(float delta);

	/**
	 * Contains information and resources about the game for the platform's UI  
	 * 
	 * @return GameInfo object with metadata
	 */
	GameInfo getGameInfo();
	
	/**
	 * Identifier that tells the platform which face the connected controllers 
	 * should display while playing this game
	 */
	default ControllerFace getControllerFace() {
		return ControllerFace.Default;
	}
}
