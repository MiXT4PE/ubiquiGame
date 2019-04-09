package ubiquigame.games.desktop;

import com.badlogic.gdx.Gdx;
import ubiquigame.common.Controller;
import ubiquigame.common.ControllerFeedbacks;
import ubiquigame.common.FeedbackParams;
import ubiquigame.common.impl.InputState;
import static com.badlogic.gdx.Input.Keys;

public class KeyboardController implements Controller {

	private InputState inputState = new InputState();


	public KeyboardController() {
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
		inputState.setUp(Gdx.input.isKeyPressed(Keys.UP));
		inputState.setDown(Gdx.input.isKeyPressed(Keys.DOWN));
inputState.setA(Gdx.input.isKeyPressed(Keys.ENTER));
		//inputState.setNext(Gdx.input.isKeyPressed(Keys.ENTER));
	}

	@Override
	public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {
		// unused
	}

}
