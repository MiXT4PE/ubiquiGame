package ubiquigame.games.EnchantedLabyrinth.gameObjects;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ubiquigame.games.EnchantedLabyrinth.TextureManager;

public class Stone {

    private Position position;
    private Texture texture;
    private static SpriteBatch spriteBatch;
    private Labyrinth labyrinth;

    public Position getPosition() {
        return position;
    }

    public Stone(Labyrinth labyrinth, TextureManager textureManager) {
        this.labyrinth = labyrinth;
        position = this.labyrinth.getRandomFreePosition();
        texture = textureManager.getTexture("stone");
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

