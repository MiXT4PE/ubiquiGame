package ubiquigame.games.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ubiquigame.common.Controller;
import ubiquigame.common.ControllerFeedbacks;
import ubiquigame.common.FeedbackParams;
import ubiquigame.common.impl.InputState;

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
        inputState.setLeft(Gdx.input.isKeyPressed(Input.Keys.J));
        inputState.setRight(Gdx.input.isKeyPressed(Input.Keys.L));
        inputState.setDown(Gdx.input.isKeyPressed(Input.Keys.K));
        inputState.setUp(Gdx.input.isKeyPressed(Input.Keys.I));
    }

    @Override
    public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {
        // unused
    }
}