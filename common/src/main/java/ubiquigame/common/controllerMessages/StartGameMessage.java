package ubiquigame.common.controllerMessages;

import ubiquigame.common.ControllerFace;

/**
 * This is the Message that the platform sends to the controller, to tell it that a new game has started.
 * It contains the name of the controllerFace that the controller needs to show
 */
public class StartGameMessage extends NetworkMessage{

	private static final long serialVersionUID = -5249236227104649288L;
	
	private ControllerFace controllerFace;

    public StartGameMessage(ControllerFace controllerFace) {
        this.controllerFace = controllerFace;
    }

    /**
     * Returns the ID of a specific controller layout for the controller to use
     * @return controllerFace
     */
    public ControllerFace getControllerFace() {
        return controllerFace;
    }
}
