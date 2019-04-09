package ubiquigame.games.racer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ubiquigame.games.racer.RacerMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Racer";
		config.width = 1920;
		config.height = 1080;
		config.resizable = true;
		config.fullscreen = true;
		TestPlatform testPlatform = new TestPlatform();
		testPlatform.setGame(new RacerMain(testPlatform));
		new LwjglApplication(testPlatform, config);
	}
}
