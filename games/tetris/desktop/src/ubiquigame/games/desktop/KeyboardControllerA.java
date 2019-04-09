package ubiquigame.games.desktop;

import com.badlogic.gdx.Gdx;
import ubiquigame.common.Controller;
import ubiquigame.common.ControllerFeedbacks;
import ubiquigame.common.FeedbackParams;
import ubiquigame.common.impl.InputState;
import static com.badlogic.gdx.Input.Keys;

public class KeyboardControllerA implements Controller {

	private InputState inputState = new InputState();

	public KeyboardControllerA() {
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
		inputState.setLeft(Gdx.input.isKeyPressed(Keys.LEFT));
		inputState.setRight(Gdx.input.isKeyPressed(Keys.RIGHT));
		inputState.setDown(Gdx.input.isKeyPressed(Keys.DOWN));
		inputState.setA(Gdx.input.isKeyPressed(Keys.CONTROL_RIGHT));
	}

	@Override
	public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {
		// unused
	}

}
