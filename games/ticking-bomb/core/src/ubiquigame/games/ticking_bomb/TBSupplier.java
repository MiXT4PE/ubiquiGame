package ubiquigame.games.ticking_bomb;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.games.ticking_bomb.gameObjects.Bomb;
import ubiquigame.games.ticking_bomb.gameObjects.Thrower;

import java.util.ArrayList;

public class TBSupplier {
    private TextureManager textureManager;
    private TBMain main;
    private TBHud hud;
    private UbiquiGamePlatform platform;

    private boolean gameOver = false;

    private int height;
    private int width;

    private SpriteBatch sb;
    private ShapeRenderer sr;

    //For the logic
    private ArrayList<Thrower> throwers;
    private ArrayList<Thrower> alive;
    private Thrower ExplodedOn;

    private Bomb bomb;

    //For the HUD
    private boolean isCountdown;
    private boolean hasBombExploded;
    private boolean isBetween_C_E;
    private boolean hasGameEnded;
    private boolean isRestart;
    private boolean showExpPlayer;

    public TBSupplier(TBMain main){
        this.main = main;
    }

    public void initialize(){
        this.platform = main.getPlatform();
        this.height = main.height;
        this.width = main.width;
        this.sb = main.getSb();
        this.sr = main.getSr();

        this.textureManager = new TextureManager();
        this.hud = new TBHud(this, sb);

    }

    public TextureManager getTextureManager() {
        return textureManager;
    }

    public Texture getTexture(String key){
        return textureManager.getTexture(key);
    }

    public TBMain getMain() {
        return main;
    }

    public TBHud getHud() {
        return hud;
    }

    public UbiquiGamePlatform getPlatform() {
        return platform;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }




    public ArrayList<Thrower> getThrowers() {
        return throwers;
    }

    public void setThrowers(ArrayList<Thrower> throwers) {
        this.throwers = throwers;
    }

    public ArrayList<Thrower> getAlive() {
        return alive;
    }

    public void setAlive(ArrayList<Thrower> alive) {
        this.alive = alive;
    }

    public SpriteBatch getSb() {
        return sb;
    }

    public ShapeRenderer getSr() {
        return sr;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public void setBomb(Bomb bomb) {
        this.bomb = bomb;
    }

    public boolean isCountdown() {
        return isCountdown;
    }

    public void setCountdown(boolean countdown) {
        isCountdown = countdown;
    }

    public boolean hasBombExploded() {
        return hasBombExploded;
    }

    public void setBombExploded(boolean hasBombExploded) {
        this.hasBombExploded = hasBombExploded;
    }

    public boolean isBetween_C_E() {
        return isBetween_C_E;
    }

    public void setBetween_C_E(boolean between_C_E) {
        isBetween_C_E = between_C_E;
    }

    public boolean hasGameEnded() {
        return hasGameEnded;
    }

    public void setGameEnded(boolean hasGameEnded) {
        this.hasGameEnded = hasGameEnded;
    }

    public boolean getGameEnded(){
        return this.hasGameEnded;
    }

    public void setExplodedOn(Thrower thrower){
        this.ExplodedOn = thrower;
    }

    public Thrower getExplodedOn(){
        return this.ExplodedOn;
    }

    public void setRestart(boolean isRestart){
        this.isRestart = isRestart;
    }

    public boolean isRestart(){
        return this.isRestart;
    }

    public boolean showsExpPlayer() {
        return showExpPlayer;
    }

    public void setShowExpPlayer(boolean showExpPlayer) {
        this.showExpPlayer = showExpPlayer;
    }

    public boolean isGameOver(){
        return this.gameOver;
    }

    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }
}
