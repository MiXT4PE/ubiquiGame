package ubiquigame.platform.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import ubiquigame.common.controllerMessages.*;
import ubiquigame.common.impl.InputState;
import ubiquigame.platform.PlatformImpl;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.helpers.Listener;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class DummyController implements ApplicationListener {
    private Stage stage;
    private Table rootTable;
    private Skin skin;
    private TextButton receiveBroadcastButton;
    private TextField broadcastResultTextField;
    private TextButton requestConnectionButton;
    private InetAddress lobby;
    private TextField connectionResultTextField;
    private TextField controllerFaceTextField;
    private TextButton setReadyButton;
    private TextButton sendInputStateButton;
    private List<ControllerConnection> controllerConnections = new ArrayList<>();

    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1500;
        config.height = 900;
        new LwjglApplication(new DummyController(), config);
    }

    @Override
    public void create() {

        this.stage = new Stage();
        this.rootTable = new Table();
        PlatformImpl.getInstance().create();
        this.skin = PlatformImpl.getInstance().getAssets().getSkin();

//        this.rootTable.setDebug(true);
        this.rootTable.setFillParent(true);
        this.rootTable.defaults().width(200).height(50).space(20);

        this.stage.addActor(this.rootTable);

        Gdx.input.setInputProcessor(stage);

        receiveBroadcastButton = new TextButton("Receive Broadcast", skin);
        receiveBroadcastButton.addListener(Listener.onClicked(event -> receiveBroadcast()));

        broadcastResultTextField = new TextField("", skin);
        broadcastResultTextField.setDisabled(true);

        requestConnectionButton = new TextButton("Request connection", skin);
        requestConnectionButton.addListener(Listener.onClicked(event -> requestConnection()));

        connectionResultTextField = new TextField("", skin);
        connectionResultTextField.setDisabled(true);

        setReadyButton = new TextButton("Set ready", skin);
        setReadyButton.addListener(Listener.onClicked(event -> setReady()));

        controllerFaceTextField = new TextField("None", skin);
        controllerFaceTextField.setDisabled(true);

        sendInputStateButton = new TextButton("Send Input", skin);
        sendInputStateButton.addListener(Listener.onClicked(event -> sendInputState()));

        this.rootTable.add(receiveBroadcastButton);
        this.rootTable.add(broadcastResultTextField).width(1200);

        this.rootTable.row();

        this.rootTable.add(requestConnectionButton);
        this.rootTable.add(connectionResultTextField).width(1200);

        this.rootTable.row();

        this.rootTable.add(setReadyButton);

        this.rootTable.row();
        this.rootTable.add(new Label("ControllerFace: ", skin));
        this.rootTable.add(controllerFaceTextField).width(800);

        this.rootTable.row();
        this.rootTable.add(sendInputStateButton);
    }

    private void sendInputState() {
        try {
            DatagramSocket udpSocket = new DatagramSocket();
            InputState inputState = new InputState();
            inputState.setA(true);
            InputMessage networkMessage = new InputMessage(inputState);
            byte[] buf = new NetworkPackage(networkMessage).getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, lobby, PlatformConfiguration.UDP_PORT);
            udpSocket.send(packet);
            udpSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setReady() {
        this.controllerConnections.forEach(controllerConnection -> controllerConnection.setReady());


    }

    private void requestConnection() {
        if (this.lobby == null) {
            this.connectionResultTextField.setText("Keine lobby");
        } else {
            this.controllerConnections.add(new ControllerConnection(this));
        }

    }

    public void receiveBroadcast() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(PlatformConfiguration.UDP_BROADCAST_PORT);
            socket.setSoTimeout(1000);
            byte[] buf = new byte[512];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            socket.receive(packet);
            NetworkPackage pack = NetworkPackage.fromBytes(buf);
            if (pack.getNetworkMessage() instanceof BroadcastMessage) {
                BroadcastMessage message = (BroadcastMessage) pack.getNetworkMessage();
                this.broadcastResultTextField.setText(message.toString());
                this.lobby = packet.getAddress();
            } else {
                throw new Exception("Wrong Message!");
            }

        } catch (SocketTimeoutException e) {
            this.broadcastResultTextField.setText("No Broadcast found");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    public static class ControllerConnection {

        private Thread thread;
        private Socket socket;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        public ControllerConnection(DummyController dummyController) {
            try {
                socket = new Socket(dummyController.lobby, PlatformConfiguration.TCP_PORT);
                socket.setSoTimeout(3000);
                this.outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(new NetworkPackage(new ConnectRequestMessage("Platzhalter")));
                inputStream = new ObjectInputStream(socket.getInputStream());
                NetworkPackage pack = (NetworkPackage) inputStream.readObject();
                if (pack.getNetworkMessage() instanceof ConnectResponseMessage) {
                    ConnectResponseMessage message = (ConnectResponseMessage) pack.getNetworkMessage();
                    dummyController.connectionResultTextField.setText(message.toString());
                } else {
                    throw new Exception("Wrong Message!");
                }

                thread = new Thread(() -> {
                    try {

                        while (socket.isConnected()) {
                            socket.setSoTimeout(1000);
                            try {
                                NetworkPackage networkPackage = (NetworkPackage) inputStream.readObject();
                                if (networkPackage.getNetworkMessage() instanceof StartGameMessage) {
                                    StartGameMessage message = (StartGameMessage) networkPackage.getNetworkMessage();
                                    dummyController.controllerFaceTextField.setText(message.getControllerFace().getFaceName());
                                } else if (networkPackage.getNetworkMessage() instanceof TournamentEndedMessage) {
                                    synchronized (dummyController.controllerFaceTextField) {
                                        dummyController.controllerFaceTextField.setText("Tournament ended");
                                    }
                                } else if (networkPackage.getNetworkMessage() instanceof ConnectResponseMessage) {
//                                    dummyController.controllerFaceTextField.setText("Tournament ended");
                                }
                            } catch (SocketTimeoutException e) {

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        dummyController.controllerConnections.remove(this);
                    }
                });
                thread.start();

            } catch (Exception e) {
                e.printStackTrace();
                dummyController.controllerConnections.remove(this);
            }
        }

        public void setReady() {
            try {
                outputStream.writeObject(new NetworkPackage(new SetReadyMessage(true)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
