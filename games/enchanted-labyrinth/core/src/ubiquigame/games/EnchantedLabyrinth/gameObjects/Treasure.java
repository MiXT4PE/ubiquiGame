package ubiquigame.games.EnchantedLabyrinth.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ubiquigame.games.EnchantedLabyrinth.TextureManager;

public class Treasure {

    private Position position;
    private Texture texture;
    private SpriteBatch spriteBatch;
    private Labyrinth labyrinth;

    public Treasure(Labyrinth labyrinth, TextureManager textureManager) {
        this.labyrinth = labyrinth;
        position = this.labyrinth.getTreasurePosition();
        texture = textureManager.getTexture("treasure");
        spriteBatch = new SpriteBatch();
    }

    public void render() {
        int tileSize = labyrinth.getTileSize();
        int rendX = labyrinth.getRenderX(position.getCol());
        int rendY = labyrinth.getRenderY(position.getRow());

        spriteBatch.begin();
        spriteBatch.draw(texture, rendX, rendY, tileSize, tileSize);
        spriteBatch.end();
    }
}
