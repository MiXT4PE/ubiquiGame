package ubiquigame.platform.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ubiquigame.platform.PlatformImpl;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.menu.TutorialScreen;
import ubiquigame.platform.mocks.Mocks;

public class ScreenTester {
	public static void main (String[] arg) {
		PlatformImpl instance = PlatformImpl.getInstance();
		instance.onCreate(() -> instance.setScreen(new TutorialScreen(Mocks.getDummyGames().get(0).getGameInfo())));
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = PlatformConfiguration.WINDOW_TITLE;
		config.width = PlatformConfiguration.WINDOW_WIDTH;
		config.height = PlatformConfiguration.WINDOW_HEIGHT;
		config.fullscreen = PlatformConfiguration.WINDOW_FULLSCREEN;
		new LwjglApplication(instance, config);
		
	}
}
