package ubiquigame.games.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ubiquigame.common.Controller;
import ubiquigame.common.ControllerFeedbacks;
import ubiquigame.common.FeedbackParams;
import ubiquigame.common.impl.InputState;

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
        inputState.setLeft(Gdx.input.isKeyPressed(Input.Keys.LEFT));
        inputState.setRight(Gdx.input.isKeyPressed(Input.Keys.RIGHT));
        inputState.setDown(Gdx.input.isKeyPressed(Input.Keys.DOWN));
        inputState.setUp(Gdx.input.isKeyPressed(Input.Keys.UP));
    }

    @Override
    public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {
        // unused
    }
}