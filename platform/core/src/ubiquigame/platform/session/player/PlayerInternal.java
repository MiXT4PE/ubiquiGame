package ubiquigame.platform.session.player;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ubiquigame.common.Controller;
import ubiquigame.common.Player;

public class PlayerInternal implements Player {

	private String name;
	private ControllerInternal controller;
	private Image avatar;
	
	private boolean isReady;

	public PlayerInternal() {

	}

	public PlayerInternal(String name, ControllerInternal controller, Image avatar) {
		this.name = name;
		this.controller = controller;
		this.avatar = avatar;

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Controller getController() {
		return controller;
	}

	@Override
	public Image getAvatar() {
		return avatar;
	}

	public boolean isConnected() {
		return controller.isConnected();
	}
	
	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
		
	}

}
