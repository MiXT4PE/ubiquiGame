package ubiquigame.games.desktop;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ubiquigame.common.Controller;
import ubiquigame.common.Player;

public class TestPlayerThalia implements Player {

	private Controller testController;

	public TestPlayerThalia() {
		testController = new KeyboardControllerB();
	}

	@Override
	public String getName() {
		return "Thalia";
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