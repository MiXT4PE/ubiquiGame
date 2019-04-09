package ubiquigame.common.controllerMessages;

/**
 * This is the Message that the controller sends to the platform each time
 * it changes its ready state to unready or vice versa
 */
public class SetReadyMessage extends NetworkMessage{

	private static final long serialVersionUID = -1709969066188346032L;
	private boolean isReady;

    public SetReadyMessage(boolean isReady) {
        this.isReady = isReady;
    }

    public boolean isReady() {
        return isReady;
    }
}
