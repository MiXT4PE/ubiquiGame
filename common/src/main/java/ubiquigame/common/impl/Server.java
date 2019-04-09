package ubiquigame.common.impl;

import java.util.Arrays;

public class Server {
    private String name, IP;
    private String[] players;

    public Server(String name, String IP, String[] players) {
        this.name = name;
        this.IP = IP;
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public String getIP() {
        return IP;
    }

    public String[] getPlayers() {
        return players;
    }

    public void setPlayers(String[] players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return "Server{" +
                "name='" + name + '\'' +
                ", IP='" + IP + '\'' +
                ", players=" + Arrays.toString(players) +
                '}';
    }
}
