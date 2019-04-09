package ubiquigame.common.controllerMessages;

import ubiquigame.common.impl.InputState;

/**
 * This is the message, that the controller sends via UDP to the platform
 * that includes the state of all buttons on the controller
 */
public class InputMessage extends NetworkMessage{

	private static final long serialVersionUID = -1576736128752367760L;
	private InputState inputState;

    /**
     * Returns the Inputstate which includes all pressed and unpressed buttons
     * @return InputState
     */
    public InputState getInputState() {
        return inputState;
    }


    public InputMessage(InputState inputState) {
        this.inputState = inputState;
    }
}
