package ubiquigame.games.racer.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import ubiquigame.games.racer.RacerMain;
import ubiquigame.games.racer.states.PlayState;

//Grass Objects or Breakable Wall Objects
public class PolygonObject {
    private Polygon objectPolygon;
    private float[] vertices;
    private Vector2[] verticesObject;
    private Sprite sprite;
    private int type;

    //type 0 = grass, type 1 = breakableWall, type 2 = solid wall
    public PolygonObject(int[] borderX, int[] borderY, Vector2 center, float radius, int type) {
        this.type = type;

        vertices = new float[borderX.length + borderY.length];
        verticesObject = new Vector2[borderX.length];
        for (int i = 0; i < vertices.length / 2; i++){
            vertices [i*2] = borderX[i];
            vertices [i*2 + 1] = RacerMain.getInstance().height - borderY[i];
            verticesObject[i] = new Vector2(borderX[i], RacerMain.getInstance().height - borderY[i]);
        }
        objectPolygon = new Polygon(vertices);
        if (type == 1){
            sprite = new Sprite(new Texture("racer/images/boulder.png"));
            sprite.flip(true, false);
            sprite.setPosition(center.x - sprite.getWidth()/2, center.y - sprite.getHeight()/2);
            sprite.setScale(2 * radius / sprite.getWidth() + 0.05f);
        }
    }

    /*
     * @return Polygon of the grass
     */
    public Polygon getObjectPolygon(){
        return objectPolygon;
    }

    /*
     * @return Vector of the breakableWall vertices
     */
    public Vector2[] getVerticesBreakableWall() {
        return verticesObject;
    }


    /*
     * This Method draws the shape of the map and the texture
     * @param sr to draw shapes
     * @param sb to draw textures
     */
    public void render(ShapeRenderer sr, SpriteBatch sb) {
        if (type == 1){
            sb.begin();
            sprite.draw(sb);
            sb.end();
        }
        /*
        sr.begin(ShapeRenderer.ShapeType.Line);
        if (type == 0) {
            sr.setColor(Color.ORANGE);
            sr.polygon(vertices);
        } else if (type == 1){
            sr.setColor(Color.BROWN);
            sr.polygon(vertices);
        }else{
            sr.setColor(Color.BLACK);
            sr.polygon(vertices);
        }
        sr.end();
        */
    }
}
