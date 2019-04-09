package ubiquigame.platform.config;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;

import com.electronwill.nightconfig.core.file.FileConfig;
import ubiquigame.common.constants.NetworkConfiguration;

/**
 * The configuration of the platform with its default properties.
 *
 */
public class PlatformConfiguration {

	// define additional properties here, notice: must be annotated

	public static String WINDOW_TITLE = "ubiquiGame";

	@PersistProperty()
	public static String GAMES_PATH = "games/";

	@PersistProperty()
	public static String LANGUAGE = "de";

	@PersistProperty()
	public static boolean DEBUG_MODE = Boolean.getBoolean("bDebugMode");

	// Graphics
	@PersistProperty(category = "graphics")
	public static int WINDOW_WIDTH = 1920;

	@PersistProperty(category = "graphics")
	public static int WINDOW_HEIGHT = 1080;

	@PersistProperty(category = "graphics")
	public static boolean WINDOW_FULLSCREEN = true;

	// Networking
	@PersistProperty(category = "network")
	public static int TCP_PORT = NetworkConfiguration.tcpPort;

	@PersistProperty(category = "network")
	public static int UDP_PORT = NetworkConfiguration.udpPort;

	@PersistProperty(category = "network")
	public static int UDP_BROADCAST_PORT = NetworkConfiguration.udpBroadcastPort;

	@PersistProperty(category = "network")
	public static String PLATFORM_NAME = System.getProperty("user.name");

	// Database

	@PersistProperty(category = "api")
	public static String API_HOST = "https://heess.me";
	@PersistProperty(category = "api")
	public static String API_URI = "/external/php";
	@PersistProperty(category = "audio")
	// Audio

	public static int AUDIO_MASTER_VOLUME = 100;
	@PersistProperty(category = "audio")
	public static int AUDIO_MUSIC_VOLUME = 100;
	@PersistProperty(category = "audio")
	public static int AUDIO_SOUND_VOLUME = 100;

	// end of config fields
	private FileConfig config;

	private List<Field> persistedFields = Collections.emptyList();

	public PlatformConfiguration(String configPath) {
		try {
			load(configPath);
		} catch (Exception e) {
			LogManager.getLogger().error("Couldn't load config file! Using defaults.", e);
		}

	}

	private void load(String configPath) {
		config = FileConfig.builder(configPath).build();
		config.load();

		// find relevant fields
		persistedFields = Arrays.stream(this.getClass().getDeclaredFields())//
				.filter(f -> f.getAnnotation(PersistProperty.class) != null).collect(Collectors.toList());

		// load config fields from config file
		persistedFields.forEach(f -> loadFieldFromConfig(f.getType(), f));
	}

	private <T> void loadFieldFromConfig(Class<T> clazz, Field f) {
		@SuppressWarnings("unchecked")
		T value = (T) config.get(constantNameToProperty(f));
		if (value == null)
			return;
		try {
			f.set(this, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	private String constantNameToProperty(Field f) {
		return f.getAnnotation(PersistProperty.class).category() + "." + f.getName().toLowerCase().replace('_', '-');
	}

	public void save() {
		persistedFields.forEach(this::saveFieldToConfig);
		config.save();
	}

	private <T> void saveFieldToConfig(Field f) {
		try {
			config.set(constantNameToProperty(f), f.get(this));
		} catch (IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
	}
}
