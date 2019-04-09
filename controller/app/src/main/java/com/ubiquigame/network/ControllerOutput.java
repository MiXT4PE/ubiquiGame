package com.ubiquigame.network;

import com.ubiquigame.utility.Utility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import ubiquigame.common.constants.NetworkConfiguration;
import ubiquigame.common.controllerMessages.InputMessage;
import ubiquigame.common.controllerMessages.NetworkPackage;
import ubiquigame.common.impl.InputState;

public class ControllerOutput implements Runnable {

    private final String ip;

    public ControllerOutput(String ip) {
        this.ip = ip;
    }

    @Override
    public void run() {
        byte[] serializedMessage;
        DatagramPacket udpPacket;

        try (DatagramSocket socket = new DatagramSocket()) {
            // attach fixed ip and port to socket
            socket.connect(InetAddress.getByName(ip), NetworkConfiguration.udpPort);
            // runs while playing flag is set
            while (Utility.u.isPlaying()) {
                serializedMessage = new NetworkPackage(new InputMessage(InputState.s)).getBytes();
                udpPacket = new DatagramPacket(serializedMessage, serializedMessage.length);
                socket.send(udpPacket);
                Thread.sleep(10);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
