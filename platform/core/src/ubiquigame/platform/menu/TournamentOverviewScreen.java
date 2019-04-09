package ubiquigame.platform.menu;

import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

import ubiquigame.common.Player;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.platform.helpers.Listener;
import ubiquigame.platform.menu.highscore.ScoreEntry;
import ubiquigame.platform.session.tournament.Score;
import ubiquigame.platform.session.tournament.TournamentSession;

public class TournamentOverviewScreen extends AbstractMenuScreen {

	Label top;
	Label gamesPlayed;
	Table content;
	Label[] name;
	HorizontalGroup[] crowns;
	Label[] points;
	Label pressSpace;

	protected void initializeUI() {
		int total = TournamentSession.getCurrent().getTotalRounds();
		int current = TournamentSession.getCurrent().getCurrentRound();

		String subtitle = String.format("%d/%d"+" "+localize("roundsPlayed"), current, total);
		String headerKey = current == total ? "lastStanding" : "currentStanding";
		createHeader(localize(headerKey), subtitle);

		pressSpace = new Label(localize("pressSpace"), this.getSkin());
		content = new Table();

		Score totalScore = TournamentSession.getCurrent().getTotalScore();

		List<Player> playersRanked = totalScore.getPlayersRanked();
		for (int i = 0; i < playersRanked.size(); i++) {
			Player player = playersRanked.get(i);
			ScoreEntry se = new ScoreEntry(player.getName(), player.getAvatar(), totalScore.get(player), i + 1,
					this.getSkin(), BG_BOX);
			content.add(se).padBottom(10).growX();
			content.row();
		}
		rootTable.add(content).width(Value.percentWidth(.8f, rootTable)).center().top().growX().expandY().padTop(100);
		rootTable.row();
		rootTable.add(pressSpace).padTop(80);
		stage.addListener(Listener.onKeyDown(Input.Keys.SPACE, e -> {
			menuManager.onGameOverviewClosed();
		}));
	}

	public TournamentOverviewScreen(GameOverMessage gameOverMessage) {
		super();
		this.initializeUI();
	}
}
