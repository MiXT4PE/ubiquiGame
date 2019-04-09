package ubiquigame.platform.session.player;

import com.badlogic.gdx.Gdx;
import ubiquigame.common.Controller;
import ubiquigame.common.ControllerFeedbacks;
import ubiquigame.common.FeedbackParams;
import ubiquigame.common.impl.InputState;

/**
 * Internal controller used by the platform
 */
public class ControllerInternal implements Controller {

    // The accumulated value of the inputStates that were received since the last frame
    private InputState receivedInputState = new InputState();
    // Counts the packets since the last frame
    private int packetCounter = 0;
    // The current "Packets per second"
    private int packetsPerSecond;
    private float passedTime;
    // The InputState from the last Frame
    private InputState lastInputState = new InputState();
    // The Input State that is currently used for the game
    private InputState currentInputState = new InputState();

    private boolean isConnected = false;

    public synchronized void update() {
        if (receivedInputState != null) {
            this.lastInputState = this.currentInputState;
            this.currentInputState = this.receivedInputState;
            this.receivedInputState = null;
        } else {
            // If no packets were received, use empty Inputstate
            this.lastInputState = this.currentInputState;
            this.currentInputState = new InputState();
        }
        passedTime += Gdx.graphics.getDeltaTime();

        if(passedTime>1f){
            packetsPerSecond = (int) (packetCounter / passedTime);
            passedTime = 0;
            packetCounter = 0;
        }

    }


    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public synchronized InputState getInput() {
        return currentInputState;
    }

    public synchronized void setInputState(InputState inputState) {
        this.packetCounter++;
        if (this.receivedInputState == null) {
            this.receivedInputState = inputState;
        } else {
            collectInput(this.receivedInputState, inputState);
        }


    }

    @Override
    public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {
        //NIY
    }

    @Override
    public InputState getLastInput() {
        return this.lastInputState;
    }

    private void collectInput(InputState collected, InputState received) {
        if(received.isNext()){collected.setNext(received.isNext());}
        if(received.isLeft()){collected.setLeft(received.isLeft());}
        if(received.isRight()){collected.setRight(received.isRight());}
        if(received.isUp()){collected.setUp(received.isUp());}
        if(received.isDown()){collected.setDown(received.isDown());}
        if(received.isA()){collected.setA(received.isA());}
        if(received.isB()){collected.setB(received.isB());}
        if(received.isBoost()){collected.setBoost(received.isBoost());}
        if(received.isAccelerator()){collected.setAccelerator(received.isAccelerator());}
    }

    public int getPacketsPerSecond() {
        return packetsPerSecond;
    }
}
