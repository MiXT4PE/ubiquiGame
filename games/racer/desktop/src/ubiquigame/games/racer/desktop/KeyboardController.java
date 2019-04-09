package ubiquigame.games.racer.desktop;

import com.badlogic.gdx.Gdx;
import ubiquigame.common.Controller;
import ubiquigame.common.ControllerFeedbacks;
import ubiquigame.common.FeedbackParams;
import ubiquigame.common.impl.InputState;

public class KeyboardController implements Controller {

	private InputState inputState = new InputState();
	private int keyLeft;
	private int keyRight;
	private int keyUp;
	private int keySpace;

	public KeyboardController(int keyLeft, int keyRight, int keyUp, int keySpace) {
		this.keyLeft = keyLeft;
		this.keyRight = keyRight;
		this.keyUp = keyUp;
		this.keySpace = keySpace;
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
		inputState.setLeft(Gdx.input.isKeyPressed(keyLeft));
		inputState.setRight(Gdx.input.isKeyPressed(keyRight));
		inputState.setAccelerator(Gdx.input.isKeyPressed(keyUp));
		inputState.setBoost(Gdx.input.isKeyPressed(keySpace));
	}

	@Override
	public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {
		// unused
	}

}
