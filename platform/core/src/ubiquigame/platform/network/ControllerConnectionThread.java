package ubiquigame.platform.network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ubiquigame.common.ControllerFace;
import ubiquigame.common.controllerMessages.*;
import ubiquigame.platform.PlatformImpl;
import ubiquigame.platform.database.dto.User;
import ubiquigame.platform.session.ConnectionSession;
import ubiquigame.platform.session.player.ControllerInternal;
import ubiquigame.platform.session.player.PlayerInternal;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.apache.logging.log4j.LogManager;

public class ControllerConnectionThread extends Thread {

	private final Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private ControllerInternal controller;
	private PlayerInternal player;

	private static int i = 0;

	public ControllerConnectionThread(Socket socket) {
		this.socket = socket;
		i++;
		this.setName("ControllerConnectionThread" + i);
	}

	private Image downloadAvatar(String username) {
		Optional<User> playerInfo = PlatformImpl.getInstance().getDatabase().getUser(username);
		byte[] avatarData = playerInfo.map(p -> p.getAvatar()).orElse(new byte[0]);
		if (avatarData.length != 0) {
			try {
				Texture texture = new Texture(new Pixmap(avatarData, 0, avatarData.length));
				return new Image(texture);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new Image(PlatformImpl.getInstance().getAssets().getDefaultAvatar());

	}

	private PlayerInternal createPlayerInstance(ConnectRequestMessage conRequestMsg) {
		String username = conRequestMsg.getUsername();
		controller = new ControllerInternal();

		Deque<Image> avatarQueue = new ConcurrentLinkedDeque<Image>();
		try {
			Gdx.app.postRunnable(new Runnable() {
				@Override
				public void run() {
					avatarQueue.clear();
					avatarQueue.add(downloadAvatar(username));
				}
			});
			// block
			while (avatarQueue.isEmpty())
				Thread.yield();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new PlayerInternal(username, controller, avatarQueue.getLast());
	}

	private ConnectRequestMessage handshake() throws IOException, ClassNotFoundException {
		inputStream = new ObjectInputStream(socket.getInputStream());

		socket.setSoTimeout(3000);
		ConnectRequestMessage connectionMessage = (ConnectRequestMessage) ((NetworkPackage) inputStream.readObject())
				.getNetworkMessage();

		outputStream = new ObjectOutputStream(socket.getOutputStream());
		synchronized (outputStream) {
			outputStream.writeObject(new NetworkPackage(
					new ConnectResponseMessage(true, ConnectionSession.getCurrent().getPlayerCount())));
			outputStream.flush();
			outputStream.reset();
		}

		return connectionMessage;
	}

	private void handleMessage() throws ClassNotFoundException, IOException {
		try {
			socket.setSoTimeout(1000);
			NetworkPackage networkPackage = (NetworkPackage) inputStream.readObject();
			if (networkPackage.getNetworkMessage() instanceof SetReadyMessage) {
				SetReadyMessage message = (SetReadyMessage) networkPackage.getNetworkMessage();
				player.setReady(message.isReady());
			} else {
				LogManager.getLogger().warn(
						String.format("Received message object is not of type %s!", NetworkPackage.class.getName()));
			}
		} catch (SocketTimeoutException e) {

		} catch (SocketException e) {

		}
	}

	@Override
	public synchronized void start() {
		try {
			ConnectRequestMessage requestMsg = handshake();
			player = createPlayerInstance(requestMsg);
			ConnectionSession.getCurrent().addPlayer(player);
			ConnectionSession.getCurrent().getIpMapping().put(socket.getInetAddress(), controller);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.start();
	}

	@Override
	public void run() {
		try {

			while (!socket.isClosed()) {
				handleMessage();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			synchronized (ConnectionController.getInstance()) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Gdx.app.postRunnable(() -> {
					ConnectionSession.getCurrent().getIpMapping().remove(this.socket.getInetAddress());
					ConnectionController.getInstance().removeConnection(this);
				});

			}
		}
	}

	public void stopThread() {
		try {
			ConnectionSession.getCurrent().getIpMapping().remove(this.socket.getInetAddress());
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendStartGameMessage(ControllerFace controllerFace) {
		try {
			synchronized (outputStream) {
				this.outputStream.writeObject(new NetworkPackage(new StartGameMessage(controllerFace)));
				outputStream.flush();
				outputStream.reset();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendTournamentEndedMessage() {
		try {
			synchronized (outputStream) {
				this.outputStream.writeObject(new NetworkPackage(new TournamentEndedMessage()));
				outputStream.flush();
				outputStream.reset();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
