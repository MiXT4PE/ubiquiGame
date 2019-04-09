package ubiquigame.platform;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ubiquigame.common.GameInfo;
import ubiquigame.common.UbiquiGame;
import ubiquigame.common.impl.AbstractUbiquiGame;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.gameloader.GameLoader;
import ubiquigame.platform.mocks.Mocks;
import ubiquigame.platform.session.GameSession;

public class GameManager {

    private static final Logger LOGGER = LogManager.getLogger();
    private GameLoader gameLoader;

    private List<UbiquiGame> availableGames = new ArrayList<>();

    public GameManager() {
        gameLoader = new GameLoader();
        gameLoader.discover(Paths.get(PlatformConfiguration.GAMES_PATH));
        gameLoader.loadGames();

        gameLoader.getLoadedClasses().forEach(this::preloadGame);
        //availableGames.addAll(Mocks.getDummyGames());

        // make availableGames unmodifiable
        availableGames = Collections.unmodifiableList(availableGames);
    }

    @SuppressWarnings("unchecked")
    public UbiquiGame createNewInstance(UbiquiGame game) {
        try {
            return gameLoader.createInstance((Class<AbstractUbiquiGame>) game.getClass());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<UbiquiGame> getGames() {
        return availableGames;
    }

    public GameSession getSession() {
        return Mocks.getDummyTournamentSession();
    }

    private void preloadGame(Class<AbstractUbiquiGame> gameClass) {
        try {
            UbiquiGame instance = gameLoader.createInstance(gameClass);
            if (instance != null && checkGameInfo(instance)) {
                availableGames.add(instance);
            } else {
                LOGGER.error("Game '" + gameClass.getName() + "' not loaded!");
            }
        }catch(InvocationTargetException e){
            LOGGER.error("Error during Game Creation", e.getTargetException());
            LOGGER.error("Class '" + gameClass.getName() + "' not loaded!");
        }catch (Exception e){
            LOGGER.error("Error during Game Creation", e);
            LOGGER.error("Class '" + gameClass.getName() + "' not loaded!");
        }
    }

    private boolean checkGameInfo(UbiquiGame instance) {
        if ((instance.getControllerFace() == null)) {
            LOGGER.error("getControllerFace() returns null");
            return false;
        }
        GameInfo gameInfo = instance.getGameInfo();
        if (gameInfo == null) {
            LOGGER.error("getGameInfo() returns null");
            return false;
        }
        if (gameInfo.getDescription() == null) {
            LOGGER.error("getDescription() returns null");
            return false;
        }
        if (gameInfo.getGameTitle() == null) {
            LOGGER.error("getGameTitle() returns null");
            return false;
        }
        if (gameInfo.getThumbnail() == null) {
            LOGGER.error("getThumbnail() returns null");
            return false;
        }
        if (gameInfo.getTutorialManual() == null) {
            LOGGER.error("getTutorialManual() returns null");
            return false;
        }

        return true;


    }

}
