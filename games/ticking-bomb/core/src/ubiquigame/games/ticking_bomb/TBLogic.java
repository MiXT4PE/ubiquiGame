package ubiquigame.games.ticking_bomb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.common.impl.InputState;
import ubiquigame.games.ticking_bomb.gameObjects.Bomb;
import ubiquigame.games.ticking_bomb.gameObjects.Thrower;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TBLogic{
    //Supplier
    private TBSupplier supplier;
    private TBMain main;
    private UbiquiGamePlatform platform;
    private TBHud hud;

    //Camera
    private OrthographicCamera camera;

    private boolean initialized = false;

    //Playersize
    private int pTsizeX;
    private int pTsizeY;

    //Bombsize
    private int bTsize;

    //Playerpositions and Bombpositions
    private int p0_X, p0_Y, p0_handX, p0_handY;
    private int p1_X, p1_Y, p1_handX, p1_handY;
    private int p2_X, p2_Y, p2_handX, p2_handY;
    private int p3_X, p3_Y, p3_handX, p3_handY;

    //Bomb particle
    ParticleEffect pe;

    public TBLogic(TBSupplier supplier) {
        this.supplier = supplier;
        this.main = supplier.getMain();
        this.hud = supplier.getHud();
        this.platform = supplier.getPlatform();
        camera = new OrthographicCamera(main.width, main.height);
        camera.setToOrtho(false, main.width, main.height);
        this.pTsizeX = main.width / 8;
        this.pTsizeY = main.width / 8;
        this.bTsize  = main.width / 16;

        definePositions();
        initialize();
    }

    private void definePositions(){
        //Player 1
        this.p0_X = ((main.width/2) / 2 );
        this.p0_Y = main.height - ((main.height/2) / 2) - (pTsizeY / 2);

        this.p0_handX = p0_X + ( pTsizeX / 4 ) - ( bTsize / 16 );
        this.p0_handY = p0_Y + pTsizeY - ( bTsize / 3 );

        //Player 2
        this.p1_X = main.width - ((main.width/2) / 2 ) - pTsizeX;
        this.p1_Y = main.height - ((main.height/2) / 2) - (pTsizeY / 2);

        this.p1_handX = p1_X +  ( pTsizeX / 4 )  + ( bTsize / 8 );
        this.p1_handY = p1_Y + pTsizeY - ( bTsize / 3 );

        //Player 3
        this.p2_X = ((main.width/2) / 2 );
        this.p2_Y = ((main.height/2) / 2) - (pTsizeY / 2);

        this.p2_handX = p2_X +  ( pTsizeX / 4 )  - ( bTsize / 16 );
        this.p2_handY = p2_Y + pTsizeY - ( bTsize / 3 );

        //Player 4
        this.p3_X = main.width - ((main.width/2) / 2 ) - pTsizeX;
        this.p3_Y = ((main.height/2) / 2) - (pTsizeY / 2);

        this.p3_handX = p3_X +  ( pTsizeX / 4 )  + ( bTsize / 8 );
        this.p3_handY = p3_Y + pTsizeY - ( bTsize / 3 );
    }

    public void initialize(){
        initialized = true;
        ArrayList<Thrower> throwers = new ArrayList<Thrower>();
        int i = 0;
        for (Player p: platform.getPlayers()) {
            if( i == 0 ){
                throwers.add(new Thrower(supplier, p, i+1, p0_X, p0_Y, p0_handX, p0_handY));
            } else if( i == 1 ){
                throwers.add(new Thrower(supplier, p, i+1, p1_X, p1_Y, p1_handX, p1_handY));
            } else if( i == 2 ){
                throwers.add(new Thrower(supplier, p, i+1, p2_X, p2_Y, p2_handX, p2_handY));
            } else if( i == 3 ){
                throwers.add(new Thrower(supplier, p, i+1, p3_X, p3_Y, p3_handX, p3_handY));
            }

            i++;
        }
        ArrayList<Thrower> alive = new ArrayList<Thrower>();
        alive.addAll(throwers);

        supplier.setThrowers(throwers);
        supplier.setAlive(alive);

        Bomb bomb = new Bomb(supplier);
        supplier.setBomb(bomb);
        supplier.setCountdown(true);

        //Bomb particles
        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("ticking_bomb/particleBombEffect"),Gdx.files.internal("ticking_bomb"));
        pe.start();
    }

    public void restart(Thrower thrower){
        if(supplier.isRestart()){
            ArrayList<Thrower> alive = supplier.getAlive();
            alive.remove(thrower);
            supplier.setAlive(alive);
            List<Player> temp = main.getRanking();
            if(alive.size() < 2 && supplier.getGameEnded()){
                //Game finished

                temp.add(0, thrower.getPlayer());
                temp.add(0, alive.get(0).getPlayer());
                main.setRanking( temp );

                Map<Player, Integer> score = main.getScore();
                for (int i = 0; i < main.getRanking().size(); i++) {
                    score.put(main.getRanking().get(i), (main.getRanking().size() - i) * 100 );
                }
                main.setScore(score);

                main.gameover(new GameOverMessage(main.getRanking(), main.getScore()));
                supplier.setGameOver(true);
                return;
            } else {
                //Game not finished
                //Reset
                temp.add(0, thrower.getPlayer());

                main.setRanking( temp );

                //Reset for the game
                Bomb bomb = new Bomb(supplier);
                supplier.setBomb(bomb);
            }
            supplier.setRestart(false);
        }
    }

    private void handleInput() {
        InputState input;
        for (Thrower thrower: supplier.getThrowers()) {
            input = thrower.getPlayer().getController().getInput();
            if(input.isNext()){
                supplier.getBomb().passBomb(thrower);
            }
        }

        //For testing purpose isnt possible in real game
        //It makes the bomb explode instantly
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            supplier.getBomb().explodeTrigger();
        }
    }

    public void update(float dt) {
        if( ( !supplier.isCountdown() || !supplier.hasBombExploded() ) ){
            if(supplier.isRestart() && !supplier.hasBombExploded() && !supplier.showsExpPlayer()){
                restart(supplier.getExplodedOn());
            } else {
                handleInput();
                supplier.getBomb().update(dt);
                pe.getEmitters().first().setPosition(supplier.getBomb().getPosX() + supplier.getBomb().getSpriteBlack().getWidth() - ( (supplier.getBomb().getSpriteBlack().getScaleX() -1 ) * 60),
                        supplier.getBomb().getPosY() + supplier.getBomb().getSpriteBlack().getHeight() - ( ( supplier.getBomb().getSpriteBlack().getScaleY() - 1 ) * 60));
                pe.update(Gdx.graphics.getDeltaTime());
            }
            //Start bomb
            this.supplier.getBomb().start();
            //Start bomb animation
            pe.update(Gdx.graphics.getDeltaTime());
        }
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        //Background Color :)
        Gdx.gl.glClearColor((81f/255f),(166f/255f),(83f/255f), 1);

        //Default clear call
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw on the SpriteBatch
        if(initialized){

            //Start SpriteBatch
            sb.begin();

            //Draw each thrower
            for (Thrower thrower: supplier.getThrowers()) {
                //Thrower has the bomb is alive and not burning
                if( thrower.hasBomb() && thrower.isAlive() && !thrower.isBurning()){
                    thrower.getSprite_up().setPosition(thrower.getPosX(), thrower.getPosY());
                    thrower.getSprite_up().setSize(pTsizeX, pTsizeY);
                    thrower.getSprite_up().draw(sb);

                    thrower.getPants().setPosition(thrower.getPosX(), thrower.getPosY());
                    thrower.getPants().setSize(pTsizeX, pTsizeY);
                    thrower.getPants().draw(sb);

                //Thrower doesnt the bomb is alive and not burning
                } else if ( !thrower.hasBomb() && thrower.isAlive() && !thrower.isBurning() ) {
                    thrower.getSprite_down().setPosition(thrower.getPosX(), thrower.getPosY());
                    thrower.getSprite_down().setSize(pTsizeX, pTsizeY);
                    thrower.getSprite_down().draw(sb);

                    thrower.getPants().setPosition(thrower.getPosX(), thrower.getPosY());
                    thrower.getPants().setSize(pTsizeX, pTsizeY);
                    thrower.getPants().draw(sb);

                //Thrower is not alive or is burning
                } else {
                    //Thrower is burning
                    if( thrower.isBurning() ){
                        thrower.getSprite_burning().setPosition(thrower.getPosX(), thrower.getPosY());
                        thrower.getSprite_burning().setSize(pTsizeX, pTsizeY);
                        thrower.getSprite_burning().draw(sb);

                    //Thrower is not alive
                    } else {
                        thrower.getSprite_ashes().setPosition(thrower.getPosX(), thrower.getPosY());
                        thrower.getSprite_ashes().setSize(pTsizeX, pTsizeY * 1.5f);
                        thrower.getSprite_ashes().draw(sb);
                    }
                }
            }

            //Decide whether or not to draw the bomb
            if(!supplier.hasBombExploded() && !supplier.isCountdown() && !supplier.isBetween_C_E() && !supplier.showsExpPlayer()){
                supplier.getBomb().getAtPlayer().setBomb(true);

                supplier.getBomb().getSpriteBlack().setPosition(supplier.getBomb().getPosX(), supplier.getBomb().getPosY());

                supplier.getBomb().getSpriteBlack().setSize(bTsize, bTsize);
                supplier.getBomb().getSpriteBlack().draw(sb);
                pe.draw(sb);
            }

            //Draw exploding bomb
            if(supplier.hasBombExploded()){
                //supplier.getBomb().getSpriteExplode().setPosition(supplier.getExplodedOn().getPosHandX(), supplier.getExplodedOn().getPosHandY());
                //supplier.getBomb().getSpriteExplode().setSize(bTsize, bTsize);
                //supplier.getBomb().getSpriteExplode().draw(supplier.getSb());
            }

            //Close SpriteBatch
            sb.end();

            //Handle Bomb animation
            if (pe.isComplete()){
                pe.reset();
            }
        }
    }

    public void dispose() {
    }
}
