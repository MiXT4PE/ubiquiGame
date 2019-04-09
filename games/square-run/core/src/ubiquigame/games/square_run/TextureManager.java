package ubiquigame.games.square_run;

import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;

public class TextureManager {
    private HashMap<String, Texture> textures;

    public TextureManager() {
        this.textures = new HashMap<String, Texture>();
        init();
    }

    //Set textures here
    private void init() {
        textures.put("player", new Texture("SquareRun/Player.png"));
    }

    public void dispose() {
        for (Texture t : textures.values()) {
            t.dispose();
        }
    }

    public Texture getTexture(String key) {
        return textures.get(key);
    }
}