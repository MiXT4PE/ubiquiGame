package com.ubiquigame.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import com.ubiquigame.utility.Utility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import ubiquigame.common.constants.NetworkConfiguration;
import ubiquigame.common.controllerMessages.BroadcastMessage;
import ubiquigame.common.controllerMessages.NetworkMessage;
import ubiquigame.common.controllerMessages.NetworkPackage;

public class Lobby implements Runnable {
    private String ip;
    private TextView[] players, playersReady;
    private ImageView[] playersAvatar;
    private Context context;

    public Lobby(String ip, TextView[] players, TextView[] playersReady, ImageView[] playersAvatar, Context context){
        this.ip = ip;
        this.players = players;
        this.playersReady = playersReady;
        this.playersAvatar = playersAvatar;
        this.context = context;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(NetworkConfiguration.udpBroadcastPort)){
            socket.setSoTimeout(100);
            byte[] buffer = new byte[512];
            BroadcastMessage bcm, previousBcm;
            bcm = null;
            while (Utility.u.isInLobby()) {
                try {
                    socket.receive(new DatagramPacket(buffer, buffer.length));
                    NetworkMessage nm = NetworkPackage.fromBytes(buffer).getNetworkMessage();
                    if (nm instanceof BroadcastMessage) {
                        previousBcm = bcm;
                        bcm = (BroadcastMessage) nm;
                        if (bcm.getIP().equals(ip) && (previousBcm == null || !(Arrays.equals(previousBcm.getPlayers(), bcm.getPlayers()) && Arrays.equals(previousBcm.getReady(), bcm.getReady())))) {
                            String[] playerNames = bcm.getPlayers();
                            boolean[] playerReadyBoolean = bcm.getReady();
                            new Handler(Looper.getMainLooper()).post(() -> {
                                for (int i = 0; i < playerNames.length; i++) {
                                    players[i].setText(playerNames[i]);
                                    playersReady[i].setText((playerReadyBoolean[i] ? "Player is ready" : "Player is not ready"));
                                    final int temp = i;
                                    Utility.u.getProfilePicture(context, b -> {
                                        if (b != null) {
                                            playersAvatar[temp].setImageBitmap(b);
                                        }
                                    }, playerNames[temp]);
                                }
                            });
                        }
                    }
                } catch(SocketTimeoutException ignored){}
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
