package ubiquigame.games.memory;

import com.badlogic.gdx.graphics.Texture;

public class MemoryCard {

    private Texture picture;
    private boolean open;
    private int x;
    private int y;



    public MemoryCard(Texture picture, int x, int y) {
        open = false;
        this.picture =picture;
        this.x = x;
        this.y = y;
    }



    public Texture getPicture() {
        return picture;
    }

    public void setPicture(Texture picture) {
        picture = picture;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


}
