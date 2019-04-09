package ubiquigame.games.racer.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import ubiquigame.games.racer.states.PlayState;

import java.util.ArrayList;

public class Car {
    //car values
    private Polygon carPolygon;
    private float[] vertices;
    private Color color;
    private int ranking;
    private boolean isRacing;
    private float timerTime;
    private int timeSeconds;
    //movement
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean space;
    //speed
    private final float STANDARD_MAXSPEED = 200;
    private final float STANDARD_ACCELERATION = 500;
    private final float STANDARD_DECELERATION = 250;
    private float maxSpeed;
    private float acceleration;
    private float deceleration;
    //position
    private float x;
    private float y;
    private float dx;
    private float dy;
    //rotation
    private float radians;
    private final float ROTATION_SPEED = 3;
    //vertices
    private float[] shapex;
    private float[] shapey;
    //texture
    private Sprite spriteBody;
    private Sprite spriteOverlay;
    private int TEXTUREWIDTH = 22; // (20 hitbox)
    private int TEXTUREHEIGHT = 37;// (34 Hitbox)
    private float rotation;
    //boost
    private final float BOOST_DURATION = 1;
    private final float BOOST_COOLDOWN = 0.5f;
    private final int BOOST_MAXSPEED = 120;
    private final int BOOST_ACCELERATION = 1000;
    private final int BOOST_DECELERATION = 280;
    private final int NUMBER_BOOSTS = 5;
    private int boostsCounter;
    private int boostState;
    private float timerBoost;
    private boolean timerBoost_running;
    //grass
    private final int GRASS_MAXSPEED = 100;
    private final int GRASS_ACCELERATION = 250;
    private final int GRASS_DECELERATION = 125;
    private boolean isOnGrass;
    private PolygonObject grassTemp;
    //collision with wall
    private final float COLLISION_DELAY = 0.0005f;
    private final int COLLISION_COUNTER = 20;
    private float timerCollision;
    private boolean timerCollision_running;
    private int timerCollision_counter;
    private Vector2 intersectionPoint;
    private Vector2 tempPoint;
    //rounds
    private final int ROUNDS = 2;
    private boolean firstRound;
    private boolean isOnFinishLine;
    private int sideIn;
    private int round;
    //control switch
    private boolean timerSwitch_running;
    //boost track
    private ArrayList <Vector2> boostTrack;
    private final int BOOSTTRACK_LENGTH = 100;
    //powerups
    private boolean powerup_boost;
    private float timer_invisibility;
    private boolean timer_invisibility_running;
    private final float TIMER_INVISIBILITY_DURATION = 2;


    /*
     * Constructor of the car to set default values
     * @param x - the x-value of the start position
     * @param y - the y-value of the start position
     */
    public Car(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        ranking = 0;
        isRacing = false;
        timerTime = 0;
        timeSeconds = 0;

        vertices = new float[8];
        carPolygon = new Polygon();

        maxSpeed = STANDARD_MAXSPEED;
        acceleration = STANDARD_ACCELERATION;
        deceleration = STANDARD_DECELERATION;

        shapex = new float[4];
        shapey = new float[4];

        radians = 3.1415f / 2 + ROTATION_SPEED / 2;

        spriteBody = new Sprite(new Texture("racer/images/carBody.png"));
        spriteOverlay = new Sprite(new Texture("racer/images/carOverlay.png"));

        rotation = 86;

        boostsCounter = NUMBER_BOOSTS;
        timerBoost_running = false;
        boostState = 0;

        isOnGrass = false;
        grassTemp = new PolygonObject(new int[]{0,0,0}, new int[]{0,0,0}, null, 0, 0);

        timerCollision = 0;
        timerCollision_running = false;
        timerCollision_counter = 0;
        intersectionPoint = new Vector2();
        tempPoint = new Vector2();

        firstRound = true;
        isOnFinishLine = false;
        round = ROUNDS;

        timerSwitch_running = false;

        boostTrack = new ArrayList<>();
        powerup_boost = false;
        timer_invisibility = 0;
        timer_invisibility_running = false;
    }

    /*
     * Sets the Shape of the Car in Dependency to the new radian
     */
    private void setShape(){
        shapex[0] = x + MathUtils.cos(radians - 3.1415f / 6) * 20;
        shapey[0] = y + MathUtils.sin(radians - 3.1415f / 6) * 20;

        shapex[1] = x + MathUtils.cos(radians - 3.1415f / 6 - 2 * 3.1415f / 3) * 20;
        shapey[1] = y + MathUtils.sin(radians - 3.1415f / 6 - 2 * 3.1415f / 3) * 20;

        shapex[2] = x + MathUtils.cos(radians + 3.1415f / 6 + 2 * 3.1415f / 3) * 20;
        shapey[2] = y + MathUtils.sin(radians + 3.1415f / 6 + 2 * 3.1415f / 3) * 20;

        shapex[3] = x + MathUtils.cos(radians + 3.1415f / 6) * 20;
        shapey[3] = y + MathUtils.sin(radians + 3.1415f / 6) * 20;

        vertices = new float[]{shapex[0], shapey[0], shapex[1], shapey[1], shapex[2], shapey[2], shapex[3], shapey[3]};
        carPolygon = new Polygon(vertices);
    }

    /*
     * Sets the value of left
     * @param b value if left was pressed
     */
    public void setLeft(boolean b){
        left = b;
    }

    /*
     * Sets the value of left
     * @param b value if right was pressed
     */
    public void setRight(boolean b){
        right = b;
    }

    /*
     * Sets the value of left
     * @param b value if up was pressed
     */
    public void setUp(boolean b){
        up = b;
    }

    /*
     * Sets the value of left
     * @param b value if space was pressed
     */
    public void setSpace(boolean b){
        space = b;
    }

    /*
     * sets the ranking of the car
     * @param ranking
     */
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    /*
     * sets if the car is still racing
     * @param isRacing
     */
    public void setRacing(boolean b) {
        this.isRacing = b;
    }

    /*
     * sets if the control is switched
     * @param control switched
     */
    public void setSwitch(boolean b) {
        this.timerSwitch_running = b;
    }

    /*
     * sets if the car has a powerup_boost
     * @param boost
     */
    public void setPowerupBoost(boolean b) {
        this.powerup_boost = b;
    }

    /*
     * sets if another car hits the powerup invisibility
     * @param invisibility
     */
    public void setPowerupInvisibility(boolean b) {
        this.timer_invisibility_running = b;
        this.timer_invisibility = 0;
    }


    /*
     * sets the position of the car
     * @param x, y coordinates of the car
     */
    public void setPosition(int tempx, int tempy) {
        this.x = tempx;
        this.y = tempy;
    }

    /*
     * returns the x-value of the position
     * @return x
     */
    public int getX() {
        return (int) x;
    }

    /*
     * returns the y-value of the position
     * @return y
     */
    public int getY() {
        return (int) y;
    }

    /*
     * returns the round
     * @return round of the car
     */
    public int getRound(){
        return round;
    }

    /*
     * returns the number of boosts
     * @return number of boosts
     */
    public int getBoosts() {
        return boostsCounter;
    }

    /*
     * returns the ranking number of the car
     * @return ranking
     */
    public int getRanking() {
        return ranking;
    }

    /*
     * returns if the control is switched
     * @return switch
     */
    public boolean getSwitch() {
        return timerSwitch_running;
    }


    /*
     * returns the ranking number of the car
     * @return ranking
     */
    public int getTime() {
        return timeSeconds;
    }

    /*
     * Updates the position, rotation, speed, ... of the car
     * @param dt delta time between two frames
     */
    public void update(float dt) {
        //accelerating
        if (up || timerCollision_running){
            dx += MathUtils.cos(radians) * acceleration * dt;
            dy += MathUtils.sin(radians) * acceleration * dt;
        }
        //turning
        if (left && !timerCollision_running){
            radians += ROTATION_SPEED * dt;
            rotation += 171.89 * dt;

        }else if (right && !timerCollision_running){
            radians -= ROTATION_SPEED * dt;
            rotation -= 171.89 * dt;
        }
        //deceleration
        float vec = (float) Math.sqrt(dx * dx + dy * dy);
        if (vec > 0){
            dx -= (dx / vec) * deceleration * dt;
            dy -= (dy / vec) * deceleration * dt;
        }
        if (vec > maxSpeed){
            dx = (dx / vec) * maxSpeed;
            dy = (dy / vec) * maxSpeed;
        }
        //set position
        x += dx * dt;
        y += dy * dt;
        //set shape
        setShape();
        //Boost
        checkBoost();
        //Timer update
        if (isRacing){
            timerTime += dt;
            if (timerTime >= 1){
                timerTime = timerTime - 1;
                timeSeconds++;
            }
        }
        if (timerBoost_running){
            timerBoost += dt;
            if (boostTrack.size() == BOOSTTRACK_LENGTH) {
                boostTrack.remove(0);
                boostTrack.remove(0);
            }
            boostTrack.add(new Vector2(shapex[2] + 0.25f * (shapex[1] - shapex[2]), shapey[2] + 0.25f * (shapey[1] - shapey[2])));
            boostTrack.add(new Vector2(shapex[2] + 0.75f * (shapex[1] - shapex[2]), shapey[2] + 0.75f * (shapey[1] - shapey[2])));
        } else {
            if (boostTrack.size() != 0){
                boostTrack.remove(0);
                boostTrack.remove(0);
            }
        }
        if (timerCollision_running){
            timerCollision += dt;
        }
        if (timer_invisibility_running){
            timer_invisibility += dt;
            if (timer_invisibility >= TIMER_INVISIBILITY_DURATION){
                timer_invisibility_running = false;
                timer_invisibility = 0;
            }
        }
    }

    /*
     * This Method draws the shape of the car and the texture
     * @param sr to draw shapes
     * @param sb to draw textures
     */
    public void render(ShapeRenderer sr, SpriteBatch sb) {
        spriteBody.setFlip(false, true);
        spriteBody.setSize(TEXTUREWIDTH, TEXTUREHEIGHT);
        spriteBody.setOrigin(0, 0);
        spriteBody.setPosition(shapex[2] + (shapex[2] - x) * 0.08f, shapey[2] + (shapey[2] - y) * 0.08f);
        spriteBody.setRotation(rotation);
        spriteBody.setColor(color);
        spriteOverlay.setFlip(false, true);
        spriteOverlay.setSize(TEXTUREWIDTH, TEXTUREHEIGHT);
        spriteOverlay.setOrigin(0, 0);
        spriteOverlay.setPosition(shapex[2] + (shapex[2] - x) * 0.08f, shapey[2] + (shapey[2] - y) * 0.08f);
        spriteOverlay.setRotation(rotation);
        //car
        if (!timer_invisibility_running){
            sb.begin();
            spriteBody.draw(sb);
            spriteOverlay.draw(sb);
            sb.end();
        }
        //car hitbox
        sr.begin(ShapeRenderer.ShapeType.Line);
        //sr.setColor(Color.BLUE);
        //sr.polygon(vertices);
        //boost track
        sr.setColor(color);
        for (int i = 0; i < boostTrack.size(); i++){
            sr.point(boostTrack.get(i).x, boostTrack.get(i).y, 0);
            sr.point(boostTrack.get(i).x + 1, boostTrack.get(i).y + 1, 0);
            sr.point(boostTrack.get(i).x + 1, boostTrack.get(i).y - 1, 0);
            sr.point(boostTrack.get(i).x - 1, boostTrack.get(i).y + 1, 0);
            sr.point(boostTrack.get(i).x - 1, boostTrack.get(i).y - 1, 0);

            sr.point(boostTrack.get(i).x + 2, boostTrack.get(i).y, 0);
            sr.point(boostTrack.get(i).x, boostTrack.get(i).y + 2, 0);
            sr.point(boostTrack.get(i).x - 2, boostTrack.get(i).y, 0);
            sr.point(boostTrack.get(i).x, boostTrack.get(i).y - 2, 0);
        }
        sr.end();
    }

    /*
     * Checks if the player boosted and adjust speed values
     */
    private void checkBoost(){
        if ((space && boostsCounter > 0 && !timerBoost_running && boostState == 0 && !timerCollision_running) || powerup_boost){
            if (!powerup_boost){
                boostsCounter--;
            }else{
                if (isOnGrass){
                    maxSpeed = GRASS_MAXSPEED;
                    acceleration = GRASS_ACCELERATION;
                    deceleration = GRASS_DECELERATION;
                }else{
                    maxSpeed = STANDARD_MAXSPEED;
                    acceleration = STANDARD_ACCELERATION;
                    deceleration = STANDARD_DECELERATION;
                }
                boostState = 0;
                timerBoost = 0;
            }
            powerup_boost = false;
            timerBoost_running = true;
            maxSpeed += BOOST_MAXSPEED;
            acceleration += BOOST_ACCELERATION;
            deceleration += BOOST_DECELERATION;
        }
        if (timerBoost > BOOST_DURATION && boostState == 0){
            maxSpeed -= BOOST_MAXSPEED;
            acceleration -= BOOST_ACCELERATION;
            deceleration -= BOOST_DECELERATION;
            timerBoost = 0;
            boostState = 1;
        }
        if (timerBoost > BOOST_COOLDOWN && boostState == 1){
            boostState = 0;
            timerBoost_running = false;
            timerBoost = 0;
        }
    }

    /*
     * Checks the collision with grass and adjust values
     * @param map the map with grassObjects
     */
    public void checkGrassCollision(Map map){
        for (PolygonObject grass: map.getGrassObjects()) {
            if (Intersector.overlapConvexPolygons(carPolygon, grass.getObjectPolygon()) && !isOnGrass){
                grassTemp = grass;
                break;
            }
        }
        if (Intersector.overlapConvexPolygons(carPolygon, grassTemp.getObjectPolygon()) && !isOnGrass) {
            isOnGrass = true;
            maxSpeed -= GRASS_MAXSPEED;
            acceleration -= GRASS_ACCELERATION;
            deceleration -= GRASS_DECELERATION;
        }else if (!Intersector.overlapConvexPolygons(carPolygon, grassTemp.getObjectPolygon()) && isOnGrass){
            isOnGrass = false;
            maxSpeed += GRASS_MAXSPEED;
            acceleration += GRASS_ACCELERATION;
            deceleration += GRASS_DECELERATION;
        }
    }

    /*
     * Checks the collision with map and adjust values
     * @param map the map with map borders
     */
    public void checkWallCollision(Map map, float dt){
        //outer border
        for (int i = 0; i < map.getVerticesOuterBorder().length; i++){
            if (i == map.getVerticesOuterBorder().length - 1){
                if (Intersector.intersectSegmentPolygon(map.getVerticesOuterBorder()[map.getVerticesOuterBorder().length - 1], map.getVerticesOuterBorder()[0], carPolygon)){
                    collisionWall(map.getVerticesOuterBorder()[map.getVerticesOuterBorder().length - 1], map.getVerticesOuterBorder()[0]);
                    break;
                }
            }else{
                if (Intersector.intersectSegmentPolygon(map.getVerticesOuterBorder()[i], map.getVerticesOuterBorder()[i + 1], carPolygon)){
                    collisionWall(map.getVerticesOuterBorder()[i], map.getVerticesOuterBorder()[i + 1]);
                    break;
                }
            }
        }
        //inner border
        for (int i = 0; i < map.getVerticesInnerBorder().length; i++){
            if (i == map.getVerticesInnerBorder().length - 1){
                if (Intersector.intersectSegmentPolygon(map.getVerticesInnerBorder()[map.getVerticesInnerBorder().length - 1], map.getVerticesInnerBorder()[0], carPolygon)){
                    collisionWall(map.getVerticesInnerBorder()[map.getVerticesInnerBorder().length - 1], map.getVerticesInnerBorder()[0]);
                    break;
                }
            }else{
                if (Intersector.intersectSegmentPolygon(map.getVerticesInnerBorder()[i], map.getVerticesInnerBorder()[i + 1], carPolygon)){
                    collisionWall(map.getVerticesInnerBorder()[i], map.getVerticesInnerBorder()[i + 1]);
                    break;
                }
            }
        }
        //wallIslands
        for (int j = 0; j < map.getWallIslands().size(); j++){
            for (int i = 0; i < map.getWallIslands().get(j).getVerticesBreakableWall().length; i++){
                if (i == map.getWallIslands().get(j).getVerticesBreakableWall().length - 1){
                    if (Intersector.intersectSegmentPolygon(map.getWallIslands().get(j).getVerticesBreakableWall()[map.getWallIslands().get(j).getVerticesBreakableWall().length - 1], map.getWallIslands().get(j).getVerticesBreakableWall()[0], carPolygon)){
                        collisionWall(map.getWallIslands().get(j).getVerticesBreakableWall()[map.getWallIslands().get(j).getVerticesBreakableWall().length - 1], map.getWallIslands().get(j).getVerticesBreakableWall()[0]);
                        break;
                    }
                }else{
                    if (Intersector.intersectSegmentPolygon(map.getWallIslands().get(j).getVerticesBreakableWall()[i], map.getWallIslands().get(j).getVerticesBreakableWall()[i + 1], carPolygon)){
                        collisionWall(map.getWallIslands().get(j).getVerticesBreakableWall()[i], map.getWallIslands().get(j).getVerticesBreakableWall()[i + 1]);
                        break;
                    }
                }
            }
        }
        //breakableWall
        for (int j = 0; j < map.getBreakableWallObjects().size(); j++){
            for (int i = 0; i < map.getBreakableWallObjects().get(j).getVerticesBreakableWall().length; i++){
                if (i == map.getBreakableWallObjects().get(j).getVerticesBreakableWall().length - 1){
                    if (Intersector.intersectSegmentPolygon(map.getBreakableWallObjects().get(j).getVerticesBreakableWall()[map.getBreakableWallObjects().get(j).getVerticesBreakableWall().length - 1], map.getBreakableWallObjects().get(j).getVerticesBreakableWall()[0], carPolygon)){
                        collisionWall(map.getBreakableWallObjects().get(j).getVerticesBreakableWall()[map.getBreakableWallObjects().get(j).getVerticesBreakableWall().length - 1], map.getBreakableWallObjects().get(j).getVerticesBreakableWall()[0]);
                        map.removeBreakableWall(j);
                        break;
                    }
                }else{
                    if (Intersector.intersectSegmentPolygon(map.getBreakableWallObjects().get(j).getVerticesBreakableWall()[i], map.getBreakableWallObjects().get(j).getVerticesBreakableWall()[i + 1], carPolygon)){
                        collisionWall(map.getBreakableWallObjects().get(j).getVerticesBreakableWall()[i], map.getBreakableWallObjects().get(j).getVerticesBreakableWall()[i + 1]);
                        map.removeBreakableWall(j);
                        break;
                    }
                }
            }
        }
        //timerCollision
        if (timerCollision_running){
            collisionTimerRun(dt);
        }
    }

    /*
     * Checks the collision with other cars and adjust values
     * @param map the map with map borders
     */
    public void checkCarCollision(ArrayList<Car> cars, Car car){
        for (int j = 0; j < cars.size(); j++){
            for (int i = 0; i < 4; i++){
                if (i == 3){
                    if (cars.get(j) != car && Intersector.intersectSegmentPolygon(new Vector2(cars.get(j).shapex[3], cars.get(j).shapey[3]), new Vector2(cars.get(j).shapex[0], cars.get(j).shapey[0]), carPolygon)){
                        collisionCar(cars.get(j));
                        break;
                    }
                }else{
                    if (cars.get(j) != car && Intersector.intersectSegmentPolygon(new Vector2(cars.get(j).shapex[i], cars.get(j).shapey[i]), new Vector2(cars.get(j).shapex[i + 1], cars.get(j).shapey[i + 1]), carPolygon)){
                        collisionCar(cars.get(j));
                        break;
                    }
                }
            }
        }
    }

    /*
     * adjust values on wall collision
     */
    private void collisionWall(Vector2 p1, Vector2 p2){
        timerCollision_running = true;
        maxSpeed = 0;
        acceleration = 0;
        deceleration = 0;
        //get intersectionPoint
        for (int i = 0; i < 4; i++){
            Vector2 tempP1;
            Vector2 tempP2;
            if (i == 3){
                tempP1 = new Vector2(shapex[3], shapey[3]);
                tempP2 = new Vector2(shapex[0], shapey[0]);
            }else{
                tempP1 = new Vector2(shapex[i], shapey[i]);
                tempP2 = new Vector2(shapex[i + 1], shapey[i + 1]);
            }

            if (Intersector.intersectSegments(p1, p2, tempP1, tempP2, intersectionPoint)){
                tempPoint.x = intersectionPoint.x;
                tempPoint.y = intersectionPoint.y;
                break;
            }
        }
    }

    /*
     * adjust values on car collision
     * @param car of the collision
     */
    private void collisionCar(Car car){
        timerCollision_running = true;
        maxSpeed = 0;
        acceleration = 0;
        deceleration = 0;
        tempPoint.x = car.getX() + (x - car.getX()) * 0.6f;
        tempPoint.y = car.getY() + (y - car.getY()) * 0.6f;
    }

    /*
     * this function is doing the collision
     * @param dt delta time
     */
    private void collisionTimerRun(float dt){
        //move car in right direction
        if (timerBoost_running){
            timerBoost_running = false;
            timerBoost = 0;
            boostState = 0;
        }
        if (timerCollision > COLLISION_DELAY){
            timerCollision = 0;
            timerCollision_counter++;
            if (timerCollision_counter < COLLISION_COUNTER){
                x = x + (x - tempPoint.x) * dt * 1.5f;
                y = y + (y - tempPoint.y) * dt * 1.5f;
            }else{
                if (isOnGrass){
                    maxSpeed = GRASS_MAXSPEED;
                    acceleration = GRASS_ACCELERATION;
                    deceleration = GRASS_DECELERATION;
                }else{
                    maxSpeed = STANDARD_MAXSPEED;
                    acceleration = STANDARD_ACCELERATION;
                    deceleration = STANDARD_DECELERATION;
                }
                timerCollision_running = false;
                timerCollision_counter = 0;
            }
        }
    }

    /*
     * update rounds
     * @param map
     */
    public void updateRound(Map map) {
        if (Intersector.intersectSegmentPolygon(new Vector2(map.getFinishLine().a.x, map.getFinishLine().a.y), new Vector2(map.getFinishLine().b.x, map.getFinishLine().b.y), carPolygon) && !isOnFinishLine){
            isOnFinishLine = true;
            sideIn = Intersector.pointLineSide(new Vector2(map.getFinishLine().a.x, map.getFinishLine().a.y), new Vector2(map.getFinishLine().b.x, map.getFinishLine().b.y), new Vector2(x, y));
        }else if(!Intersector.intersectSegmentPolygon(new Vector2(map.getFinishLine().a.x, map.getFinishLine().a.y), new Vector2(map.getFinishLine().b.x, map.getFinishLine().b.y), carPolygon) && isOnFinishLine) {
            int sideOut = Intersector.pointLineSide(new Vector2(map.getFinishLine().a.x, map.getFinishLine().a.y), new Vector2(map.getFinishLine().b.x, map.getFinishLine().b.y), new Vector2(x, y));
            if (round != 0){
                if (sideIn == -1 && sideOut == 1){
                    round++;
                    boostsCounter -= NUMBER_BOOSTS;
                    if (boostsCounter < 0){
                        boostsCounter = 0;
                    }
                }else if (sideIn == 1 && sideOut == -1){
                    if (!firstRound){
                        boostsCounter += NUMBER_BOOSTS;
                        round--;
                    }
                }
            }
            firstRound = false;
            isOnFinishLine = false;
        }
    }

    public ArrayList<Powerup> checkPowerupCollision(ArrayList<Car> cars, ArrayList<Powerup> powerups, Map map, PlayState ps) {
        loop: for (Powerup powerup : powerups){
            for (int i = 0; i < 4; i++){
                if (i == 3){
                    if (Intersector.intersectSegmentCircle(new Vector2(shapex[3], shapey[3]), new Vector2(shapex[0], shapey[0]), powerup.getPosition(), (float) Math.pow(Powerup.RADIUS, 2))){
                        powerup.doAction(cars, this, map, ps);
                        powerups.remove(powerup);
                        break loop;
                    }
                }else{
                    if (Intersector.intersectSegmentCircle(new Vector2(shapex[i], shapey[i]), new Vector2(shapex[i + 1], shapey[i + 1]), powerup.getPosition(),(float) Math.pow(Powerup.RADIUS, 2))){
                        powerup.doAction(cars, this, map, ps);
                        powerups.remove(powerup);
                        break loop;
                    }
                }
            }
        }
        return powerups;
    }


}