package ubiquigame.common.controllerMessages;

/**
 * This is the message that the controller sends to the platform to request a connection to the lobby
 */
public class ConnectRequestMessage extends NetworkMessage{
 
	private static final long serialVersionUID = -3866305860458319543L;
	private String username;

    public ConnectRequestMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
