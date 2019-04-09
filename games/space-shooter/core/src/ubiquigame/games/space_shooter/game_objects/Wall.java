package ubiquigame.games.space_shooter.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import ubiquigame.games.space_shooter.Level;

public class Wall extends SpaceObject {

    protected Polygon bounds;
    public static final float MIN_BOUNCE = 50f;
    private Level level;

    public Wall(Level level, float x, float y, int width, int height) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        bounds = new Polygon(new float[]{x, y, x+width, y, x+width, y+height, x, y+height});
    }

    // no need for update because its not moving

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        sr.setColor(1,1,1,1);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rect(x, y, width, height);
        sr.end();
    }

    // checks if a wall is vertical or horizontal
    public boolean isHorizontal() {
        if(width > height) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Polygon getBounds() {
        return bounds;
    }
}
