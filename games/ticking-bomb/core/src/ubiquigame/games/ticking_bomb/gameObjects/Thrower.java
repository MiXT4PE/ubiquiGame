package ubiquigame.games.ticking_bomb.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import ubiquigame.common.Player;
import ubiquigame.common.constants.PlayerIdentifier;
import ubiquigame.games.ticking_bomb.TBSupplier;
import ubiquigame.games.ticking_bomb.TextureManager;

public class Thrower {
    //Default connection to logic
    private TBSupplier supplier;
    private TextureManager tm;

    //All needed sprites
    private Sprite sprite_down;
    private Sprite sprite_up;
    private Sprite sprite_burning;
    private Sprite pants;
    private Sprite sprite_ashes;

    //Boolean states of the thrower
    private boolean hasBomb = false;
    private boolean isAlive = true;
    private boolean isBurning = false;

    //Platform player variables
    private Player player;
    private int pNumber;
    private java.awt.Color awtColor;
    private com.badlogic.gdx.graphics.Color color;

    //Logic positions
    private int posX;
    private int posY;
    private int posHandX;
    private int posHandY;


    public Thrower(TBSupplier supplier, Player player, int pNumber,int posX, int posY, int posHandX, int posHandY) {
        this.supplier = supplier;
        this.tm = supplier.getTextureManager();
        this.player = player;
        this.pNumber = pNumber;
        this.posX = posX;
        this.posY = posY;
        this.posHandX = posHandX;
        this.posHandY = posHandY;
        decideTexture();
    }

    public boolean hasBomb(){
        return this.hasBomb;
    }

    public void setBomb(boolean hasBomb){
        this.hasBomb = hasBomb;
    }

    public boolean isAlive(){
        return this.isAlive;
    }

    public void setAlive(boolean isAlive){
        this.isAlive = isAlive;
    }

    public boolean isBurning(){return this.isBurning;}

    public void setBurning(boolean isBurning){this.isBurning = isBurning;}

    public Sprite getSprite_down(){
        return this.sprite_down;
    }

    public Sprite getSprite_up(){return this.sprite_up;}

    public Sprite getPants(){return this.pants;}

    public Sprite getSprite_ashes(){return this.sprite_ashes;}

    public Sprite getSprite_burning(){return this.sprite_burning;}

    public Player getPlayer(){
        return this.player;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public int getPosHandX() {
        return this.posHandX;
    }

    public int getPosHandY() {
        return this.posHandY;
    }

    private void decideTexture(){
        this.awtColor = PlayerIdentifier.getColorForIndex(pNumber);
        this.color = new Color( ( (float)awtColor.getRed() / 255f ), ( (float)awtColor.getGreen() / 255f ), ( (float)awtColor.getBlue() / 255f ), ( (float)awtColor.getAlpha() / 255f ) );

        this.sprite_ashes = new Sprite( tm.getTexture("thrower_ashes") );
        this.pants = new Sprite( tm.getTexture("thrower_pants") );

        if(pNumber == 1){
            this.sprite_down = new Sprite( tm.getTexture("thrower_top_down") );
            this.sprite_up = new Sprite( tm.getTexture("thrower_top_up") );
            this.sprite_burning = new Sprite( tm.getTexture("thrower_top_burned") );
        } else if(pNumber == 2){
            this.sprite_down = new Sprite( tm.getTexture("thrower_top_down") );
            this.sprite_up = new Sprite( tm.getTexture("thrower_top_up") );
            this.sprite_burning = new Sprite( tm.getTexture("thrower_top_burned") );
            this.sprite_down.flip(true, false);
            this.sprite_up.flip(true, false);
            this.sprite_burning.flip(true, false);
            this.pants.flip(true, false);
        } else if(pNumber == 3){
            this.sprite_down = new Sprite( tm.getTexture("thrower_bottom_down") );
            this.sprite_up = new Sprite( tm.getTexture("thrower_bottom_up") );
            this.sprite_burning = new Sprite( tm.getTexture("thrower_bottom_burned") );
        } else {
            this.sprite_down = new Sprite( tm.getTexture("thrower_bottom_down") );
            this.sprite_up = new Sprite( tm.getTexture("thrower_bottom_up") );
            this.sprite_burning = new Sprite( tm.getTexture("thrower_bottom_burned") );
            this.sprite_down.flip(true, false);
            this.sprite_up.flip(true, false);
            this.sprite_burning.flip(true, false);
            this.pants.flip(true, false);
        }


        this.pants.setColor(color);
    }
}
