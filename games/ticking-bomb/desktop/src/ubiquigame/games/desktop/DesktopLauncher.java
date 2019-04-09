package ubiquigame.games.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ubiquigame.games.ticking_bomb.TBMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Test";
		//config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
		//config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
		config.width = 1920;
		config.height = 1080;
		config.fullscreen = true;
		config.resizable = false;
		TestPlatform testPlatform = new TestPlatform();
		testPlatform.setGame(new TBMain(testPlatform));
		new LwjglApplication(testPlatform, config);
	}
}
