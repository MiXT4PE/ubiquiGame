package ubiquigame.games.desktop;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ubiquigame.common.Controller;
import ubiquigame.common.Player;

public class TestPlayerManuel implements Player {

	private Controller testController;

	public TestPlayerManuel() {
		testController = new KeyboardControllerA();
	}

	@Override
	public String getName() {
		return "Manuel";
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