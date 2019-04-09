package ubiquigame.games.desktop;

import com.badlogic.gdx.Gdx;
import ubiquigame.common.Controller;
import ubiquigame.common.ControllerFeedbacks;
import ubiquigame.common.FeedbackParams;
import ubiquigame.common.impl.InputState;

import static com.badlogic.gdx.Input.Keys;

public class KeyboardController4 implements Controller {

	private InputState inputState = new InputState();

	public KeyboardController4() {
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
		inputState.setNext(Gdx.input.isKeyPressed(Keys.D));
	}

	@Override
	public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {
		// unused
	}

}
