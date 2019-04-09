package ubiquigame.games.racer.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;
import ubiquigame.games.racer.RacerMain;
import ubiquigame.games.racer.states.PlayState;

import java.util.ArrayList;

public class Map {
    private Sprite sprite;
    private float[] verticesOuter;
    private float[] verticesInner;
    private Vector2[] verticesOuterBorder;
    private Vector2[] verticesInnerBorder;
    private PolygonObject[] grassObjects;
    private Segment finishLine;
    private ArrayList<PolygonObject> breakableWallObjects;
    private ArrayList<PolygonObject> wallIslands;

    /*
     * Constructor of the map to set default values
     */
    public Map(int[] borderOuterX, int[] borderOuterY, int[] borderInnerX, int[] borderInnerY, PolygonObject[] grassObjects, Segment finishLine, ArrayList<PolygonObject> breakableWallObjects, ArrayList<PolygonObject> wallIslands) {
        this.sprite = new Sprite(new Texture("racer/images/map.png"));
        //sprite = new Sprite(texture);
        this.grassObjects = grassObjects;
        //outer border
        verticesOuter = new float[borderOuterX.length + borderOuterY.length];
        verticesOuterBorder = new Vector2[borderOuterX.length];
        for (int i = 0; i < verticesOuter.length / 2; i++){
            verticesOuter[i*2] = borderOuterX[i];
            verticesOuter[i*2 + 1] = RacerMain.getInstance().height - borderOuterY[i];
            verticesOuterBorder[i] = new Vector2(borderOuterX[i], RacerMain.getInstance().height - borderOuterY[i]);
        }
        //inner border
        verticesInner = new float[borderInnerX.length + borderInnerY.length];
        verticesInnerBorder = new Vector2[borderInnerX.length];
        for (int i = 0; i < verticesInner.length / 2; i++){
            verticesInner[i*2] = borderInnerX[i];
            verticesInner[i*2 + 1] = RacerMain.getInstance().height - borderInnerY[i];
            verticesInnerBorder[i] = new Vector2(borderInnerX[i], RacerMain.getInstance().height - borderInnerY[i]);
        }
        //finishLine
        this.finishLine = new Segment(new Vector3(finishLine.a.x, RacerMain.getInstance().height - finishLine.a.y, finishLine.a.z), new Vector3(finishLine.b.x, RacerMain.getInstance().height - finishLine.b.y, finishLine.b.z));
        //breakableWall
        this.breakableWallObjects = breakableWallObjects;
        this.wallIslands = wallIslands;
    }

    /*
     * @return Vector of the outer border vertices
     */
    public Vector2[] getVerticesOuterBorder() {
        return verticesOuterBorder;
    }

    /*
     * @return Vector of the inner border vertices
     */
    public Vector2[] getVerticesInnerBorder() {
        return verticesInnerBorder;
    }

    /*
     * @return PolygonObject-Array of the GrassObjects
     */
    public PolygonObject[] getGrassObjects() {
        return grassObjects;
    }

    /*
     * @return PolygonObject-Array of the GrassObjects
     */
    public ArrayList<PolygonObject> getBreakableWallObjects() {
        return breakableWallObjects;
    }

    /*
     * @return PolygonObject-Array of the GrassObjects
     */
    public ArrayList<PolygonObject> getWallIslands() {
        return wallIslands;
    }

    /*
     * @return PolygonObject-Array of the GrassObjects
     */
    public Segment getFinishLine() {
        return finishLine;
    }

    /*
     * This Method draws the shape of the map and the texture
     * @param sr to draw shapes
     * @param sb to draw textures
     */
    public void render(ShapeRenderer sr, SpriteBatch sb) {
        //map
        //int y = PlayState.SHIFT_Y;
        //if (((float) RacerMain.getInstance().width / (float) RacerMain.getInstance().height) < 1.8f && ((float) RacerMain.getInstance().width / (float) RacerMain.getInstance().height) > 1.75f){
        //    y -= 25;
        //}
        sprite.setPosition(- PlayState.SHIFT_X, - 1080 + RacerMain.getInstance().height - PlayState.SHIFT_Y + 20);
        sb.begin();
        sprite.draw(sb);
        sb.end();
        //Outer Polygon
        //sr.begin(ShapeRenderer.ShapeType.Line);
        //sr.setColor(Color.BLACK);
        //sr.polygon(verticesOuter);
        //Inner Polygon
        //sr.setColor(Color.BLACK);
        //sr.polygon(verticesInner);
        //finishLine
        //sr.setColor(Color.BLACK);
        //sr.line(finishLine.a, finishLine.b);
        //sr.end();
    }

    public void removeBreakableWall(int j) {
        breakableWallObjects.remove(j);
    }

    public Polygon getPolygonOuterBorder(){
        float[] outerBorder = new float[verticesOuter.length + 12];
        for(int i = 0; i < verticesOuter.length; i++){
            outerBorder[i] = verticesOuter[i];
        }
        outerBorder[verticesOuter.length] = verticesOuter[0];
        outerBorder[verticesOuter.length + 1] = RacerMain.getInstance().height + 1;
        outerBorder[verticesOuter.length + 2] = RacerMain.getInstance().width + 1;
        outerBorder[verticesOuter.length + 3] = RacerMain.getInstance().height + 1;
        outerBorder[verticesOuter.length + 4] = RacerMain.getInstance().width + 1;
        outerBorder[verticesOuter.length + 5] = -1;
        outerBorder[verticesOuter.length + 6] = -1;
        outerBorder[verticesOuter.length + 7] = -1;
        outerBorder[verticesOuter.length + 8] = -1;
        outerBorder[verticesOuter.length + 9] = RacerMain.getInstance().height + 1;
        outerBorder[verticesOuter.length + 10] = verticesOuter[0];
        outerBorder[verticesOuter.length + 11] = RacerMain.getInstance().height + 1;
        return new Polygon(outerBorder);
    }

    public Polygon getPolygonInnerBorder(){
        return new Polygon(verticesInner);
    }


    public void setBreakableWallObject(int[] borderX, int[] borderY, Vector2 center, float radius, int type){
        breakableWallObjects.add(new PolygonObject(borderX, borderY, center, radius, type));
    }

    /*
     * this Function checks if the point is colliding with objects of the map
     * @return true if the point is on track
     */
    public boolean isPointInMap(ArrayList<Car> cars, int tempx, int tempy, float distance){
        //Point in Polygon
        if (Intersector.isPointInPolygon(this.getPolygonOuterBorder().getVertices(), 0, this.getPolygonOuterBorder().getVertices().length, tempx, tempy) ||
                Intersector.isPointInPolygon(this.getPolygonInnerBorder().getVertices(), 0, this.getPolygonInnerBorder().getVertices().length, tempx, tempy)){
            return false;
        }

        //distance < RADIUS outerBorder
        Vector2[] vertices = this.getVerticesOuterBorder();
        if (!isDistanceNotSmalerThanDistance(vertices, tempx, tempy, distance)){
            return false;
        }

        //distance < RADIUS innerBorder
        vertices = this.getVerticesInnerBorder();
        if (!isDistanceNotSmalerThanDistance(vertices, tempx, tempy, distance)){
            return false;
        }

        //Point in Breakable Wall or distance < RADIUS
        for (PolygonObject breakableWall : this.getBreakableWallObjects()){
            if (Intersector.isPointInPolygon(breakableWall.getObjectPolygon().getVertices(), 0, breakableWall.getObjectPolygon().getVertices().length, tempx, tempy)){
                return false;
            }
            //distance min RADIUS
            vertices = breakableWall.getVerticesBreakableWall();
            if (!isDistanceNotSmalerThanDistance(vertices, tempx, tempy, distance)){
                return false;
            }
        }

        //Point in Breakable Wall or distance < RADIUS
        for (PolygonObject wallIsland : this.getWallIslands()){
            if (Intersector.isPointInPolygon(wallIsland.getObjectPolygon().getVertices(), 0, wallIsland.getObjectPolygon().getVertices().length, tempx, tempy)){
                return false;
            }
            //distance min RADIUS
            vertices = wallIsland.getVerticesBreakableWall();
            if (!isDistanceNotSmalerThanDistance(vertices, tempx, tempy, distance)){
                return false;
            }
        }

        //distance car bigger than 2.5*RADIUS
        for (Car car : cars){
            if (Math.sqrt(Math.pow(tempx - car.getX(), 2) + Math.pow(tempy - car.getY(), 2)) <= 2.5f * distance){
                return false;
            }
        }
        return true;
    }

    public boolean isDistanceNotSmalerThanDistance(Vector2[] vertices, int tempx, int tempy, float distance){
        for (int i = 0; i < vertices.length; i++){
            if (i == vertices.length - 1){
                if (Intersector.distanceSegmentPoint(vertices[i], vertices[0], new Vector2(tempx, tempy)) <= distance + 2){
                    return false;
                }
            }else{
                if (Intersector.distanceSegmentPoint(vertices[i], vertices[i + 1], new Vector2(tempx, tempy)) <= distance + 2){
                    return false;
                }
            }
        }
        return true;
    }
}
