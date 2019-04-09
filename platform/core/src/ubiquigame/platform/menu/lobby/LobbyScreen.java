package ubiquigame.platform.menu.lobby;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.helpers.Listener;
import ubiquigame.platform.menu.AbstractMenuScreen;
import ubiquigame.platform.network.ConnectionController;
import ubiquigame.platform.session.ConnectionSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Lobby to Connect to
 */
public class LobbyScreen extends AbstractMenuScreen {

	private static final int TIME_TO_WAIT = 5;
	private List<PlayerSlot> playerConnectionActors;;
	private Label footer;
	private float timer = 0f;

	public LobbyScreen() {
		super();

		createHeader(localize("multiplayerLobby", PlatformConfiguration.PLATFORM_NAME), localize("connectSmartphone"));

		Table playerConnectionTable = new Table();
		this.playerConnectionActors = new ArrayList<>();
		for (int i = 0; i < 4; i++) {
			playerConnectionActors.add(new PlayerSlot(skin, i));
			playerConnectionTable.add(playerConnectionActors.get(i))
					.width(Value.percentWidth(0.2f, this.getRootTable()))
					.height(Value.percentWidth(0.2f, this.getRootTable()))
					.space(Value.percentWidth(0.05f, this.getRootTable()));
		}

		footer = new Label("", skin);

		this.getRootTable().add(playerConnectionTable).expandY();
		this.getRootTable().row();
		this.getRootTable().add(footer);
		createFooter();
		TextButton cancelButton = createFooterButton(localize("cancel"), Align.right).getActor();
		cancelButton.addListener(Listener.onClicked(event -> menuManager.goBack()));
		ConnectionSession session = ConnectionSession.getCurrent();
		this.stage.addListener(Listener.onKeyDown(Input.Keys.SPACE, inputEvent -> {
			if (!(session.getPlayerCount() < 2 || !session.allPlayersReady())) {
				this.timer = 5f;
			} else {
				if (PlatformConfiguration.DEBUG_MODE)
					this.playerConnectionActors.forEach(playerSlot -> playerSlot.onClick());
			}
		}));
	}

	@Override
	public void render(float delta) {

		ConnectionSession session = ConnectionSession.getCurrent();
		if (session.getPlayerCount() < 2 || !session.allPlayersReady()) {
			this.timer = 0f;
		} else {
			this.timer += delta;
		}

		if (session.getPlayerCount() >= 2) {

			if (session.allPlayersReady()) {
				this.footer.setText(localize("startGameIn") + (TIME_TO_WAIT - (int) timer) + "...");
			} else {
				this.footer.setText(localize("ready"));
			}
		} else {
			this.footer.setText(localize("morePlayers"));
		}

		if (timer >= 5f) {
			menuManager.onLobbyReady();
		}

		super.render(delta);

	}

	@Override
	public void show() {
		super.show();
		ConnectionController.getInstance().startLobby();
	}

	@Override
	public void hide() {
		super.hide();
		ConnectionController.getInstance().stopLobby();
	}
}
