package ubiquigame.games.square_run.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import ubiquigame.common.Player;
import ubiquigame.common.constants.PlayerIdentifier;

public class SRPlayer {
    //Player class
    private Player player;
    //Player positioning
    private int posX;
    private int posY;
    //Player input timer
    private boolean iwt = false;
    private final float inputWait = 0.1f;
    private float inputWaitTimer = inputWait;
    //Player coloring
    private Sprite sprite;
    private int pNumber;
    private java.awt.Color awtColor;
    private com.badlogic.gdx.graphics.Color color;

    public SRPlayer(Player player, int pNumber, Texture texture, int posX, int posY) {
        this.player = player;
        this.pNumber = pNumber;
        this.posX = posX;
        this.posY = posY;
        this.sprite = new Sprite(texture);
        setColor();
    }

    public void update(float delta) {
        if (iwt == true) {
            inputWaitTimer -= delta;
        }
        if (inputWaitTimer <= 0) {
            inputWaitTimer = inputWait;
            iwt = false;
        }
    }

    private void setColor() {
        this.awtColor = PlayerIdentifier.getColorForIndex(pNumber);
        this.color = new Color(awtColor.getGreen() / 255f, awtColor.getBlue() / 255f, awtColor.getRed() / 255f, awtColor.getAlpha() / 255f);
        this.sprite.setColor(color);
    }

    public Player getPlayer() {
        return player;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Sprite getSprite() {
        sprite.setPosition((posX * 90 + 120), (posY * 90 + 100));
        return sprite;
    }

    public boolean isIwt() {
        return iwt;
    }

    public void setIwt(boolean iwt) {
        this.iwt = iwt;
    }
}