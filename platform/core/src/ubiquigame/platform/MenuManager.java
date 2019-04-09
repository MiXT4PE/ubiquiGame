
package ubiquigame.platform;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.Callable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGame;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.platform.database.UbiquiGameDB;
import ubiquigame.platform.database.dto.User;
import ubiquigame.platform.helpers.AsyncOperation;
import ubiquigame.platform.menu.CreditsScreen;
import ubiquigame.platform.menu.GameResultScoreScreen;
import ubiquigame.platform.menu.GameRunnerScreen;
import ubiquigame.platform.menu.LoadingScreen;
import ubiquigame.platform.menu.MainMenuScreen;
import ubiquigame.platform.menu.OptionsModel;
import ubiquigame.platform.menu.OptionsScreen;
import ubiquigame.platform.menu.SlotMachineScreen;
import ubiquigame.platform.menu.TournamentOverviewScreen;
import ubiquigame.platform.menu.TutorialScreen;
import ubiquigame.platform.menu.gamelibrary.GameDescriptionScreen;
import ubiquigame.platform.menu.gamelibrary.GameLibraryScreen;
import ubiquigame.platform.menu.highscore.HighscoreScreen;
import ubiquigame.platform.menu.lobby.LobbyScreen;
import ubiquigame.platform.menu.tournament.ChooseGameModeScreen;
import ubiquigame.platform.menu.tournament.GameModeOptionsScreen;
import ubiquigame.platform.network.ConnectionController;
import ubiquigame.platform.session.ConnectionSession;
import ubiquigame.platform.session.GameSession;
import ubiquigame.platform.session.SingleGameSession;
import ubiquigame.platform.session.tournament.Score;
import ubiquigame.platform.session.tournament.TournamentSession;
import ubiquigame.platform.session.tournament.gamemode.Gamemode;
import ubiquigame.platform.session.tournament.gamemode.RandomGameMode;

public class MenuManager {

	private Screen currentScreen;
	private final PlatformImpl platform;
	private AsyncOperation<?> asyncOperation;
	private static final LoadingScreen LOADING_SCREEN = new LoadingScreen();
	private Stack<Screen> screenStack = new Stack<>();

	public MenuManager() {
		platform = PlatformImpl.getInstance();
	}

	public Screen getCurrentScreen() {
		return currentScreen;
	}

	public void update() {

		if (asyncOperation != null) {
			if (asyncOperation.isDone()) {
				asyncOperation.doLast();
				screenStack.push(currentScreen);
				platform.setScreen(currentScreen);
				asyncOperation = null;
			}
			return;
		}

		if (!currentScreen.equals(platform.getScreen())) {
			screenStack.push(currentScreen);
			platform.setScreen(currentScreen);
			return;
		}
	}

	public void showHighscore() {
		platform.setScreen(LOADING_SCREEN);
		Callable<List<User>> callable = () -> PlatformImpl.getInstance().getDatabase().getAllUsers();

		asyncOperation = AsyncOperation.create(callable).onResult(list -> {
			HighscoreScreen highscore = new HighscoreScreen(list);
			currentScreen = highscore;
		});
		asyncOperation.execute();
	}

	public void showMainMenu() {
		MainMenuScreen mainmenu = new MainMenuScreen();
		screenStack.clear();
		currentScreen = mainmenu;
	}

	public void showDescriptionScreen(UbiquiGame game) {
		GameDescriptionScreen description = new GameDescriptionScreen(game);
		if (!GameSession.getCurrent().isTournament())
			description = description.withPlayButton();
		currentScreen = description;
	}

	public void showGameLibraryScreen() {
		GameLibraryScreen selection = new GameLibraryScreen(platform.getGameManager().getGames());
		currentScreen = selection;
	}

	public void showResultScoreScreen(GameOverMessage message) {
		if (GameSession.getCurrent().isTournament()) {
			Score score = new Score(ConnectionSession.getCurrent().getPlayers());
			for (Player player : message.getPlayerRanking()) {
				score.set(player, message.getScore(player));
			}
			TournamentSession.getCurrent().setLastGameScore(score);
		}
		GameResultScoreScreen result = new GameResultScoreScreen(message);
		currentScreen = result;
	}

	public void onGameResultScreenClose() {
		if (!GameSession.getCurrent().isTournament()) {
			showMainMenu();
			ConnectionController.getInstance().sendTournamentEnded();
			return;
		}

		TournamentOverviewScreen overview = new TournamentOverviewScreen(null);
		currentScreen = overview;
	}

	public void showLobbyScreen() {
		LobbyScreen lobbyScreen = new LobbyScreen();
		currentScreen = lobbyScreen;
	}

	public void showChooseGameModeScreen() {
		ChooseGameModeScreen gameMode = new ChooseGameModeScreen();
		currentScreen = gameMode;
	}

	public void showGameModeOptionsScreen() {
		if (TournamentSession.getCurrent().getGamemode() == Gamemode.ALLGAMES) {
			showLobbyScreen();
		} else {
			GameModeOptionsScreen gameModeOptions = new GameModeOptionsScreen();
			currentScreen = gameModeOptions;
		}

	}

	public void nextRound() {
		TournamentSession ts = (TournamentSession) GameSession.getCurrent();
		if (ts.getGamemode() == Gamemode.RANDOM) {
			showSlotMachine();
			return;
		}
		ts.getNextGame();

		showTutorialScreen();
	}

	public void showTutorialScreen() {
		UbiquiGame currentGame = GameSession.getCurrent().getCurrentGame();
		TutorialScreen tutorialScreen = new TutorialScreen(currentGame.getGameInfo());
		currentScreen = tutorialScreen;
	}

	public void startGame() {
		UbiquiGame currentGame = GameSession.getCurrent().getCurrentGame();
		UbiquiGame gameToPlay = platform.getGameManager().createNewInstance(currentGame);
		GameRunnerScreen gameRunnerScreen = new GameRunnerScreen(gameToPlay);
		gameRunnerScreen.create();
		currentScreen = gameRunnerScreen;
	}

	public void requestPlatformExit() {
		Gdx.app.exit();
	}

	public void showOptionsMenu() {
		OptionsScreen options = new OptionsScreen();
		currentScreen = options;
	}
	
	public void showOptionsMenu(OptionsModel prevOptions) {
		OptionsScreen options = new OptionsScreen(prevOptions);
		currentScreen = options;
	}

	public void startTournamentSession() {
		if (ConnectionSession.getCurrent() == null) {
			ConnectionSession.create();
		}
		GameSession.createTournament();
		showChooseGameModeScreen();
	}

	public void startSingleGameSession() {
		// if no connection session exists, create one
		if (ConnectionSession.getCurrent() == null) {
			ConnectionSession.create();
		}
		GameSession.createSingleGame();
		showGameLibraryScreen();
	}

	public void goBack() {
		if (screenStack.size() < 2)
			return;
		screenStack.pop(); // pop current screen
		Screen lastScreen = screenStack.pop();

		// Additional pop for TutorialScreens so game does not just start again
		if (lastScreen instanceof TutorialScreen) {
			lastScreen = screenStack.pop();
		}
		currentScreen = lastScreen; // pop last screen set as current
	}

	/**
	 * 
	 * @param game the game chosen to play
	 */
	public void playGame(UbiquiGame game) {
		SingleGameSession gameSession = GameSession.getSingleGameSession();
		gameSession.setGameOrder(game);

		showLobbyScreen();

	}

	public void onGameOverviewClosed() {
		TournamentSession ts = (TournamentSession) GameSession.getCurrent();

		if (ts.hasNextGame()) {
			nextRound();
		} else {
			tournamentEnded();
		}
	}

	private void tournamentEnded() {
		UbiquiGameDB database = PlatformImpl.getInstance().getDatabase();
		Score totalScore = TournamentSession.getCurrent().getTotalScore();
		database.updateHighscore(totalScore);
		ConnectionController.getInstance().sendTournamentEnded();
		showMainMenu();
	}

	public void onLobbyReady() {
		if (GameSession.getCurrent().isTournament() && TournamentSession.getCurrent().getTotalScore() == null) {
			TournamentSession.getCurrent().resetTotalScore();
		}
		if (GameSession.getCurrent().isTournament()) {
			nextRound();
		} else {
			showTutorialScreen();
		}

	}

	public void showSlotMachine() {
		RandomGameMode randomGamemode = TournamentSession.getCurrent().getGameModeStrategy(RandomGameMode.class);
		SlotMachineScreen screen = new SlotMachineScreen(randomGamemode);
		currentScreen = screen;

	}

	public void showCredits() {
		currentScreen = new CreditsScreen();
	}
}