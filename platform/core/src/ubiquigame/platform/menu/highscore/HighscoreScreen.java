package ubiquigame.platform.menu.highscore;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;

import ubiquigame.platform.database.dto.User;
import ubiquigame.platform.helpers.Listener;
import ubiquigame.platform.menu.AbstractMenuScreen;

/**
 * Presents the global highscore pulled from remote database
 * 
 */
public class HighscoreScreen extends AbstractMenuScreen {

	private Map<String, ScoreEntry> entryMap = new LinkedHashMap<>();

	private List<User> playerInfoList = Collections.emptyList();

	private TextField searchField;
	private ScrollPane scrollBoard;

	public HighscoreScreen(List<User> playerInfoList) {
		super();
		this.playerInfoList = playerInfoList;
		createUI();
	}

	private void createUI() {
		createHeader("Highscore");
		Table highscoreBoard = new Table();
		scrollBoard = new ScrollPane(highscoreBoard, skin);
		searchField = new TextField(localize("search"), skin);

		searchField.addListener(Listener.onKeyboardFocused(e -> searchField.selectAll()));
		searchField.addListener(Listener.onKeyDown(Input.Keys.ENTER, e -> searchPlayer()));

		TextButton searchButton = new TextButton(localize("search"), skin);
		searchButton.addListener(Listener.onClicked(e -> searchPlayer()));

		stage.addActor(searchButton);
		Table searchBar = new Table();
		searchBar.add(searchField).growX().padRight(6);
		searchBar.add(searchButton);
		scrollBoard.setScrollingDisabled(true, false);
		scrollBoard.setOverscroll(false, false);
		scrollBoard.setFadeScrollBars(false);
		int place = 1;
		for (User pinfo : playerInfoList) {
			ScoreEntry entry = new ScoreEntry(pinfo, place, skin, BG_BOX);
			entryMap.put(pinfo.getName(), entry);
			highscoreBoard.add(entry).growX().padBottom(5f);
			highscoreBoard.row();
			place++;
		}
		Table vg = new Table();

		vg.add(searchBar).left().top().growX().expandY().padBottom(10);
		vg.row();
		vg.add(scrollBoard).growX();

		rootTable.add(vg).width(Value.percentWidth(.8f, rootTable)).padTop(20).grow();
		createFooter();
		TextButton backButton = createFooterButton(localize("cancel"), Align.right).getActor();
		backButton.addListener(Listener.onClicked(e -> menuManager.goBack()));
	}

	private void searchPlayer() {
		String input = searchField.getText();
		Table table = entryMap.keySet().stream().filter(s -> s.toLowerCase().contains(input.toLowerCase())).findFirst()
				.map(key -> entryMap.get(key)).orElse(null);
		if (table != null) {
			Vector2 vector2 = new Vector2(0, 0);
			table.localToParentCoordinates(vector2);
			scrollBoard.scrollTo(0, vector2.y - scrollBoard.getHeight() + table.getHeight(), //
					table.getWidth(), vector2.y + table.getHeight(), //
					false, false);
		}
	}

}
