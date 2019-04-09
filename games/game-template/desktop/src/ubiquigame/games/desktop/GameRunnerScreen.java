package ubiquigame.games.desktop;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;

import ubiquigame.common.UbiquiGame;
import ubiquigame.games.your_game_name.overlay.HUD;

public class GameRunnerScreen extends ScreenAdapter implements Screen {

	private UbiquiGame ubiquiGame;
	private boolean isStarted = false;

	public GameRunnerScreen(UbiquiGame ubiquiGame) {
		this.ubiquiGame = ubiquiGame;
	}

	@Override
	public void render(float delta) {
		if (this.isStarted) {
			ubiquiGame.update(delta);
		}
		ubiquiGame.render(delta);
	}

	public void create() {
		ubiquiGame.create();
	}

	public void stop() {
		this.isStarted = false;
	}

	public void start() {
		this.isStarted = true;
	}

}
