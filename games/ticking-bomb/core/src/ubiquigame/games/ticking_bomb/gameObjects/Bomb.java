package ubiquigame.games.ticking_bomb.gameObjects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import ubiquigame.games.ticking_bomb.TBSupplier;

import java.util.ArrayList;

public class Bomb {
    private TBSupplier supplier;

    //All player
    private ArrayList<Thrower> throwers;

    //Player where the bomb is at
    private Thrower atPlayer = null;

    //Bomb explode timerExplosion
    private float timeToExplode;
    private boolean bombStarted = false;

    //Sprite
    private Sprite spriteBlack;
    private Sprite spriteExplode;
    private int posX;
    private int posY;
    private int nextPosX;
    private int nextPosY;

    //Moving animation
    private final float movingLength = 0.5f;
    private boolean moving = false;
    float directionX;
    float directionY;
    private float roundingErrorX = 0f;
    private float roundingErrorY = 0f;

    //Sprite scaling while moving
    float movingTimer = 0f;
    float scaling = 0f;

    //Bomb explosion animation
    private final float explosionLength = 3f;
    private float explosionTimer = explosionLength;

    public Bomb(TBSupplier supplier){
        this.supplier = supplier;
        this.throwers = supplier.getAlive();
        this.spriteExplode = new Sprite(supplier.getTextureManager().getTexture("bomb_explode"));
        this.spriteBlack = new Sprite(supplier.getTextureManager().getTexture("bomb"));
        calculateTime();
        calculateFirstPlayer();
    }

    private void calculateTime(){
        float one = (float)(Math.random()*4) + 3f;
        float two = (float)(Math.random()*4) + 3f;
        this.timeToExplode = (one * two);
    }

    private void calculateFirstPlayer(){
        int num = (int)( Math.random()* supplier.getAlive().size() );
        this.atPlayer = supplier.getAlive().get( num );
        this.posX = atPlayer.getPosHandX();
        this.posY = atPlayer.getPosHandY();
    }

    public void start(){
        if(!this.bombStarted && !supplier.isRestart() && !supplier.isCountdown()){
            this.bombStarted = true;
            System.out.println("Bomb Time: "+timeToExplode+"s");
        }
    }

    public void passBomb(Thrower thrower){
        if(atPlayer.equals(thrower) && throwers.size() >= 2 && !moving && atPlayer.isAlive() && !atPlayer.isBurning() && !supplier.isCountdown()){
            boolean isFound = false;
            while(!isFound){
                int nextInt = (int)(Math.random()* throwers.size());
                Thrower nextTh = throwers.get(nextInt);

                if(!nextTh.equals(thrower) && nextTh.isAlive()){
                    atPlayer = nextTh;
                    nextPosX = nextTh.getPosHandX();
                    nextPosY = nextTh.getPosHandY();
                    directionX = (nextPosX - posX);
                    directionY = (nextPosY - posY);
                    moving = true;
                    thrower.setBomb(false);
                    nextTh.setBomb(true);
                    isFound = true;
                }
            }
        }
    }

    public void explode(){
        if(atPlayer.isAlive()){
            atPlayer.setBurning(true);
            supplier.setExplodedOn(atPlayer);
            supplier.setBombExploded(true);
            supplier.setRestart(true);
            if(supplier.getAlive().size() - 1 < 2){
                supplier.setGameEnded(true);
            }
        }
    }


    public void update(float delta){
        //Moving animation
        if(moving) {

            //Sprite scaling
            if (movingTimer + 0.02f > movingLength) {
                //Bomb has reached its destination
                movingTimer = 0f;
                scaling = 0f;
                spriteBlack.setScale(1f);
            } else if(movingTimer > (movingLength / 2) + 0.11f){
                //Bomb has already reached the peak of the flight
                scaling = -0.15f;
            }else if(movingTimer >= (movingLength / 2) - 0.11f) {
                //Peak of the flight
                scaling = 0f;
            } else {
                //Bomb is flying to the screen
                scaling = 0.15f;
            }

            spriteBlack.scale( scaling );

            movingTimer += delta;

            //Actual moving
            int distX = nextPosX - posX;
            int distY = nextPosY - posY;

            int dist = (int)Math.sqrt(distX * distX + distY * distY);

            int travelX = (int) ( (directionX * delta) / movingLength);
            roundingErrorX += ( (directionX * delta) / movingLength) - travelX;

            int travelY = (int) ( (directionY * delta) / movingLength);
            roundingErrorY += ( (directionY * delta) / movingLength) - travelY;

            int distTravel = ( (int) Math.sqrt(travelX * travelX + travelY * travelY) );

            if ( distTravel > dist ){
                this.posX = nextPosX;
                this.posY = nextPosY;
                this.moving = false;
            } else {
                this.posX += travelX;
                this.posY += travelY;

                //Rounding Error correction
                if( Math.abs(roundingErrorX)  - 1f > 0f ){
                    float signX = Math.signum(roundingErrorX);
                    this.posX += 1f * signX;
                    roundingErrorX -= 1f * signX;
                }
                if( Math.abs(roundingErrorY)  - 1f > 0f ){
                    float signY = Math.signum(roundingErrorY);
                    this.posY += 1f * signY;
                    roundingErrorY -= 1f * signY;
                }
            }
        } else {
            //for sprite scaling
            movingTimer = 0f;
            scaling = 0f;
            spriteBlack.setScale(1f);
        }

        //Bomb explosion timer
        if(bombStarted){
            timeToExplode -= delta;
        }
        if( (timeToExplode <= 0f) && bombStarted){
            explode();
        }

        //Explosion animation
        if(supplier.hasBombExploded()){
            explosionTimer -= delta;
        }

        if(explosionTimer < 0){
            explosionTimer = explosionLength;
            atPlayer.setAlive(false);
            atPlayer.setBurning(false);
            supplier.setBombExploded(false);
            supplier.setShowExpPlayer(true);
        }
    }

    public int getPosX(){
        return this.posX;
    }

    public int getPosY(){
        return this.posY;
    }

    public Sprite getSpriteBlack(){
        return this.spriteBlack;
    }

    public Sprite getSpriteExplode(){
        return this.spriteExplode;
    }

    public void explodeTrigger(){
        explode();
    }

    public Thrower getAtPlayer() {
        return atPlayer;
    }
}
