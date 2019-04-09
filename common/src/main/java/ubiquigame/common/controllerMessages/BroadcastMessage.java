package ubiquigame.common.controllerMessages;

import java.util.Arrays;

/**
 * This is the Message, that the platform boradcasts while it has a lobby opened where players can still join.
 * It also includes a platform name, its IP and an Array of its current players
 */
public class BroadcastMessage extends NetworkMessage{


	private static final long serialVersionUID = 8865526999143820555L;
	private String name, IP;
    private String[] players;
    private boolean[] ready;

    public BroadcastMessage(String name, String IP, String[] players, boolean[] ready) {
        this.name = name;
        this.IP = IP;
        this.players = players;
        this.ready = ready;
    }

    /**
     * Returns the name the broadcasting platform
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the IP address of the broadcasting platform
     * @return IP
     */
    public String getIP() {
        return IP;
    }

    /**
     * Returns names of current players in the broadcasting platform
     * @return players
     */
    public String[] getPlayers() {
        return players;
    }

    /**
     * Returns the ready status of current players in the broadcasting platform
     * @return ready
     */
    public boolean[] getReady() {
        return ready;
    }

    @Override
    public String toString() {
        return "BroadcastMessage{" +
                "name='" + name + '\'' +
                ", IP='" + IP + '\'' +
                ", players=" + Arrays.toString(players) +
                '}';
    }
}
