package com.ubiquigame.network;

import com.ubiquigame.utility.Utility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import ubiquigame.common.constants.NetworkConfiguration;
import ubiquigame.common.controllerMessages.BroadcastMessage;
import ubiquigame.common.controllerMessages.NetworkMessage;
import ubiquigame.common.controllerMessages.NetworkPackage;
import ubiquigame.common.impl.Server;

public class Search implements Runnable {

    private BlockingQueue<Server> serverQueue;
    private ArrayList<String> ips = new ArrayList<>();

    public Search(BlockingQueue<Server> serverQueue) {
        this.serverQueue = serverQueue;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(NetworkConfiguration.udpBroadcastPort)) {
            socket.setSoTimeout(10);
            byte[] buffer = new byte[512];
            while (Utility.u.isSearching()) {
                try {
                    socket.receive(new DatagramPacket(buffer, buffer.length));
                    NetworkMessage nm = NetworkPackage.fromBytes(buffer).getNetworkMessage();

                    if (nm instanceof BroadcastMessage) {
                        BroadcastMessage message = (BroadcastMessage) nm;
                        String ip = message.getIP();

                        if (!ips.contains(ip)) {
                            ips.add(ip);
                            serverQueue.put(new Server(message.getName(), ip, message.getPlayers()));
                        }
                    }
                } catch(SocketTimeoutException ignored){}
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
