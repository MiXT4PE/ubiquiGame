package ubiquigame.games.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ubiquigame.games.EnchantedLabyrinth.EnchantedLabyrinthGame;

import javax.swing.*;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Test";
		//config.width = 1024;
		//config.height = 700;
		//config.resizable = true;
		config.setFromDisplayMode(LwjglApplicationConfiguration.getDesktopDisplayMode());

		TestPlatform testPlatform = new TestPlatform();
		testPlatform.setGame(new EnchantedLabyrinthGame(testPlatform));
		new LwjglApplication(testPlatform, config);
	}

}
