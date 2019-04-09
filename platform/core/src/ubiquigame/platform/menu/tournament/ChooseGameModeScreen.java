package ubiquigame.platform.menu.tournament;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.helpers.Listener;
import ubiquigame.platform.menu.AbstractMenuScreen;
import ubiquigame.platform.mocks.Mocks;
import ubiquigame.platform.session.tournament.TournamentSession;
import ubiquigame.platform.session.tournament.gamemode.Gamemode;

public class ChooseGameModeScreen extends AbstractMenuScreen {

	private Label gameModeDescription;

	private Map<Gamemode, TextButton> gamemodeButtonMap = new HashMap<>();

	public ChooseGameModeScreen() {
		super();
		createHeader(localize("Tournament"), localize("chooseGamemode"));
		Table content = new Table();

		Arrays.stream(Gamemode.values()).forEach(this::createGameModeButton);

		gameModeDescription = new Label(Mocks.getDummyGames().get(0).getGameInfo().getDescription(), skin);
		gameModeDescription.setWrap(true);
		content.add(gamemodeButtonMap.get(Gamemode.ALLGAMES)).height(200).width(300);
		content.add(gamemodeButtonMap.get(Gamemode.RANDOM)).height(200).width(300).pad(0, 10, 0, 10);
//		content.add(gamemodeButtonMap.get(Gamemode.CUSTOM)).height(200).width(300);
		content.row();
		content.add(new Label(localize("description"), skin)).left().padTop(20);
		content.row();
		content.add(gameModeDescription).colspan(3).left().fillX().padTop(5).expandY().top();
		content.row();

		rootTable.add(content).padTop(20).growY();

		createFooter();
		TextButton showGamesButton = createFooterButton(localize("showGames"), Align.left).getActor();
		TextButton backButton = createFooterButton(localize("cancel"), Align.right).getActor();
		TextButton continueButton = createFooterButton(localize("continue"), Align.right).getActor();

		showGamesButton.addListener(Listener.onClicked(e -> menuManager.showGameLibraryScreen()));
		backButton.addListener(Listener.onClicked(e -> menuManager.goBack()));
		continueButton.addListener(Listener.onClicked(e -> menuManager.showGameModeOptionsScreen()));
		if(PlatformConfiguration.DEBUG_MODE){
			this.getStage().addListener(Listener.onKeyDown(Input.Keys.SPACE, e -> menuManager.showGameModeOptionsScreen()));
		}
	}

	private void createGameModeButton(Gamemode gamemode) {
		TextButton button = new TextButton(gamemode.getInfo().getName(), skin, "filled");
		gamemodeButtonMap.put(gamemode, button);
		button.addListener(Listener.onClicked(e -> TournamentSession.getCurrent().setGameMode(gamemode)));
	}

	@Override
	public void render(float delta) {
		updateGamemodeButtons();
		Gamemode gamemode = TournamentSession.getCurrent().getGamemode();
		gameModeDescription.setText(gamemode.getInfo().getDescription());
		super.render(delta);
	}

	private void updateGamemodeButtons() {
		Gamemode gamemode = TournamentSession.getCurrent().getGamemode();
		gamemodeButtonMap.get(gamemode);
		for (TextButton b : gamemodeButtonMap.values()) {
			b.setChecked(b.equals(gamemodeButtonMap.get(gamemode)));
		}

	}
}
