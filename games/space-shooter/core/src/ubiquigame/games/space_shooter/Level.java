package ubiquigame.games.space_shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import ubiquigame.common.Player;
import ubiquigame.games.space_shooter.game_objects.*;

import java.util.ArrayList;
import java.util.Stack;

public class Level {

    private SpaceShooter spaceShooter = SpaceShooter.getInstance();

    private final int WIDTH = 1920; // spaceShooter.width;
    private int HEIGHT = 1080; // spaceShooter.height;

    private ArrayList<Ship> ships;
    private int numPlayers;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Wall> walls;
    private ArrayList<HUDWall> hudWalls;
    private Stack<Player> ranking;

    // no static final because it will be changed later during the game to max = 100
    private int numSpawn = 50;

    private GameOverListener gameOverListener;

    public Level(GameOverListener gameOverListener){
        this.gameOverListener = gameOverListener;

        ships = new ArrayList<>();
        obstacles = new ArrayList<>();
        walls = new ArrayList<>();
        hudWalls = new ArrayList<>();
    }

    public void create() {
        // read coordinates of wall and hud wall from json file
        JsonReader jr = new JsonReader();
        JsonValue jv = jr.parse(Gdx.files.internal("space-shooter/level/level.json"));
        JsonValue level = jv.get(2);
        for (int i = 0; i < level.size; i++) {
            JsonValue curr = level.get(i);
            walls.add(new Wall(
                    this,
                    curr.getFloat("x"),
                    curr.getFloat("y"),
                    curr.getInt("width"),
                    curr.getInt("height")
            ));
        }
        JsonValue hud = jv.get(1);
        for (int i = 0; i < hud.size; i++) {
            JsonValue curr = hud.get(i);
            hudWalls.add(new HUDWall(
                    this,
                    curr.getFloat("x"),
                    curr.getFloat("y"),
                    curr.getInt("width"),
                    curr.getInt("height")
            ));
        }

        for (int i = 0; i < numSpawn; i++) {
            Vector2 randXY = randXY();
            obstacles.add(new Obstacle(this, randXY.x, randXY.y, Obstacle.randSize()));
        }

        ranking = new Stack<Player>();
    }

    /**
     * checks collision with given spaceObject
     * @param spaceObject
     * @return intersecting spaceObject
     */
    public SpaceObject checkCollision(SpaceObject spaceObject) {
        if(spaceObject instanceof Ship || spaceObject instanceof Obstacle || spaceObject instanceof Bullet) {

            if(spaceObject instanceof Ship)
                hashCode();

            for (int i = 0; i < walls.size(); i++) {
                if (Intersector.overlapConvexPolygons(walls.get(i).getBounds(), spaceObject.getBounds()))
                    return walls.get(i);
            }
            for (int i = 0; i < hudWalls.size(); i++) {
                if (Intersector.overlapConvexPolygons(hudWalls.get(i).getBounds(), spaceObject.getBounds()))
                    return hudWalls.get(i);
            }
        }

        if(spaceObject instanceof Bullet) {
            for (int i = 0; i < obstacles.size(); i++) {
                if (Intersector.overlapConvexPolygons(obstacles.get(i).getBounds(), spaceObject.getBounds()))
                    return obstacles.get(i);
            }
        }

        if(spaceObject instanceof Obstacle || spaceObject instanceof Bullet) {
            for (int i = 0; i < ships.size(); i++) {
                if (Intersector.overlapConvexPolygons(ships.get(i).getBounds(), spaceObject.getBounds()))
                    return ships.get(i);
            }
        }
        return null;
    }

    // create x and y coordinates for obstacles within x € [-20,0] u [width, width+20] and y € [-20,0] u [height, height+20]
    public Vector2 randXY() {
        float x = 0;
        float y = 0;
        int range = MathUtils.random(0,4);
        switch(range) {
            case 0:
                x = MathUtils.random(-20, 0);
                y = MathUtils.random(-20, HEIGHT + 21);
                break;
            case 1:
                x = MathUtils.random(WIDTH, WIDTH+21);
                y = MathUtils.random(-20, HEIGHT + 21);
                break;
            case 2:
                x = MathUtils.random(-20,WIDTH+21);
                y = MathUtils.random(-20, 0);
                break;
            case 3:
                x = MathUtils.random(-20,WIDTH+21);
                y = MathUtils.random(HEIGHT, HEIGHT+21);
                break;
        }
        return new Vector2(x,y);
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }

    public void update(float dt) {

        for(int i = ships.size()-1; i >= 0; i--) {
            ships.get(i).update(dt);
            if(ships.get(i).getDead()) {
                ranking.push(ships.get(i).getPlayer());
                ships.remove(i);
                // end game if one ship left
                if(ships.size() <= 1) {
                    // put last player on top of the stack
                    ranking.push(ships.get(0).getPlayer());
                    if(gameOverListener != null)
                        gameOverListener.onGameOver(ranking);
                }
            }
        }

        for(int i = obstacles.size()-1; i >= 0; i--) {
            obstacles.get(i).update(dt);
            if(obstacles.get(i).shouldRemove()) {
                obstacles.remove(i);
            }
        }

        if(ships.size() == numPlayers-1) {
            numSpawn = 80;
        }
        if(ships.size() == numPlayers-2) {
            numSpawn = 100;
        }

        // create new obstacles
        if(!(numSpawn == obstacles.size())) {
            Vector2 randXY = randXY();
            obstacles.add(new Obstacle(this, randXY.x, randXY.y, Obstacle.randSize()));
        }
    }
    // call render methods from each game object
    public void render(SpriteBatch sb, ShapeRenderer sr) {

        for(Ship ship : ships) {
            ship.render(sr);
        }

        for(Obstacle obstacle : obstacles) {
            obstacle.render(sr);
        }

        for (Wall wall : walls) {
            wall.render(sb, sr);
        }

        for (HUDWall hudWall : hudWalls) {
            hudWall.render(sb, sr);
        }
    }
}
