package ubiquigame.games.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ubiquigame.games.memory.MemoryGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Test";
		config.width = 1920;
		config.height = 1200;
		config.resizable = true;
		TestPlatform testPlatform = new TestPlatform();
		testPlatform.setGame(new MemoryGame(testPlatform));
		new LwjglApplication(testPlatform, config);
	}
}
