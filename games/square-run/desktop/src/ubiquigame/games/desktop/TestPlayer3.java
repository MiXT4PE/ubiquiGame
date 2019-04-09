package ubiquigame.games.desktop;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ubiquigame.common.Controller;
import ubiquigame.common.Player;

public class TestPlayer3 implements Player {

    private Controller testController;

    public TestPlayer3() {
        testController = new KeyboardController3();
    }

    @Override
    public String getName() {
        return "IJKL";
    }

    @Override
    public Controller getController() {
        return testController;
    }

    @Override
    public Image getAvatar() {
        return null;
    }
}