package ubiquigame.games.tetris;

import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

public class TextureManager {
    private HashMap<String, Texture> textures;

    public TextureManager(){
        this.textures = new HashMap<String, Texture>();
        init();
    }

    //Set textures here
    private void init(){
        textures.put("pixel_aqua", new Texture("tetris/pixel_aqua.jpg"));
        textures.put("pixel_blue", new Texture("tetris/pixel_blue.jpg"));
        textures.put("pixel_green", new Texture("tetris/pixel_green.jpg"));
        textures.put("pixel_orange", new Texture("tetris/pixel_orange.jpg"));
        textures.put("pixel_purple", new Texture("tetris/pixel_purple.jpg"));
        textures.put("pixel_red", new Texture("tetris/pixel_red.jpg"));
        textures.put("pixel_yellow", new Texture("tetris/pixel_yellow.jpg"));
    }

    public void dispose(){
        for (Texture t : textures.values()) {
            t.dispose();
        }
    }

    public Texture getTexture(String key){
        return textures.get(key);
    }
}
