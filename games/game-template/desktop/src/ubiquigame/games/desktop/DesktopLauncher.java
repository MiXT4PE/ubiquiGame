package ubiquigame.games.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ubiquigame.games.your_game_name.YourMainClass;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Test";
		config.width = 1024;
		config.height = 800;
		config.resizable = false;
		TestPlatform testPlatform = new TestPlatform();
		testPlatform.setGame(new YourMainClass(testPlatform));
		new LwjglApplication(testPlatform, config);
	}
}
