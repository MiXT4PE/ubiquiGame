package ubiquigame.platform.debug;

import com.badlogic.gdx.Gdx;

import ubiquigame.common.ControllerFeedbacks;
import ubiquigame.common.FeedbackParams;
import ubiquigame.common.impl.InputState;
import ubiquigame.platform.session.player.ControllerInternal;

import static com.badlogic.gdx.Input.Keys;

public class DebugController extends ControllerInternal {

	private InputState inputState = new InputState();

	@Override
	public InputState getInput() {
		return inputState;
	}

	@SuppressWarnings("serial")
	@Override
	public void update() {
		inputState = new InputState() {

			public boolean isLeft() {
				return Gdx.input.isKeyPressed(Keys.LEFT);
			}

			public boolean isRight() {
				return Gdx.input.isKeyPressed(Keys.RIGHT);
			}

			public boolean isUp() {
				return Gdx.input.isKeyPressed(Keys.UP);
			}

			@Override
			public boolean isAccelerator() {
				return Gdx.input.isKeyPressed(Keys.SPACE);
			}

			@Override
			public boolean isBoost() {
				return Gdx.input.isKeyPressed(Keys.SPACE);
			}

			@Override
			public boolean isDown() {
				return Gdx.input.isKeyPressed(Keys.DOWN);
			}

			@Override
			public boolean isNext() {
				return Gdx.input.isKeyPressed(Keys.SPACE);
			}
		};
	};

	@Override
	public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {

	}

	@Override
	public int getPacketsPerSecond() {
		return 0;
	}
}
