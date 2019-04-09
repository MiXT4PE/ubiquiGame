package ubiquigame.games.EnchantedLabyrinth;

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

        textures.put("playerNoHammer0", new Texture("EnchantedLabyrinth/player1.png"));
        textures.put("playerNoHammer1", new Texture("EnchantedLabyrinth/player2.png"));
        textures.put("playerNoHammer2", new Texture("EnchantedLabyrinth/player3.png"));
        textures.put("playerNoHammer3", new Texture("EnchantedLabyrinth/player4.png"));

        textures.put("playerHammer0", new Texture("EnchantedLabyrinth/player1Hammer.png"));
        textures.put("playerHammer1", new Texture("EnchantedLabyrinth/player2Hammer.png"));
        textures.put("playerHammer2", new Texture("EnchantedLabyrinth/player3Hammer.png"));
        textures.put("playerHammer3", new Texture("EnchantedLabyrinth/player4Hammer.png"));

        textures.put("treasure", new Texture("EnchantedLabyrinth/treasure.png"));
        textures.put("stone", new Texture("EnchantedLabyrinth/stone.png"));

        textures.put("background", new Texture("EnchantedLabyrinth/background1.jpg"));
        textures.put("wall_vertical", new Texture("EnchantedLabyrinth/wall_vertical.png"));
        textures.put("wall_horizontal", new Texture("EnchantedLabyrinth/wall_horizontal.png"));
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
