package ubiquigame.games.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import ubiquigame.common.ControllerFeedbacks;
import ubiquigame.common.FeedbackParams;
import ubiquigame.common.impl.InputState;

public class KeyboardController2 implements ubiquigame.common.Controller {
    private InputState inputState = new InputState();

    public KeyboardController2() {
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
        inputState.setUp(Gdx.input.isKeyJustPressed(Input.Keys.W));
        inputState.setRight(Gdx.input.isKeyJustPressed(Input.Keys.D));
        inputState.setDown(Gdx.input.isKeyJustPressed(Input.Keys.S));
        inputState.setLeft(Gdx.input.isKeyJustPressed(Input.Keys.A));
    }

    @Override
    public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {
        // unused
    }
}
