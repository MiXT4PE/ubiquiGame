package ubiquigame.games.EnchantedLabyrinth;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ubiquigame.common.Player;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.games.EnchantedLabyrinth.gameObjects.Labyrinth;
import ubiquigame.games.EnchantedLabyrinth.gameObjects.Stone;
import ubiquigame.games.EnchantedLabyrinth.gameObjects.Treasure;
import ubiquigame.games.EnchantedLabyrinth.gameObjects.LabyrinthPlayer;
import java.util.*;

public class EnchantedLabyrinthLogic {
    //Main instance
    EnchantedLabyrinthGame main;
    //TextureManager
    TextureManager tm;
    //Camera
    private OrthographicCamera camera;
    private Labyrinth labyrinth;
    private List<LabyrinthPlayer> labyrinthPlayers = new ArrayList<>();
    private List<Stone> stones;
    private Treasure treasure;
    private Sound stoneTakenSound;
    private  Texture background;
	private boolean isGameOver;

    public EnchantedLabyrinthLogic(){

        main = EnchantedLabyrinthGame.getInstance();
        tm = main.getTextureManager();
        background = tm.getTexture("background");
        camera = new OrthographicCamera(main.width, main.height);
        camera.setToOrtho(false, main.width, main.height);

        labyrinth = new Labyrinth( main.width, main.height, 25, 30, tm);
        int playerNumber = 0;
        for(Player p : main.getPlatformInstance().getPlayers()) {
            labyrinthPlayers.add(new LabyrinthPlayer(playerNumber, labyrinth, p, tm, main.hud.addLabelToTable()));
            playerNumber++;
        }

        treasure = new Treasure(labyrinth, tm);
        stones = new ArrayList<>();
        for(int i = 0; i < labyrinthPlayers.size()*5; i++) {
            stones.add(new Stone(labyrinth, tm));
        }

        FileHandle fileHandle = Gdx.files.internal("EnchantedLabyrinth/stoneTaken.mp3");
        stoneTakenSound = Gdx.audio.newSound(fileHandle);
    }

    public void render(SpriteBatch sb){
        //Gdx.gl.glClearColor(0, 0.5f, 1.0f, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.begin();
        sb.draw(background,0,0, main.width,main.height);
        sb.end();

        for(LabyrinthPlayer labyrinthPlayer : labyrinthPlayers) {
            Stone stoneToDelete = null;

            for(Stone stone : stones) {
                stone.render();
                if (labyrinthPlayer.canCollect() && stone.getPosition().equals(labyrinthPlayer.getPosition())) {
                    labyrinthPlayer.addStone();
                    stoneToDelete = stone;
                    stoneTakenSound.play();
                    break;
                }
            }

            if(stoneToDelete != null) {
                stones.remove(stoneToDelete);
            }
        }

        labyrinth.render();
        isGameOver = false;
        for(LabyrinthPlayer labyrinthPlayer : labyrinthPlayers) {
            labyrinthPlayer.render();

            if (labyrinthPlayer.isTreasureTaken()) {
                isGameOver = true;
            }
        }

        treasure.render();

        if(isGameOver) {
            Collections.sort(labyrinthPlayers, (p1, p2) -> {
                if(p1.getScore() != p2.getScore()) {
                    return p1.getScore() > p2.getScore() ? -1 : 1;
                }
                return 0;
            });

            List<Player> rankings = new ArrayList<>();
            Map<Player, Integer> score = new HashMap<>();
            for(LabyrinthPlayer lp : labyrinthPlayers) {
                rankings.add(lp.getPlatformPlayer());
                score.put(lp.getPlatformPlayer(), lp.getScore());
            }

            EnchantedLabyrinthGame.getInstance().gameover(new GameOverMessage(rankings, score));
        }
    }

    public boolean isGameOver() {
		return isGameOver;
	}
    
    private void handleInput(){

    }

    public void update(float delta){
        handleInput();
    }


}
