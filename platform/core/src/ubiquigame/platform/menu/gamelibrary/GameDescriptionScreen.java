package ubiquigame.platform.menu.gamelibrary;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import ubiquigame.common.GameInfo;
import ubiquigame.common.UbiquiGame;
import ubiquigame.platform.helpers.Listener;
import ubiquigame.platform.menu.AbstractMenuScreen;

/**
 * Description Screen for a given Game
 */
public class GameDescriptionScreen extends AbstractMenuScreen {

	private UbiquiGame game;

	public GameDescriptionScreen(UbiquiGame game) {
		super();
		this.game = game;
		GameInfo gameInfo = game.getGameInfo();
		createHeader(gameInfo.getGameTitle());

		Table content = new Table();
		Label description = new Label(gameInfo.getDescription(), this.getSkin());
		description.setWrap(true);

		content.add(gameInfo.getThumbnail()).width(200).height(200).padTop(50);
		content.add(description).width(500).padLeft(50).padRight(50);
		rootTable.add(content).expandY();

		createFooter();
		TextButton back = createFooterButton(localize("cancel"), Align.right).getActor();
		back.addListener(Listener.onClicked(e -> menuManager.goBack()));
	}

	public GameDescriptionScreen withPlayButton() {
		TextButton play = createFooterButton(localize("play"), Align.right).getActor();
		play.addListener(Listener.onClicked(e -> menuManager.playGame(game)));
		return this;
	}
}
