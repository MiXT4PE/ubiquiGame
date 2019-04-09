package ubiquigame.games.space_shooter.game_objects;

import com.badlogic.gdx.math.Polygon;

public abstract class SpaceObject {
    protected float x;
    protected float y;

    protected float dx;
    protected float dy;

    protected float cachedX;
    protected float cachedY;

    protected float radians;
    protected float speed;
    protected float rotationSpeed;

    protected int width;
    protected int height;

    protected float[] shapex;
    protected float[] shapey;

    public abstract Polygon getBounds();
}
