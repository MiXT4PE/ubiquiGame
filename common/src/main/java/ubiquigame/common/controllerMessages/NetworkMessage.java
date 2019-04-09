package ubiquigame.common.controllerMessages;

import java.io.Serializable;

/**
 * This is the superclass of all Messages that are sent from controller to platform or vice versa via TCP or UDP
 * To send such a Message, it must be contained in a NetworkPackage
 */
public abstract class NetworkMessage implements Serializable {


	private static final long serialVersionUID = 733859625768363956L;

}
