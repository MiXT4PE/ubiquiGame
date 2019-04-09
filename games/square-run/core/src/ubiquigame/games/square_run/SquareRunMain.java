package ubiquigame.games.square_run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ubiquigame.common.ControllerFace;
import ubiquigame.common.GameInfo;
import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.AbstractUbiquiGame;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.games.square_run.overlay.HUD;

import java.util.*;

public class SquareRunMain extends AbstractUbiquiGame {
    //Everything can be drawn in here
    public SpriteBatch sb = null;
    public ShapeRenderer sr = null;
    //Variables should be declared here
    private static SquareRunMain instance = null;
    private SquareRunLogic logic;
    private UbiquiGamePlatform platform;
    public int height;
    public int width;
    public HUD hud;
    //Texture Manager
    public Tile[][] map;
    public TextureManager tm;
    public Player[] players;
    //GameOverMessage
    private List<Player> ranking;
    private Map<Player, Integer> score;
    private static final GameInfo gameInfo = new SquareRunGameInfo();

    public SquareRunMain(UbiquiGamePlatform platform) {
        super(platform);
        this.platform = platform;
        this.instance = this;
        //GameOverMessage vars
        this.ranking = new ArrayList<Player>();
        this.score = new HashMap<Player, Integer>();
    }

    /*
     * Is called once when this minigame is being created
     */
    @Override
    public void create() {
        this.players = getPlatformInstance().getPlayers();
        //This should come from the platform
        height = platform.getScreenDimension().getHeight();
        width = platform.getScreenDimension().getWidth();
        //Texture Manager
        tm = new TextureManager();
        //create the drawing boards
        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        //create the HUD -> in hud you should define the hud explicitly
        hud = new HUD(sb);
        //Create logic
        logic = new SquareRunLogic();
        map = new Tile[9][9];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                map[i][j] = new Tile();
            }
        }
        initBorder();
        logic.create();
        //path(map[1][1], 1, 1);
    }

    /*
     * Is called to update the status of the objects in the game
     * @param delta difference in time to the last update call
     */
    @Override
    public void update(float delta) {
        //Default update call
        logic.update(delta);
    }

    /*
     * Is called when the screen is being rendered
     * @param delta difference in time to the last render call
     */
    @Override
    public void render(float delta) {
        //Default clear call
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Background Color :)
        Gdx.gl.glClearColor(1, 1, 1, 1);
        int x = 100;
        int y = 80;
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[j][i].isPassable()) {
                    sr.setColor(Color.GRAY);
                } else {
                    sr.setColor(Color.RED);
                }
                if (map[j][i].isStart()) {
                    sr.setColor(Color.YELLOW);
                }
                if (map[j][i].isFinish()) {
                    sr.setColor(Color.GREEN);
                }
                if (map[j][i].isWay()) {
                    sr.setColor(Color.BLUE);
                }
                sr.rect(x + 90 * i, y + 90 * j, 80, 80);
            }
        }
        sr.end();
        //Default render call
        logic.render(sb);

        //render for the hud
        hud.stage.act(delta);
        hud.stage.draw();
    }

    private void initBorder() {
        map[1][1].setStart(true);
        map[0][0].setPassable(false);
        map[0][1].setPassable(false);
        map[0][2].setPassable(false);
        map[0][3].setPassable(false);
        map[0][4].setPassable(false);
        map[0][5].setPassable(false);
        map[0][6].setPassable(false);
        map[0][7].setPassable(false);
        map[0][8].setPassable(false);
        map[1][0].setPassable(false);
        map[2][0].setPassable(false);
        map[3][0].setPassable(false);
        map[4][0].setPassable(false);
        map[5][0].setPassable(false);
        map[6][0].setPassable(false);
        map[7][0].setPassable(false);
        map[8][0].setPassable(false);
        map[8][1].setPassable(false);
        map[8][2].setPassable(false);
        map[8][3].setPassable(false);
        map[8][4].setPassable(false);
        map[8][5].setPassable(false);
        map[8][6].setPassable(false);
        map[8][7].setPassable(false);
        map[8][8].setPassable(false);
        map[1][8].setPassable(false);
        map[2][8].setPassable(false);
        map[3][8].setPassable(false);
        map[4][8].setPassable(false);
        map[5][8].setPassable(false);
        map[6][8].setPassable(false);
        map[7][8].setPassable(false);
        map[8][8].setPassable(false);
        map[1][3].setPassable(false);
        map[2][4].setPassable(false);
        map[3][2].setPassable(false);
        map[4][6].setPassable(false);
        map[5][4].setPassable(false);
        map[5][7].setPassable(false);
        map[6][4].setPassable(false);
        map[6][6].setPassable(false);
        map[1][2].setWay(true);
        map[2][2].setWay(true);
        map[2][3].setWay(true);
        map[3][3].setWay(true);
        map[4][3].setWay(true);
        map[4][4].setWay(true);
        map[4][5].setWay(true);
        map[5][5].setWay(true);
        map[6][5].setWay(true);
        map[7][5].setWay(true);
        map[7][6].setWay(true);
        map[7][7].setFinish(true);
    }

    /*
     * This function is being called when the game is over
     */
    @Override
    public void gameover(GameOverMessage state) {
        platform.notifyCurrGameOver(state);
    }

    @Override
    public ControllerFace getControllerFace() {
        //should return controller but there is still a bug
        return ControllerFace.Controller_Cross;
    }

    /*
     * This function declares a GameInfo and returns it for the platform
     * @return GameInfo This class holds some informations over the game
     */
    @Override
    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public static SquareRunMain getInstance() {
        return instance;
    }

    public List<Player> getRanking() {
        return ranking;
    }

    public void setRanking(List<Player> ranking) {
        this.ranking = ranking;
    }

    public Map<Player, Integer> getScore() {
        return score;
    }

    public void setScore(Map<Player, Integer> score) {
        this.score = score;
    }

    public TextureManager getTextureManager() {
        return tm;
    }

    public Tile[][] getMap() {
        return map;
    }
}