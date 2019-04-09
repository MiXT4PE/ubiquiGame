package ubiquigame.games.desktop;

import com.badlogic.gdx.Gdx;
import ubiquigame.common.Controller;
import ubiquigame.common.ControllerFeedbacks;
import ubiquigame.common.FeedbackParams;
import ubiquigame.common.impl.InputState;

import static com.badlogic.gdx.Input.Keys;

public class KeyboardControllerB implements Controller {

	private InputState inputState = new InputState();

	public KeyboardControllerB() {
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
		inputState.setLeft(Gdx.input.isKeyPressed(Keys.A));
		inputState.setRight(Gdx.input.isKeyPressed(Keys.D));
		inputState.setDown(Gdx.input.isKeyPressed(Keys.S));
		inputState.setA(Gdx.input.isKeyPressed(Keys.SPACE));
	}

	@Override
	public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {
		// unused
	}

}
