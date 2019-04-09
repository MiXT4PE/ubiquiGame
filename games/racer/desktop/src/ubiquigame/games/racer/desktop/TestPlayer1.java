package ubiquigame.games.racer.desktop;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ubiquigame.common.Controller;
import ubiquigame.common.Player;

public class TestPlayer1 implements Player {

	private Controller testController;
	private String name;

	public TestPlayer1(int keyLeft, int keyRight, int keyUp, int keySpace, String name) {
		this.name = name;
		testController = new KeyboardController(keyLeft, keyRight, keyUp, keySpace);
	}

	@Override
	public String getName() {
		return name;
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