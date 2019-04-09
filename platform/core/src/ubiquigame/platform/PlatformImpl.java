package ubiquigame.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.I18NBundle;

import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.common.impl.ScreenDimension;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.database.UbiquiGameDB;
import ubiquigame.platform.menu.GameRunnerScreen;
import ubiquigame.platform.session.ConnectionSession;

public class PlatformImpl extends Game implements UbiquiGamePlatform {
	private static PlatformImpl instance;
	private final PlatformConfiguration config;

	public static final String CONFIGURATION_FILE_PATH = "platform-config.json";

	private List<Runnable> runnablesOnCreate = new ArrayList<>();

	private UbiquiGameDB database;

	private MenuManager menuManager;
	private GameManager gameManager;
	private PlatformAssets assets;
	private ExecutorService executorService;
	private I18NBundle myBundle;

	private PlatformImpl() {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		config = new PlatformConfiguration(CONFIGURATION_FILE_PATH);
		assets = new PlatformAssets();
		database = new UbiquiGameDB();
	}

	public static PlatformImpl getInstance() {
		if (instance == null)
			instance = new PlatformImpl();
		return instance;
	}

	@Override
	public void create() {
		assets.load();
		gameManager = new GameManager();
		menuManager = new MenuManager();

		setLocalization(PlatformConfiguration.LANGUAGE);
	
		FontSuite.generate();
		menuManager.showMainMenu();
		runnablesOnCreate.forEach(Runnable::run);
		runnablesOnCreate = null;
	}

	public void onCreate(Runnable runnable) {
		runnablesOnCreate.add(runnable);
	}

	
	
	@Override
	public void render() {

		menuManager.update();
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}

	@Override
	public void dispose() {
		config.save();
	}

	@Override
	public void resize(int width, int height) {
		PlatformConfiguration.WINDOW_WIDTH = Gdx.graphics.getWidth();
		PlatformConfiguration.WINDOW_HEIGHT = Gdx.graphics.getHeight();
		super.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public Player[] getPlayers() {

		return ConnectionSession.getCurrent().getPlayers();
	}

	@Override
	public void notifyCurrGameOver(GameOverMessage message) {
		if (getScreen() instanceof GameRunnerScreen) {
			((GameRunnerScreen) getScreen()).gameover();
		}
		menuManager.showResultScoreScreen(message);
	}

	@Override
	public ScreenDimension getScreenDimension() {
		return new ScreenDimension(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public PlatformAssets getAssets() {
		return assets;
	}

	/**
	 * called when db is unreachable
	 */
	public void notifyDBUnreachable() {

	}

	public UbiquiGameDB getDatabase() {
		return database;
	}

	public MenuManager getMenuManager() {
		return menuManager;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	@Override
	public float getMusicVolume() {
		return (PlatformConfiguration.AUDIO_MUSIC_VOLUME / 100f) * (PlatformConfiguration.AUDIO_MASTER_VOLUME / 100f);
	}

	@Override
	public float getSoundVolume() {
		return (PlatformConfiguration.AUDIO_SOUND_VOLUME / 100f) * (PlatformConfiguration.AUDIO_MASTER_VOLUME / 100f);
	}

	public void setLocalization(String localeKey) {
		FileHandle baseFileHandle = Gdx.files.internal("languages/platform");
		Locale locale = new Locale(localeKey);
		myBundle = I18NBundle.createBundle(baseFileHandle, locale, "UTF-8");
		I18NBundle.setExceptionOnMissingKey(false);
	}

	public I18NBundle getLocalization() {
		return myBundle;
	}

}
