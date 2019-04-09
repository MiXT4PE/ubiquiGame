package ubiquigame.platform.network;

import ubiquigame.common.controllerMessages.BroadcastMessage;
import ubiquigame.common.controllerMessages.NetworkPackage;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.session.ConnectionSession;
import ubiquigame.platform.session.player.PlayerInternal;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class BroadcastThread extends Thread{

    private boolean isActive;
    private DatagramSocket datagramSocket;
    private List<InetAddress> broadcastList;
    private String ip;

    public BroadcastThread() {
        this.setName("BroadcastThread");
    }

    @Override
    public void run() {
        try {
            this.initialize();
            while(true){
                if (isActive) {

                    this.update();

                } else {
                    Thread.sleep(100);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(datagramSocket != null) {
                datagramSocket.close();
            }
        }
    }

    private void initialize() throws SocketException, UnknownHostException {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress().toString().split("/")[0];
        }

        this.datagramSocket = new DatagramSocket();
        datagramSocket.setBroadcast(true);
        this.broadcastList = new ArrayList<InetAddress>();
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while(interfaces.hasMoreElements()){
            NetworkInterface networkInterface = interfaces.nextElement();

            if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                networkInterface.getInterfaceAddresses().stream()
                        .map(a -> a.getBroadcast())
                        .filter(Objects::nonNull)
                        .forEach(broadcastList::add);
            }
        }

    }
//TODO:
    private void update() throws IOException, InterruptedException {
        for(InetAddress broadcast : broadcastList){
            NetworkPackage netPackage = new NetworkPackage(
                    new BroadcastMessage(
                            PlatformConfiguration.PLATFORM_NAME,
                            ip,
                            getPlayerList(),
                            getPlayerReadyList()));

            byte[] buffer = netPackage.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, broadcast, PlatformConfiguration.UDP_BROADCAST_PORT);
            datagramSocket.send(packet);
        }
        Thread.sleep(10);
    }

    private String[] getPlayerList() {
        return Arrays.stream(ConnectionSession.getCurrent().getPlayers()).map(playerInternal -> playerInternal.getName()).toArray(size -> new String[size]);
    }

    private boolean[] getPlayerReadyList() {
        PlayerInternal[] players = ConnectionSession.getCurrent().getPlayers();
        boolean[] readyArray = new boolean[players.length];

        for(int a = 0; a < readyArray.length; a++){
            readyArray[a] = players[a].isReady();
        }

        return readyArray;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}
