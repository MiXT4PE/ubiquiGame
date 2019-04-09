package com.ubiquigame.network;

import android.content.Context;
import android.content.Intent;

import com.ubiquigame.controller.ControllerActivity;
import com.ubiquigame.controller.LobbyActivity;
import com.ubiquigame.controller.OverviewActivity;
import com.ubiquigame.utility.User;
import com.ubiquigame.utility.Utility;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.NoRouteToHostException;
import java.net.Socket;

import ubiquigame.common.ControllerFace;
import ubiquigame.common.constants.NetworkConfiguration;
import ubiquigame.common.controllerMessages.ConnectRequestMessage;
import ubiquigame.common.controllerMessages.ConnectResponseMessage;
import ubiquigame.common.controllerMessages.NetworkMessage;
import ubiquigame.common.controllerMessages.NetworkPackage;
import ubiquigame.common.controllerMessages.SetReadyMessage;
import ubiquigame.common.controllerMessages.StartGameMessage;
import ubiquigame.common.controllerMessages.TournamentEndedMessage;

public class ControllerConnection implements Runnable {

    private Context context;
    private String IP;

    public ControllerConnection(String IP, Context context) {
        this.IP = IP;
        this.context = context;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(IP, NetworkConfiguration.tcpPort)) {
            // output stream
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());

            // send connect request
            outStream.writeObject(new NetworkPackage(new ConnectRequestMessage(User.u.getUsername())));

            // wait for responses
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

            // get and process messages
            while(true) {
                NetworkMessage nm = ((NetworkPackage) inStream.readObject()).getNetworkMessage();
                if (nm instanceof ConnectResponseMessage) {
                    ConnectResponseMessage response = (ConnectResponseMessage) nm;
                    if (response.isSuccess() && !Utility.u.isInLobby()) {
                        Utility.u.setSearching(false);
                        Utility.u.setConnecting(false);
                        context.startActivity((new Intent(context, LobbyActivity.class)).putExtra("IP", IP));
                        new Thread(() -> {
                            try {
                                while (!User.u.isReady()) {
                                    Thread.sleep(100);
                                }
                                outStream.writeObject(new NetworkPackage(new SetReadyMessage(true)));
                            } catch (InterruptedException | IOException e) {
                                e.printStackTrace();
                            }
                        }).start();
                    } else {
                        Utility.u.setConnecting(false);
                        Utility.u.showError(Utility.u.isInLobby() ? "ConnectResponseMessage already received" : response.getConnectionFailedMessage() + ". Tap card to try again", context);
                    }
                } else if (nm instanceof StartGameMessage){
                    String layout = ((StartGameMessage) nm).getControllerFace().getFaceName();
                    if (layout.equals(ControllerFace.Default.getFaceName())) {
                        Utility.u.showError("Default controller face received: Something went wrong.", context);
                        Utility.u.stopThreads();
                        context.startActivity(new Intent(context, OverviewActivity.class));
                    } else {
                        Intent intent = new Intent(context, ControllerActivity.class);
                        intent.putExtra("layout", context.getResources().getIdentifier(layout, "layout", context.getPackageName()));
                        intent.putExtra("ip", IP);
                        intent.putExtra("isViewing", false);
                        context.startActivity(intent);
                    }
                } else if (nm instanceof TournamentEndedMessage){
                    Utility.u.stopThreads();
                    break;
                }
            }
            // after break go back to OverviewActivity
            context.startActivity(new Intent(context, OverviewActivity.class));
        } catch (IOException | ClassNotFoundException e) {
            if (e instanceof NoRouteToHostException){
                Utility.u.showError("Host unreachable", context);
            /*} else if (e instanceof _){ ToDo
                Utility.u.showError("Connection lost", context);
                Utility.u.stopThreads();
                context.startActivity(new Intent(context, OverviewActivity.class));*/
            } else {
                e.printStackTrace();
            }
        }
    }
}

