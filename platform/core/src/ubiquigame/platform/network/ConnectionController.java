package ubiquigame.platform.network;

import ubiquigame.common.ControllerFace;
import ubiquigame.platform.session.ConnectionSession;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionController {

    private static ConnectionController instance;

    private BroadcastThread broadcastThread;
    private AcceptConnectionThread acceptConnectionThread;
    private ControllerInputThread controllerInputThread;

    private List<ControllerConnectionThread> controllerConnectionThreads = new ArrayList<>();


    public ConnectionController() {
        this.broadcastThread = new BroadcastThread();
        this.broadcastThread.start();
        this.acceptConnectionThread = new AcceptConnectionThread();
        this.acceptConnectionThread.start();
        this.controllerInputThread = new ControllerInputThread();
        this.controllerInputThread.start();
    }

    public synchronized void startLobby() {
        if(!broadcastThread.isAlive()){
            this.broadcastThread = new BroadcastThread();
            broadcastThread.start();
        }
        this.broadcastThread.setActive(true);
        this.acceptConnectionThread.setAcceptingConnections(true);
        this.controllerConnectionThreads.stream().forEach(controllerConnectionThread -> controllerConnectionThread.stopThread());
        this.controllerConnectionThreads.clear();
        ConnectionSession.getCurrent().kickAll();
    }

    public synchronized void stopLobby() {
        this.broadcastThread.setActive(false);
        this.acceptConnectionThread.setAcceptingConnections(false);
    }

    public static ConnectionController getInstance() {
        if (instance == null) {
            instance = new ConnectionController();
        }
        return instance;
    }


    public synchronized void addConnection(Socket socket) {
        ControllerConnectionThread controllerConnectionThread = new ControllerConnectionThread(socket);
        this.controllerConnectionThreads.add(controllerConnectionThread);
        controllerConnectionThread.start();
    }

    public synchronized void removeConnection(ControllerConnectionThread controllerConnectionThread) {
        this.controllerConnectionThreads.remove(controllerConnectionThread);
    }

    public synchronized void sendGameStarted(ControllerFace controllerFace) {
        this.controllerConnectionThreads.forEach(controllerConnectionThread -> controllerConnectionThread.sendStartGameMessage(controllerFace));
    }

    public synchronized void sendTournamentEnded() {
        this.controllerConnectionThreads.forEach(controllerConnectionThread -> controllerConnectionThread.sendTournamentEndedMessage());
    }
}
