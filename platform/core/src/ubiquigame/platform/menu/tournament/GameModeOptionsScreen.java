package ubiquigame.platform.menu.tournament;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import ubiquigame.platform.helpers.Listener;
import ubiquigame.platform.menu.AbstractMenuScreen;
import ubiquigame.platform.session.tournament.TournamentSession;
import ubiquigame.platform.session.tournament.gamemode.AllGamesGameMode;
import ubiquigame.platform.session.tournament.gamemode.CustomGameMode;
import ubiquigame.platform.session.tournament.gamemode.RandomGameMode;

public class GameModeOptionsScreen extends AbstractMenuScreen {

	private AbstractGameModeOptionsPartial<?> optionsPartial;

	public GameModeOptionsScreen() {
		super();
		createHeader(localize("gameModeOptions"));
		TournamentSession session = TournamentSession.getCurrent();

		optionsPartial = null;
		switch (session.getGamemode()) {
		case ALLGAMES:
			optionsPartial = new AllGamesOptionsPartial(session.getGameModeStrategy(AllGamesGameMode.class), skin);
			break;
		case CUSTOM:
			optionsPartial = new CustomGameOptionsPartial(session.getGameModeStrategy(CustomGameMode.class), skin);
			break;
		case RANDOM:
			optionsPartial = new RandomGameOptionsPartial(session.getGameModeStrategy(RandomGameMode.class), skin);
			break;

		}
		Table content = new Table();
		content.add(optionsPartial).padTop(100);
		content.row();
		rootTable.add(content).padTop(20).growY().padBottom(20);
		// buttonbar
		createFooter();

		createFooterButton(localize("cancel"), Align.right).getActor()
				.addListener(Listener.onClicked(e -> menuManager.goBack()));
		createFooterButton(localize("continue"), Align.right).getActor()
				.addListener(Listener.onClicked(e -> continueSession()));

	}

	private void continueSession() {
		optionsPartial.apply();
		menuManager.showLobbyScreen();
	}

}
