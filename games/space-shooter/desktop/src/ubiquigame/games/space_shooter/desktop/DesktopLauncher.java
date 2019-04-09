package ubiquigame.games.space_shooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ubiquigame.games.space_shooter.SpaceShooter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Test";
		config.width = 1920;
		config.height = 1080;
		config.resizable = true;
		TestPlatform testPlatform = new TestPlatform();
		testPlatform.setGame(new SpaceShooter(testPlatform));
		new LwjglApplication(testPlatform, config);
	}
}
