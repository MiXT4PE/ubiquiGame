package ubiquigame.games.desktop;

import com.badlogic.gdx.Gdx;
import ubiquigame.common.Controller;
import ubiquigame.common.ControllerFeedbacks;
import ubiquigame.common.FeedbackParams;
import ubiquigame.common.impl.InputState;

import static com.badlogic.gdx.Input.Keys;

public class KeyboardController3 implements Controller {

	private InputState inputState = new InputState();

	public KeyboardController3() {
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
		inputState.setNext(Gdx.input.isKeyPressed(Keys.S));
	}

	@Override
	public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {
		// unused
	}

}
