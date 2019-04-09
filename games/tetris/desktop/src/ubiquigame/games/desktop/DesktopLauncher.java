package ubiquigame.games.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ubiquigame.games.tetris.TetrisGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Test";
		config.width = 1600;//1024;
		config.height = 800;
		config.resizable = false;
		TestPlatform testPlatform = new TestPlatform();
		testPlatform.setGame(new TetrisGame(testPlatform));
		new LwjglApplication(testPlatform, config);
	}
}
