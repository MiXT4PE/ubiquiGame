package ubiquigame.platform.network;

import com.badlogic.gdx.Gdx;
import ubiquigame.common.controllerMessages.ConnectResponseMessage;
import ubiquigame.common.controllerMessages.NetworkPackage;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.session.ConnectionSession;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptConnectionThread extends Thread {

    private boolean isAcceptingConnections = false;
    private ServerSocket serverSocket;

    public AcceptConnectionThread() {
        this.setName("AcceptConnectionThread");
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(PlatformConfiguration.TCP_PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                synchronized (ConnectionController.getInstance()) {
                    if (this.isAcceptingConnections() && ConnectionSession.getCurrent().getPlayerCount() < 4) {
                        ConnectionController.getInstance().addConnection(socket);
                    } else {
                        ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
                        stream.writeObject(new NetworkPackage(new ConnectResponseMessage(false, 0)));
                        Thread.sleep(10);
                        socket.close();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }

    public boolean isAcceptingConnections() {
        return isAcceptingConnections;
    }

    public void setAcceptingConnections(boolean acceptingConnections) {
        isAcceptingConnections = acceptingConnections;
    }
}
