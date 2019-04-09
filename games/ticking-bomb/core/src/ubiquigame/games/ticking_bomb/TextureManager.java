package ubiquigame.games.ticking_bomb;

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
        textures.put("bomb", new Texture("ticking_bomb/bomb.png"));
        textures.put("bomb_explode", new Texture("ticking_bomb/bomb_explode.png"));
        textures.put("thrower_top_up", new Texture("ticking_bomb/thrower_top_up.png"));
        textures.put("thrower_top_down", new Texture("ticking_bomb/thrower_top_down.png"));
        textures.put("thrower_top_burned", new Texture("ticking_bomb/thrower_top_burned.png"));
        textures.put("thrower_bottom_up", new Texture("ticking_bomb/thrower_bottom_up.png"));
        textures.put("thrower_bottom_down", new Texture("ticking_bomb/thrower_bottom_down.png"));
        textures.put("thrower_bottom_burned", new Texture("ticking_bomb/thrower_bottom_burned.png"));
        textures.put("thrower_ashes", new Texture("ticking_bomb/thrower_ashes.png"));
        textures.put("thrower_pants", new Texture("ticking_bomb/thrower_pants.png"));
        textures.put("banner", new Texture("ticking_bomb/banner.png"));
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
