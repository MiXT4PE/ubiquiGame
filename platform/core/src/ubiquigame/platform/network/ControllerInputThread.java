package ubiquigame.platform.network;

import ubiquigame.common.controllerMessages.BroadcastMessage;
import ubiquigame.common.controllerMessages.InputMessage;
import ubiquigame.common.controllerMessages.NetworkPackage;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.session.ConnectionSession;
import ubiquigame.platform.session.player.ControllerInternal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.apache.logging.log4j.LogManager;

public class ControllerInputThread extends Thread {

	private boolean isAcceptingInput = false;
	private DatagramSocket socket;

	public ControllerInputThread() {
		this.setName("ControllerInputThread");
	}

	@Override
	public void run() {
		this.isAcceptingInput = true;
		try {
			this.socket = new DatagramSocket(PlatformConfiguration.UDP_PORT);

			while (true) {
				if (this.isAcceptingInput) {
					try {

						byte[] buf = new byte[512];
						DatagramPacket packet = new DatagramPacket(buf, buf.length);
						socket.receive(packet);

						NetworkPackage networkPackage = NetworkPackage.fromBytes(packet.getData());
						if (networkPackage.getNetworkMessage() instanceof InputMessage) {
							InputMessage message = (InputMessage) networkPackage.getNetworkMessage();
							ControllerInternal controllerInternal = ConnectionSession.getCurrent().getIpMapping()
									.get(packet.getAddress());
							if (controllerInternal != null) {
								controllerInternal.setInputState(message.getInputState());
							}

						} else if (networkPackage.getNetworkMessage() instanceof BroadcastMessage) {
							// Ignore broadcastMessages
						} else {
							LogManager.getLogger().warn(String.format("Received message object is not of type %s!", NetworkPackage.class.getName()));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					Thread.sleep(100);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
