package ubiquigame.games.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ubiquigame.games.square_run.SquareRunMain;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Square";
        config.width = 1600;
        config.height = 900;
        config.resizable = false;
        TestPlatform testPlatform = new TestPlatform();
        testPlatform.setGame(new SquareRunMain(testPlatform));
        new LwjglApplication(testPlatform, config);
    }
}