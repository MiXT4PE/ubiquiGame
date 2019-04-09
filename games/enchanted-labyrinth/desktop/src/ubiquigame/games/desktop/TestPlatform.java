package ubiquigame.games.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGame;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.common.impl.ScreenDimension;
import ubiquigame.games.EnchantedLabyrinth.EnchantedLabyrinthGame;

public class TestPlatform extends Game implements UbiquiGamePlatform {

	private GameRunnerScreen gameRunnerScreen;
	private Player[] players;

	public TestPlatform()
	{
		players = new Player[] { new TestPlayer1(), new TestPlayer2(), new TestPlayer3(), new TestPlayer4()};
		this.gameRunnerScreen =new GameRunnerScreen(null);
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
		setGame(new EnchantedLabyrinthGame(this));
	}

	@Override
	public ScreenDimension getScreenDimension() {
		return new ScreenDimension(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	TestPlatform platform;
	@Override
	public float getMusicVolume() {
		return 0;
	}

	@Override
	public float getSoundVolume() {

		return 0;
	}

	public void setGame(UbiquiGame game) {
		gameRunnerScreen = new GameRunnerScreen(game);
	}
}
