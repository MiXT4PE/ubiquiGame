package ubiquigame.games.space_shooter.game_objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import ubiquigame.games.space_shooter.Level;
import ubiquigame.games.space_shooter.SpaceShooter;

public class Bullet extends SpaceObject{

    private boolean remove = false;

    private Level level;

    private Color color;

    private Polygon bounds;

    public Bullet(Level level, float x, float y, float radians, Color color){
        this.x = x;
        this.y = y;
        width = 10;
        height = 20;

        this.radians = radians;
        this.color = color;

        this.level = level;

        float speed = 500f;
        dx = MathUtils.cos(radians)*speed;
        dy = MathUtils.sin(radians)*speed;

        bounds = new Polygon(new float[]{x, y, x+height, y, x+height, y+width, x, y+width});
        bounds.setOrigin(x + height/2f, y + width/2f);
        bounds.rotate(radians*MathUtils.radiansToDegrees);

    }

    public boolean shouldRemove() {
        return remove;
    }

    public void update(float dt) {
        x += dx * dt;
        y += dy * dt;
        // update position
        bounds.translate(dx * dt, dy * dt);

        // bullet out of screen
        if(x<0 || y<0 || x> SpaceShooter.getInstance().width+20 || y>SpaceShooter.getInstance().height+20){
            remove = true;
        }

        SpaceObject obj = level.checkCollision(this);
        if(obj != null) {
            // intersection with ship
            if(obj instanceof Ship) {
                // for preventing self hitting
                if(((Ship)obj).getColor() != color) {
                    ((Ship)obj).setHit(this);
                    remove = true;
                }
            }
            // intersection with obstacle
            if(obj instanceof Obstacle) {
                ((Obstacle)obj).setHit();
                remove = true;
            }
            // wall, hud wall, , bullet -> bullet disappears
            if(obj instanceof Bullet || obj instanceof Wall || obj instanceof HUDWall)
                remove = true;
        }
    }

    public void render(ShapeRenderer sr) {
        sr.setColor(color);

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.polygon(bounds.getTransformedVertices());

//        sr.line(x, y, x+MathUtils.cos(radians)*height, y+MathUtils.sin(radians)*height);
        sr.end();
    }

    // is needed for collision
    public Vector2 getStart() {
        return new Vector2(x,y);
    }
    // is needed for collision
    public Vector2 getEnd() {
        return new Vector2(x+MathUtils.cos(radians)*height, y+MathUtils.sin(radians)*height);
    }

    @Override
    public Polygon getBounds() {

        return bounds;
    }
}
