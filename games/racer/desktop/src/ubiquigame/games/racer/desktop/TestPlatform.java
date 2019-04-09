package ubiquigame.games.racer.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGame;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.common.impl.ScreenDimension;
import ubiquigame.games.racer.RacerMain;

public class TestPlatform extends Game implements UbiquiGamePlatform {

	private GameRunnerScreen gameRunnerScreen;
	private Player[] players;

	public TestPlatform() {
		players = new Player[] {
				new TestPlayer1(Input.Keys.A, Input.Keys.D, Input.Keys.W, Input.Keys.SPACE, "Anna"),
				new TestPlayer1(Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.UP, Input.Keys.NUMPAD_0, "Berta")
				//new TestPlayer1(Input.Keys.H, Input.Keys.K, Input.Keys.U, Input.Keys.I, "Carl"),
				//new TestPlayer1(Input.Keys.NUMPAD_1, Input.Keys.NUMPAD_3, Input.Keys.NUMPAD_5, Input.Keys.PLUS, "Dennis")
		};
	}

	@Override
	public void create() {
		gameRunnerScreen.create();
		setScreen(gameRunnerScreen);
		gameRunnerScreen.start();
		
	}

	@Override
	public void render() {
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			System.exit(0);
		}
		if (screen != null)
			screen.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public Player[] getPlayers() {
		return players;
	}

	@Override
	public void notifyCurrGameOver(GameOverMessage state) {
		gameRunnerScreen.stop();
		setGame(new RacerMain(this));
	}

	@Override
	public ScreenDimension getScreenDimension() {
		return new ScreenDimension(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public float getMusicVolume() {
		return 1;
	}

	@Override
	public float getSoundVolume() {
		return 1;
	}

	public void setGame(UbiquiGame game) {
		gameRunnerScreen = new GameRunnerScreen(game);
		

	}
}
