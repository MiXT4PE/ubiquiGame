package ubiquigame.games.ticking_bomb;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ubiquigame.common.ControllerFace;
import ubiquigame.common.GameInfo;
import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.AbstractUbiquiGame;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.games.ticking_bomb.gameObjects.Thrower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TBMain extends AbstractUbiquiGame {
	private UbiquiGamePlatform platform;
	private static TBGameInfo gameInfo;
	public int height;
	public int width;

	// Everything can be drawn in here
	public SpriteBatch sb = null;
	public ShapeRenderer sr = null;

	// Variables should be declared here
	private TBHud tbHud;
	private TBSupplier tbSupplier;
	private TBLogic tbLogic;
	public ArrayList<Thrower> throwers;

	// GameOverMessage
	private List<Player> ranking;
	private Map<Player, Integer> score;

	public TBMain(UbiquiGamePlatform platform) {
		super(platform);
		this.platform = platform;

		// GameOverMessage vars
		this.ranking = new ArrayList<Player>();
		this.score = new HashMap<Player, Integer>();

		this.tbSupplier = new TBSupplier(this);
	}

	/*
	 * Is called once when this minigame is being created
	 */
	@Override
	public void create() {
		// This should come from the platform
		this.height = platform.getScreenDimension().getHeight();
		this.width = platform.getScreenDimension().getWidth();

		// create the drawing boards
		this.sb = new SpriteBatch();
		this.sr = new ShapeRenderer();
		tbSupplier.initialize();

		this.tbLogic = new TBLogic(tbSupplier);

		this.tbHud = this.tbSupplier.getHud();
	}

	/*
	 * Is called to update the status of the objects in the game
	 * 
	 * @param delta difference in time to the last update call
	 */
	@Override
	public void update(float delta) {
		// Default update call delegate to GameStateManager
		if(!tbSupplier.isGameOver()){
			tbLogic.update(delta);
			tbHud.update(delta);
		}
	}

	/*
	 * Is called when the screen is being rendered
	 * 
	 * @param delta difference in time to the last render call
	 */
	@Override
	public void render(float delta) {
		// Default render call delegate to GameStateManager
		tbLogic.render(sb, sr);

		// render for the tbHud
		tbHud.stage.act(delta);
		tbHud.stage.draw();
	}

	/*
	 * This function is being called when the game is over
	 */
	@Override
	public void gameover(GameOverMessage state) {
		platform.notifyCurrGameOver(state);
		tbLogic.dispose();
		sb.dispose();
		sr.dispose();
		tbSupplier.getTextureManager().dispose();
	}

	@Override
	public ControllerFace getControllerFace() {
		return ControllerFace.Controller_Bomb;
	}

	/*
	 * This function declares a GameInfo and returns it for the platform
	 * 
	 * @return GameInfo This class holds some informations over the game
	 */
	@Override
	public GameInfo getGameInfo() {
		gameInfo = new TBGameInfo();
		return gameInfo;
	}

	public List<Player> getRanking() {
		return ranking;
	}

	public void setRanking(List<Player> ranking) {
		this.ranking = ranking;
	}

	public Map<Player, Integer> getScore() {
		return score;
	}

	public void setScore(Map<Player, Integer> score) {
		this.score = score;
	}

	public UbiquiGamePlatform getPlatform() {
		return platform;
	}

	public SpriteBatch getSb() {
		return sb;
	}

	public ShapeRenderer getSr() {
		return sr;
	}
}
