package ubiquigame.games.your_game_name;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ubiquigame.common.ControllerFace;
import ubiquigame.common.GameInfo;
import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.AbstractUbiquiGame;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.games.your_game_name.overlay.HUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YourMainClass extends AbstractUbiquiGame{
	//Everything can be drawn in here
	public SpriteBatch sb = null;
	public ShapeRenderer sr = null;

	//Variables should be declared here
	private static YourMainClass instance = null;
	private YourGameLogic logic;
	private UbiquiGamePlatform platform;
	public int height;
	public int width;
	public HUD hud;

	//Texture Manager
	public TextureManager tm;

	//GameOverMessage
	private List<Player> ranking;
	private Map<Player, Integer> score;

	public YourMainClass(UbiquiGamePlatform platform){
		super(platform);
		this.platform = platform;
		this.instance = this;

		//GameOverMessage vars
		this.ranking = new ArrayList<Player>();
		this.score = new HashMap<Player, Integer>();
	}

	/*
	 * Is called once when this minigame is being created
	 */
	@Override
	public void create() {
		//This should come from the platform
		height = platform.getScreenDimension().getHeight();
		width = platform.getScreenDimension().getWidth();

		//Texture Manager
		tm = new TextureManager();

		//create the drawing boards
		sb = new SpriteBatch();
		sr = new ShapeRenderer();

		//create the HUD -> in hud you should define the hud explicitly
		hud = new HUD(sb);

		//Create logic
        logic = new YourGameLogic();
	}

	/*
	 * Is called to update the status of the objects in the game
	 * @param delta difference in time to the last update call
	 */
	@Override
	public void update(float delta) {
		//Default update call
		logic.update(delta);
	}

	/*
	 * Is called when the screen is being rendered
	 * @param delta difference in time to the last render call
	 */
	@Override
	public void render(float delta) {
		//Default render call
		logic.render(sb, sr);

		//render for the hud
		hud.stage.act(delta);
		hud.stage.draw();
	}

	/*
	 * This function is being called when the game is over
	 */
	@Override
	public void gameover(GameOverMessage state) {
		platform.notifyCurrGameOver(state);
		sb.dispose();
		sr.dispose();
	}

	@Override
	public ControllerFace getControllerFace() {
		//should return controller but there is still a bug
		return null;
	}

	/*
	* This function declares a GameInfo and returns it for the platform
	* @return GameInfo This class holds some informations over the game
	*/
	@Override
	public GameInfo getGameInfo() {
		return new GameInfo() {
			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public Image getThumbnail() {
				return null;
			}

			@Override
			public Image getTutorialManual() {
				return null;
			}

			@Override
			public String getGameTitle() {
				return null;
			}
		};
	}

	public static YourMainClass getInstance(){
		return instance;
	}

	public List<Player> getRanking(){
		return ranking;
	}

	public void setRanking(List<Player> ranking){
		this.ranking = ranking;
	}

	public Map<Player, Integer> getScore(){
		return score;
	}

	public void setScore(Map<Player, Integer> score){
		this.score = score;
	}

	public TextureManager getTextureManager(){
		return tm;
	}

}
