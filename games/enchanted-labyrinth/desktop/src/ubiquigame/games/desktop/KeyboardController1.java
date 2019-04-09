package ubiquigame.games.desktop;

import com.badlogic.gdx.Gdx;
import ubiquigame.common.Controller;
import ubiquigame.common.ControllerFeedbacks;
import ubiquigame.common.FeedbackParams;
import ubiquigame.common.impl.InputState;
import static com.badlogic.gdx.Input.Keys;

public class KeyboardController1 implements Controller {

	private InputState inputState = new InputState();

	public KeyboardController1() {
	}

	@Override
	public InputState getInput() {
		mapKeys();
		return inputState;
	}

	/**
	 * updates InputState by polling gdx input. Extend your keyboard mapping here.
	 */
	private void mapKeys() {
		inputState.setUp(Gdx.input.isKeyJustPressed(Keys.UP));
		inputState.setRight(Gdx.input.isKeyJustPressed(Keys.RIGHT));
		inputState.setDown(Gdx.input.isKeyJustPressed(Keys.DOWN));
		inputState.setLeft(Gdx.input.isKeyJustPressed(Keys.LEFT));
	}

	@Override
	public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {
		// unused
	}

}
