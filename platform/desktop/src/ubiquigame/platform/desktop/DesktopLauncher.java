package ubiquigame.platform.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ubiquigame.platform.PlatformImpl;
import ubiquigame.platform.config.PlatformConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {throwable.printStackTrace(); System.exit(-1);});
		PlatformImpl instance = PlatformImpl.getInstance();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = PlatformConfiguration.WINDOW_TITLE;
		config.width = PlatformConfiguration.WINDOW_WIDTH;
		config.height = PlatformConfiguration.WINDOW_HEIGHT;
		config.resizable = true;
		System.setProperty("org.lwjgl.opengl.Window.undecorated", Boolean.toString(PlatformConfiguration.WINDOW_FULLSCREEN));

		config.addIcon("icons/icon128.png", FileType.Internal);
		config.addIcon("icons/icon64.png", FileType.Internal);
		config.addIcon("icons/icon32.png", FileType.Internal);
		config.addIcon("icons/icon16.png", FileType.Internal);
		new LwjglApplication(instance, config);
	}
}
