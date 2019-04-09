package ubiquigame.platform.menu;

import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGame;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.platform.helpers.Listener;
import ubiquigame.platform.menu.highscore.ScoreEntry;
import ubiquigame.platform.session.GameSession;

public class GameResultScoreScreen extends AbstractMenuScreen {

	private Table content;

	public GameResultScoreScreen(GameOverMessage message) {
		UbiquiGame currentGame = GameSession.getCurrent().getCurrentGame();
		createHeader(localize("standing"), currentGame.getGameInfo().getGameTitle());

		content = new Table();

		List<Player> playerRanking = message.getPlayerRanking();
		for (int i = 0; i < playerRanking.size(); i++) {
			Player player = playerRanking.get(i);
			ScoreEntry scoreEntry = new ScoreEntry(player.getName(), player.getAvatar(), message.getScore(player),
					i + 1, skin, BG_BOX);
			content.add(scoreEntry).growX().padBottom(10);
			content.row();
		}

		rootTable.add(content).width(Value.percentWidth(.8f, rootTable)).center().top().growX().expandY().padTop(100);
		rootTable.row();

		rootTable.add(new Label(localize("pressSpace")+"...", this.getSkin()));

		stage.addListener(Listener.onKeyDown(Input.Keys.SPACE, e -> menuManager.onGameResultScreenClose()));
	}
}
