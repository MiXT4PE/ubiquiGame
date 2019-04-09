package ubiquigame.platform.menu.gamelibrary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import ubiquigame.common.GameInfo;
import ubiquigame.common.UbiquiGame;
import ubiquigame.platform.helpers.Listener;
import ubiquigame.platform.menu.AbstractMenuScreen;

import java.util.List;

public class GameLibraryScreen extends AbstractMenuScreen {

	private Table tableGameButtons;

	private final static Texture MISSING_TEXTURE = new Texture(Gdx.files.internal("textureMissing.jpg"));

	private final int MAX_GAMES_PER_ROW = 4;

	public GameLibraryScreen(List<UbiquiGame> games) {
		super();
		createHeader(localize("chooseGame"));
		tableGameButtons = new Table();

		this.getRootTable().add(tableGameButtons).expand().center();
		tableGameButtons.row();

		for (int i = 0; i < games.size(); i++) {
			UbiquiGame game = games.get(i);
			GameInfo gameInfo = game.getGameInfo();
			Image thumbnail = gameInfo.getThumbnail();
			if (thumbnail == null) {
				thumbnail = new Image(MISSING_TEXTURE);
			}
			String gameTitle = gameInfo.getGameTitle();
			if (gameTitle == null) {
				gameTitle = game.getClass().getName();
			}

			GameTile gameTile = new GameTile(gameTitle, thumbnail, skin);

			gameTile.addListener(Listener.onClicked(e -> menuManager.showDescriptionScreen(game)));
			Cell<GameTile> cell = tableGameButtons.add(gameTile).width(200).height(200).padLeft(25).padTop(25);
			if ((i + 1) % MAX_GAMES_PER_ROW == 0) {
				cell.padRight(25);
				tableGameButtons.row();
			}
		}
		createFooter();
		createFooterButton(localize("cancel"), Align.right).getActor().addListener(Listener.onClicked(e -> menuManager.goBack()));
	}

}
