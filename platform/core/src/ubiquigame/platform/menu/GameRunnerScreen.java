package ubiquigame.platform.menu;

import java.util.Arrays;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGame;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.platform.PlatformImpl;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.helpers.Listener;
import ubiquigame.platform.network.ConnectionController;
import ubiquigame.platform.session.GameSession;
import ubiquigame.platform.session.SingleGameSession;
import ubiquigame.platform.session.player.ControllerInternal;
import ubiquigame.platform.session.player.PlayerInternal;
import ubiquigame.platform.session.tournament.Score;
import ubiquigame.platform.session.tournament.TournamentSession;

/**
 * Screen in which a UbiquiGame lives
 *
 */
public class GameRunnerScreen extends AbstractMenuScreen {

	private UbiquiGame ubiquiGame;
	private boolean isStarted = true;
	private boolean gameover = false;
	private boolean forceQuit = false;
	private DebugOverlay debugOverlay;

	public GameRunnerScreen(UbiquiGame ubiquiGame) {
		super();
		this.ubiquiGame = ubiquiGame;
		initializeUI();
	}

	protected void initializeUI() {
		rootTable.setBackground((Drawable) null);
		if (PlatformConfiguration.DEBUG_MODE) {
			debugOverlay = new DebugOverlay(skin);
			rootTable.add(debugOverlay);
		}
		stage.addListener(Listener.onKeyDown(Input.Keys.F3, e -> forceQuitGame()));
	}

	private void forceQuitGame() {
		forceQuit = true;
	}

	@Override
	protected void toggleDebugMode() {
		super.toggleDebugMode();
		if (PlatformConfiguration.DEBUG_MODE) {
			debugOverlay = new DebugOverlay(skin);
			rootTable.add(debugOverlay);
		} else {
			rootTable.removeActor(debugOverlay);
		}
	}

	@Override
	public void render(float delta) {
		if (forceQuit) {
			GameOverMessage gameOverMessage;
			if(GameSession.getCurrent().isTournament()) {
				Score totalScore = TournamentSession.getCurrent().getTotalScore();
				gameOverMessage = totalScore.toGameOverMessage();
			}else {
				Player[] players = GameSession.getCurrent().getConnectionSession().getPlayers();
				Score score = new Score(players);
				Arrays.asList(players).forEach(player -> score.set(player, 0));
				gameOverMessage = score.toGameOverMessage();
			}
			stop();
			ubiquiGame.gameover(gameOverMessage);
			return;
		}
		for (Player p : PlatformImpl.getInstance().getPlayers()) {
			ControllerInternal controller = (ControllerInternal) p.getController();
			controller.update();
		}
		if (this.isStarted) {
			ubiquiGame.update(delta);
		}
		if (gameover)
			return;
		ubiquiGame.render(delta);

		this.stage.act(delta);
		this.stage.draw();
	}

	public void create() {
		ubiquiGame.create();
		ConnectionController.getInstance().sendGameStarted(this.ubiquiGame.getControllerFace());
	}

	public void stop() {
		this.isStarted = false;
	}

	public void gameover() {
		gameover = true;
	}

	public void start() {
		this.isStarted = true;
	}

}