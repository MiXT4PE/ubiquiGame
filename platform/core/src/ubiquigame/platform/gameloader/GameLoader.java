package ubiquigame.platform.gameloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;

import ubiquigame.common.UbiquiGame;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.AbstractUbiquiGame;
import ubiquigame.platform.PlatformImpl;

public class GameLoader {
	private static final Logger LOGGER = LogManager.getLogger();
	private static Method addUrlMethod;
	{
		try {
			addUrlMethod = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
		} catch (NoSuchMethodException | SecurityException e) {
			LOGGER.catching(e);
			throw new RuntimeException(e);
		}
		addUrlMethod.setAccessible(true);
	}

	private Map<Path, String> mapJarGameClass = new HashMap<>();
	private List<Class<AbstractUbiquiGame>> loadedClasses = new ArrayList<>();

	private static final Pattern GAMES_CLASS_PATTERN = Pattern
			.compile("ubiquigame\\/games\\/[^\\/]+\\/[^\\/]+\\.class");

	public GameLoader() {
	}

	public void discover(Path dir) {
		List<Path> discoverJars = discoverJars(dir);
		LOGGER.info("Found the following jars:%s", //
				discoverJars.stream().map(path -> path.toFile().getName()).collect(Collectors.joining("\n")));
		for (Path jar : discoverJars) {
			Optional<String> findGameClass = findGameClass(jar);
			if (findGameClass.isPresent()) {
				mapJarGameClass.put(jar, findGameClass.get());
			} else {
				LOGGER.error("No suitable Main-Class found in game jar %s", jar.toFile().getName());
			}
		}

	}

	@SuppressWarnings("unchecked")
	public List<Class<AbstractUbiquiGame>> loadGames() {
		loadedClasses = new ArrayList<>();
		for (Entry<Path, String> loadable : mapJarGameClass.entrySet()) {
			try {
				addGameJar(loadable.getKey().toFile());

				Class<?> classToLoad = Class.forName(loadable.getValue());
				loadedClasses.add((Class<AbstractUbiquiGame>) classToLoad);
				LOGGER.debug("Main-Class %s successfully loaded.", classToLoad.getName());
			} catch (Exception e) {
				LOGGER.error(() -> String.format("Main-Class %s couldn't be loaded.", loadable.getValue()), e);
			}
		}

		return loadedClasses;

	}

	private static void addGameJar(File file) {
		try {
			addUrlMethod.invoke(ClassLoader.getSystemClassLoader(), new Object[] { file.toURI().toURL() });
			LOGGER.log(Level.DEBUG, "Game-Jar %s added to ClassLoader.", file.getName());
		} catch (Exception e) {
			LOGGER.error(() -> String.format("Game jar '%s' couldn't be added to system class loader.", file.getName()), e);
		}
	}

	public UbiquiGame createInstance(Class<AbstractUbiquiGame> classToInstanziate) throws InvocationTargetException {
		try {
			Constructor<AbstractUbiquiGame> constructor = classToInstanziate.getConstructor(UbiquiGamePlatform.class);
			return constructor.newInstance(PlatformImpl.getInstance());
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Class<AbstractUbiquiGame>> getLoadedClasses() {
		return loadedClasses;
	}

	private List<Path> discoverJars(Path dir) {
		BiPredicate<Path, BasicFileAttributes> predicated = (path, attr) -> {
			File f = path.toFile();
			return f.isFile() && f.getName().endsWith(".jar");
		};

		try {
			return Files.find(dir, 1, predicated, FileVisitOption.FOLLOW_LINKS).collect(Collectors.toList());
		} catch (IOException e) {
			LOGGER.catching(e);
			return Collections.emptyList();
		}
	}

	public Optional<String> findGameClass(Path jarFile) {
		ZipFile zf;
		try {
			zf = new ZipFile(jarFile.toFile());
			List<ZipEntry> possibleGameClasses = scanClassFiles(zf);
			String fistGameClass = getFistGameClass(possibleGameClasses, zf);
			return Optional.ofNullable(fistGameClass);
		} catch (IOException e) {
		}

		return Optional.empty();

	}

	private List<ZipEntry> scanClassFiles(ZipFile zf) {
		List<ZipEntry> names = new ArrayList<>();
		Enumeration<? extends ZipEntry> entries = zf.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			if (GAMES_CLASS_PATTERN.matcher(entry.getName()).matches()) {
				names.add(entry);
			}
		}
		return names;
	}

	private String getFistGameClass(List<ZipEntry> entries, ZipFile zf) {
		try {
			for (ZipEntry ze : entries) {
				InputStream is = zf.getInputStream(ze);
				ClassReader cr = new ClassReader(is);
				String fullqualifiedNameOfSuper = cr.getSuperName().replace('/', '.');
				if (AbstractUbiquiGame.class.getName().equals(fullqualifiedNameOfSuper)) {
					return cr.getClassName().replace('/', '.');
				}
			}
		} catch (IOException e) {
		}

		return null;
	}

}
