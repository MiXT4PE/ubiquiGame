package ubiquigame.platform.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import com.badlogic.gdx.utils.Scaling;

import ubiquigame.platform.PlatformImpl;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.helpers.Listener;

public class MainMenuScreen extends AbstractMenuScreen {

	private Table leftTable;
	private boolean hasShownError = false;
	private TextButton singlegame;
	private TextButton tournament;

	public MainMenuScreen() {
		super();
		Table content = new Table();
		leftTable = new Table();
		Table rightTable = new Table();
		Texture logo = new Texture("logo_white.png");

		singlegame = new TextButton(localize("SingleGame"), skin);
		addMenuButton(singlegame);
		tournament = new TextButton(localize("Tournament"), skin);
		addMenuButton(tournament);
		TextButton options = new TextButton(localize("Options"), skin);
		addMenuButton(options);
		TextButton highscore = new TextButton(localize("Highscore"), skin);
		addMenuButton(highscore);
		TextButton credits = new TextButton(localize("Credits"), skin);
		addMenuButton(credits);
		TextButton exit = new TextButton(localize("Exit"), skin);
		addMenuButton(exit);
		logo.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		Image logoImage = new Image(logo);
		logoImage.setScaling(Scaling.fit);
		rightTable.add(logoImage).width(600).top();// Rechte Seite

		content.add(leftTable).width(300).left().top();
		Value percentHeight = Value.percentHeight(.5f, content);
		content.add(logoImage).top().height(percentHeight).width(Value.percentHeight(.4f * 1.5f, content)).padLeft(200);
		rootTable.add(content).fill().expand();

		// Listeners
		singlegame.addListener(Listener.onClicked(e -> onClickSingleGame()));
		tournament.addListener(Listener.onClicked(e -> onClickTournament()));
		if (PlatformConfiguration.DEBUG_MODE) {
			this.getStage().addListener(Listener.onKeyDown(Input.Keys.SPACE, e -> onClickTournament()));
		}
		options.addListener(Listener.onClicked(e -> menuManager.showOptionsMenu()));
		highscore.addListener(Listener.onClicked(e -> menuManager.showHighscore()));
		credits.addListener(Listener.onClicked(e -> menuManager.showCredits()));
		exit.addListener(Listener.onClicked(e -> menuManager.requestPlatformExit()));

	}

	private void addMenuButton(TextButton button) {
		leftTable.add(button).height(60).space(10).growX();
		leftTable.row();
	}

	@Override
	public void show() {
		super.show();
		boolean noGames = PlatformImpl.getInstance().getGameManager().getGames().isEmpty();
		if (noGames && !this.hasShownError) {
			showDialogNoGames();
		}
		singlegame.setDisabled(noGames);
		tournament.setDisabled(noGames);
	}

	private void showDialogNoGames() {
		hasShownError = true;
		Dialog dialog = new Dialog("Warning", PlatformImpl.getInstance().getAssets().getSkin()) {
			@Override
			protected void result(Object object) {
				if ((boolean) object == false) {
					Gdx.app.exit();
				}
			}
		};
		dialog.text("No games were loaded.");
		dialog.button("Exit Platform", false);
		dialog.button("Ok", true);
		dialog.show(this.stage);
	}

	private void onClickSingleGame() {
		if (PlatformImpl.getInstance().getGameManager().getGames().isEmpty()) {
			showDialogNoGames();
		} else {
			menuManager.startSingleGameSession();
		}
	}

	private void onClickTournament() {
		if (PlatformImpl.getInstance().getGameManager().getGames().isEmpty()) {
			showDialogNoGames();
		} else {
			menuManager.startTournamentSession();
		}
	}
}
