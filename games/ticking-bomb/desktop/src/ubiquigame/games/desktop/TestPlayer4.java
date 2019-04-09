package ubiquigame.games.desktop;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ubiquigame.common.Controller;
import ubiquigame.common.Player;

public class TestPlayer4 implements Player {

	private Controller testController;

	public TestPlayer4() {
		testController = new KeyboardController4();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Don";
	}

	@Override
	public Controller getController() {
		return testController;
	}

	@Override
	public Image getAvatar() {
		// TODO Auto-generated method stub
		return null;
	}

}