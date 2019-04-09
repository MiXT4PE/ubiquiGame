package ubiquigame.games.square_run;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ubiquigame.common.Player;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.common.impl.InputState;
import com.badlogic.gdx.graphics.OrthographicCamera;
import ubiquigame.games.square_run.gameObjects.SRPlayer;

import java.util.*;

public class SquareRunLogic {
    SquareRunMain main;
    TextureManager tm;
    private OrthographicCamera camera;
    private int startPosX;
    private int startPosY;
    private ArrayList<SRPlayer> srPlayers;
    boolean gameover = false;

    public SquareRunLogic() {
        startPosX = 1;
        startPosY = 1;
        main = SquareRunMain.getInstance();
        tm = main.getTextureManager();
        camera = new OrthographicCamera(main.width, main.height);
        camera.setToOrtho(false, main.width, main.height);
        this.srPlayers = new ArrayList<>();
    }

    public void create() {
        //For each Player create gameobject SRPlayer
        int num = 1;
        for (Player player : main.players) {
            srPlayers.add(new SRPlayer(player, num, tm.getTexture("player"), startPosX, startPosY));
            num++;
        }
        srPlayers.trimToSize();
        System.out.println("Size: " + srPlayers.size());
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        for (SRPlayer srPlayer : srPlayers) {
            srPlayer.getSprite().draw(sb);
        }
        sb.end();
    }

    private void handleInput() {
        for (SRPlayer srPlayer : srPlayers) {
            if (!srPlayer.isIwt()) {
                InputState input = srPlayer.getPlayer().getController().getInput();
                int newX = srPlayer.getPosX();
                int newY = srPlayer.getPosY();
                if (input.isLeft()) {
                    newX--;
                }
                if (input.isRight()) {
                    newX++;
                }
                if (input.isUp()) {
                    newY++;
                }
                if (input.isDown()) {
                    newY--;
                }
                Tile[][] map = main.getMap();
                if (newX < 0 || newY < 0 || !map[newY][newX].isPassable()) {
                } else {
                    srPlayer.setPosX(newX);
                    srPlayer.setPosY(newY);
                }
                srPlayer.setIwt(true);
            }
        }
    }

    public void update(float delta) {
        over:
        if (!gameover) {
            handleInput();
            for (SRPlayer srPlayer : srPlayers) {
                srPlayer.update(delta);
                if (srPlayer.getPosX() == 7 && srPlayer.getPosY() == 7) {
                    List<Player> ranking = new ArrayList<>();
                    HashMap<Player, Integer> score = new HashMap<>();
                    for (Player player : main.getPlatformInstance().getPlayers()) {
                        if (player.equals(srPlayer.getPlayer())) {
                            ranking.add(0, player);
                            score.put(player, 1000);
                        } else {
                            ranking.add(player);
                            score.put(player, 200);
                        }
                    }
                    main.setRanking(ranking);
                    main.setScore(score);
                    gameover = true;
                    main.gameover(new GameOverMessage(ranking, score));
                    break over;
                }
            }
        }
    }

    public void dispose() {
    }
}