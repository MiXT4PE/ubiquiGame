package ubiquigame.games.space_shooter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ubiquigame.common.ControllerFace;
import ubiquigame.common.GameInfo;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.AbstractUbiquiGame;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.games.space_shooter.states.CountDownState;
import ubiquigame.games.space_shooter.states.GameStateManager;

public class SpaceShooter extends AbstractUbiquiGame{
	// This is the manager for all the states that are being handled
	private GameStateManager gsm;

	// information about the game for the platform to display
	private static final GameInfo gameInfo = new SpaceShooterInfo();

	// Everything can be drawn in here
	private SpriteBatch sb = null;
	private ShapeRenderer sr = null;

	// Variables should be declared here
	private static SpaceShooter instance = null;
	private UbiquiGamePlatform platform;
	public int height;
	public int width;

	// Texture Manager

	// GameOverMessage
//	private List<Player> ranking;
//	private Map<Player, Integer> score;


	public SpaceShooter(UbiquiGamePlatform platform){
		super(platform);
		this.platform = platform;
		gsm = new GameStateManager();
		this.instance = this;

		// GameOverMessage vars
//		this.ranking = new ArrayList<Player>();
//		this.score = new HashMap<Player, Integer>();

	}

	/*
	 * Is called once when this minigame is being created
	 */
	@Override
	public void create() {
		// This should come from the platform
		height = platform.getScreenDimension().getHeight();
		width = platform.getScreenDimension().getWidth();

		// create the drawing boards
		sb = new SpriteBatch();
		sr = new ShapeRenderer();

		// Push in first state
		gsm.push(new CountDownState(gsm));
//		gsm.push(new PlayState(gsm));
	}

	/*
	 * Is called to update the status of the objects in the game
	 * @param delta difference in time to the last update call
	 */
	@Override
	public void update(float delta) {
		//Default update call delegate to GameStateManager
		gsm.update(delta);
	}

	/*
	 * Is called when the screen is being rendered
	 * @param delta difference in time to the last render call
	 */
	@Override
	public void render(float delta) {
		//Default render call delegate to GameStateManager
		gsm.render(sb, sr);
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
		return ControllerFace.Controller_Cross_A;
	}

	/*
	* This function declares a GameInfo and returns it for the platform
	* @return GameInfo This class holds some informations over the game
	*/
	@Override
	public GameInfo getGameInfo() {
		return gameInfo;
	}

	public static SpaceShooter getInstance(){
		return instance;
	}

//	public List<Player> getRanking(){
//		return ranking;
//	}
//
//	public void setRanking(List<Player> ranking){
//		this.ranking = ranking;
//	}
//
//	public Map<Player, Integer> getScore(){
//		return score;
//	}
//
//	public void setScore(Map<Player, Integer> score){
//		this.score = score;
//	}


}
