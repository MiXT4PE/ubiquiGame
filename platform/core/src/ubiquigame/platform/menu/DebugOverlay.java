package ubiquigame.platform.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import ubiquigame.common.Player;
import ubiquigame.platform.PlatformImpl;
import ubiquigame.platform.helpers.ColorUtil;
import ubiquigame.platform.session.player.ControllerInternal;

public class DebugOverlay extends Container<Label> {
	private Label debugLabel;

	public DebugOverlay(Skin skin) {
		super();
		debugLabel = new Label("", skin);
		Container<Label> container = new Container<>(debugLabel);
		container.setBackground(ColorUtil.getSolidColor(new Color(Color.WHITE).add(0, 0, 0, -.5f)));
	}

	private void updateLabel() {
		String packetDebugString = "Packets/s:";
		for (Player p : PlatformImpl.getInstance().getPlayers()) {
			ControllerInternal controller = (ControllerInternal) p.getController();
			packetDebugString += "\n" + p.getName() + ": " + controller.getPacketsPerSecond();
		}
		debugLabel.setText(packetDebugString);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		updateLabel();
	}

}
