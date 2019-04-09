package ubiquigame.games.space_shooter.game_objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import ubiquigame.games.space_shooter.Level;

public class Obstacle extends SpaceObject{

    // for checking collision and adding to list
    private Level level;

    private Polygon obstacle;
    private float[] obstacleBounds;

    // energy
    private int energy;

    // different size of Obstacle
    private int type;
    public static final int SMALL = 0;
    public static final int MEDIUM = 1;
    public static final int LARGE = 2;

    // number of edges
    private int numPoints;
    private float[] dists;

    private boolean remove;
    private boolean hit;
    private boolean hitWithinX;

    public Obstacle(Level level, float x, float y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;

        this.level = level;

        if(type == SMALL) {
            numPoints = 8;
            width = height  = 12;
            speed = MathUtils.random(70, 100);
            energy = 1;
        }

        if(type == MEDIUM) {
            numPoints = 10;
            width = height  = 20;
            speed = MathUtils.random(50, 60);
            energy = 2;
        }

        if(type == LARGE) {
            numPoints = 12;
            width = height  = 40;
            speed = MathUtils.random(20, 30);
            energy = 3;
        }

        rotationSpeed = MathUtils.random(-1, 1);

        radians = MathUtils.random(2 * MathUtils.PI);
        dx = MathUtils.cos(radians) * speed;
        dy = MathUtils.sin(radians) * speed;

        shapex = new float[numPoints];
        shapey = new float[numPoints];
        dists = new float[numPoints];

        int radius = width / 2;
        for(int i = 0; i < numPoints; i++) {
            dists[i] = MathUtils.random(radius/2, radius);
        }

        setShape();

    }

    // shapex and shapey or obstacleBounds will contain points which will be connected to a polygon in render
    private void setShape() {
        float angle = 0f;
        for(int i = 0; i < numPoints; i++) {
            shapex[i] = x + MathUtils.cos(angle + radians) * dists[i];
            shapey[i] = y + MathUtils.sin(angle + radians) * dists[i];
            angle += 2 * MathUtils.PI / numPoints;
        }

        obstacleBounds = new float[numPoints*2];
        int j = 0;
        for (int i = 0; i < numPoints * 2; i+=2) {
            obstacleBounds[i] = shapex[j];
            obstacleBounds[i+1] = shapey[j];
            j++;
        }
        obstacle = new Polygon(obstacleBounds);
    }

    // choose a random size of obstacle
    public static int randSize() {
        int rand = MathUtils.random(0,3);
        switch (rand) {
            case 0:
                return SMALL;
            case 1:
                return MEDIUM;
            case 2:
                return LARGE;
        }
        return 0;
    }

    public boolean shouldRemove() {
        return remove;
    }

    public void setHit() {
        hit = true;
    }

    public void update(float dt) {
//        this.x += dx * dt;
//        this.y += dy * dt;
        move(dx*dt, dy*dt);

        turn(rotationSpeed * dt);

        setShape();

        // if obstacle gets in remove zone it ll be removed
        if(this.x < -20 || this.x > 1920 /*SpaceShooter.getInstance().width*/ +20 || this.y < -20 || this.y > 1080 /*SpaceShooter.getInstance().height*/ +20)
            remove = true;

        // energy status of obstacle
        if(hit) {
            hit = false;
            energy--;
            if(energy <= 0)
                remove = true;
        }
    }

    // before a obstacle turns check if there is the wall
    private void turn(float turnVal) {

        // save old position
//        float radiansOld = this.radians;

        this.radians += turnVal;

        if(level.checkCollision(this) instanceof Wall || level.checkCollision(this) instanceof HUDWall) {
//            rotationSpeed = -rotationSpeed;
            this.radians -= turnVal;
        }

//            this.radians = radiansOld;
    }

    // checks if the obstacle can move the full lenght. It will be trimmed to allowed length
    private void move(float xDir, float yDir) {

        SpaceObject obj = null;

        // save old position
        float xPrev = this.x;
        float yPrev = this.y;

        // cut decimal and store for next time
        xDir += this.cachedX;
        int xDirTransformed = MathUtils.floor(xDir);
        this.cachedX = xDir - xDirTransformed;

        yDir += this.cachedY;
        int yDirTransformed = MathUtils.floor(yDir);
        this.cachedY = yDir - yDirTransformed;

        // x direction
        int i = 0;
        for(;i < Math.abs(xDirTransformed); i++) {
            this.x = xPrev + Math.signum(xDir) * i;
            obj = level.checkCollision(this);
//            if(obj instanceof Wall) {
//                i -= 1;
//                break;
//            }
        }
        this.x = xPrev + Math.signum(xDir) * i;
//        if(obj instanceof Wall) {
//            if(!((Wall) obj).isHorizontal()) {
//                // bounce off vertical wall by mirroring the x direction
//                this.dx = -1 * this.dx;
//            }
//        }

        // collision with ship
        if(obj instanceof Ship) {
            hitWithinX = true;
        }

        // y direction
        int j = 0;
        for(;j < Math.abs(yDirTransformed); j++) {
            this.y = yPrev + Math.signum(yDir) * j;
//            obj = level.checkCollision(this);
//            if(obj instanceof Wall) {
//                j -= 1;
//                break;
//            }
        }
        this.y = yPrev + Math.signum(yDir) * j;
//        if(obj instanceof Wall) {
//            if (((Wall) obj).isHorizontal()) {
//                // bounce of vertical walls by mirroring the y direction
//                    this.dy = -1 * this.dy;
//            }
//        }

        // collision with ship
        if(obj instanceof Ship) {
            if(hitWithinX) {
                ((Ship) obj).setHit(this);
                remove = true;
            }
        }
    }

    public void render(ShapeRenderer sr) {
        sr.setColor(1,1,1,1);

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.polygon(obstacleBounds);
        sr.end();
    }

    @Override
    public Polygon getBounds() {
        setShape();
        return obstacle;
    }
}
