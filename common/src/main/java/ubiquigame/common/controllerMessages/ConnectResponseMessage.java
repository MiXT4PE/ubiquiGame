package ubiquigame.common.controllerMessages;

/**
 * This is the message that the platform sends to the controller to accept or deny a connection attempt.
 * It also tells the controller which "PlayerIdentifier" (Index + Color) he controls
 */
public class ConnectResponseMessage extends NetworkMessage{


	private static final long serialVersionUID = 7285674704568515995L;
	private boolean isSuccess;
    private int playerIndex;
    private String connectionFailedMessage; // Error message for when isSuccess is false

    public ConnectResponseMessage(boolean isSuccess, int playerIndex) {
        this.isSuccess = isSuccess;
        this.playerIndex = playerIndex;
    }

    public ConnectResponseMessage(boolean isSuccess, int playerIndex, String connectionFailedMessage) {
        this.isSuccess = isSuccess;
        this.playerIndex = playerIndex;
        this.connectionFailedMessage = connectionFailedMessage;
    }

    /**
     * Returns true or false whether or not the connection attempt was successful
     * @return isSuccess
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * Returns the Index and Color for a specific player
     * @return PlayerIdentifier
     */
    public int getPlayerIdentifier() {
        return playerIndex;
    }


    /**
     * Returns the error message for why the connection has failed
     * @return connectionFailedMessage
     */
    public String getConnectionFailedMessage() {
        return connectionFailedMessage;
    }

    @Override
    public String toString() {
        return "ConnectResponseMessage{" +
                "isSuccess=" + isSuccess +
                ", playerIndex=" + playerIndex +
                ", connectionFailedMessage='" + connectionFailedMessage + '\'' +
                '}';
    }
}
