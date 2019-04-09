package ubiquigame.games.racer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ubiquigame.common.ControllerFace;
import ubiquigame.common.GameInfo;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.AbstractUbiquiGame;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.games.racer.overlay.HUD;
import ubiquigame.games.racer.states.GameStateManager;
import ubiquigame.games.racer.states.PlayState;

public class RacerMain extends AbstractUbiquiGame{
	//GameInfo
	private static final GameInfo gameInfo = new GameInfo() {
		@Override
		public String getGameTitle() {
			return "Racer";
		}

		@Override
		public String getDescription() {
			return "Dies ist ein einfaches Rennspiel. Fahre gegen deine Freunde und gewinne das Rennen.";
		}

		@Override
		public Image getThumbnail() {
			return new Image(new Texture(Gdx.files.internal("racer/images/thumbnail.png")));
		}

		@Override
		public Image getTutorialManual() {
			return new Image(new Texture(Gdx.files.internal("racer/images/racerManual.png")));
		}
	};
	//This is the manager for all the states that are being handled
	private GameStateManager gsm;

	//Everything can be drawn in here
	public SpriteBatch sb = null;
	public ShapeRenderer sr = null;

	//Variables should be declared here
	private static RacerMain instance = null;
	private UbiquiGamePlatform platform;
	public int height;
	public int width;
	public HUD hud;

	public RacerMain(UbiquiGamePlatform platform){
		super(platform);
		this.platform = platform;
		gsm = new GameStateManager();
		this.instance = this;
	}

	/*
	 * Is called once when this minigame is being created
	 */
	@Override
	public void create() {
		//This should come from the platform
		height = platform.getScreenDimension().getHeight();
		width = platform.getScreenDimension().getWidth();

		//create the drawing boards
		sb = new SpriteBatch();
		sr = new ShapeRenderer();

		//create the HUD -> in hud you should define the hud explicitly
		hud = new HUD(sb, platform);

		//Push in first state
		gsm.push(new PlayState(gsm, hud));
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
		Gdx.gl.glClearColor(49/255f,117/255f,60/255f, 1/255f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//Default render call delegate to GameStateManager
		gsm.render(sb, sr);

		//render for the hud
		hud.stage.act(delta);
		hud.stage.draw();
	}

	/*
	 * This function is being called when the game is over
	 */
	@Override
	public void gameover(GameOverMessage state) {
		System.out.println(" *** 1. " + state.getPlayerRanking().get(0).getName() + " *** Score: " + state.getScore(state.getPlayerRanking().get(0)));
		System.out.println(" *** 2. " + state.getPlayerRanking().get(1).getName() + " *** Score: " + state.getScore(state.getPlayerRanking().get(1)));
		if (platform.getPlayers().length >=3){
			System.out.println(" *** 3. " + state.getPlayerRanking().get(2).getName() + " *** Score: " + state.getScore(state.getPlayerRanking().get(2)));
		}
		if (platform.getPlayers().length ==4){
			System.out.println(" *** 4. " + state.getPlayerRanking().get(3).getName() + " *** Score: " + state.getScore(state.getPlayerRanking().get(3)));
		}
		platform.notifyCurrGameOver(state);

		gsm.peek().dispose();
		gsm.pop();
		sb.dispose();
		sr.dispose();
	}

	@Override
	public ControllerFace getControllerFace() {
		//should return controller
		return ControllerFace.Controller_Racer;
	}

	/*
	* This function declares a GameInfo and returns it for the platform
	* @return GameInfo This class holds some information over the game
	*/
	@Override
	public GameInfo getGameInfo() { //TODO write GameInfo
		return gameInfo;
	}

	public static RacerMain getInstance(){
		return instance;
	}

}
