package ubiquigame.games.racer.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import ubiquigame.games.racer.RacerMain;
import ubiquigame.games.racer.states.PlayState;

import java.util.ArrayList;

public class Powerup {
    /*
    * Types:
    * 1: boost
    * 2: breakable wall
    * 3: control switch
    * 4: invisibility
    * 5: teleporter
    */
    private int x;
    private int y;
    private int type;
    private Texture texture;
    public static final int RADIUS = 15;
    private final int SWITCH_SECONDS = 3;
    private Sound soundPowerup;

    public Powerup(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        switch (type){
            case 1:
                texture = new Texture("racer/images/powerup_boost.png");
                break;
            case 2:
                texture = new Texture("racer/images/powerup_breakablewall.png");
                break;
            case 3:
                texture = new Texture("racer/images/powerup_controlswitch.png");
                break;
            case 4:
                texture = new Texture("racer/images/powerup_invisibility.png");
                break;
            case 5:
                texture = new Texture("racer/images/powerup_teleport.png");
                break;
        }
        soundPowerup = Gdx.audio.newSound(Gdx.files.internal("racer/sounds/powerup.ogg"));
    }

    public void render(ShapeRenderer sr, SpriteBatch sb) {
        sb.begin();
        sb.draw(texture, x - RADIUS - 5, y - RADIUS - 3, 0, 0, RADIUS * 2 + 10, RADIUS * 2 + 6, 1, 1, 0, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
        sb.end();
        //sr.begin(ShapeRenderer.ShapeType.Line);
        //sr.circle(x, y, RADIUS);
        //sr.end();
    }

    public Vector2 getPosition(){
        return new Vector2(x, y);
    }

    public void doAction(ArrayList<Car> cars, Car car, Map map, PlayState ps){
        soundPowerup.play();
        switch(type){
            case 1: //boost
                car.setPowerupBoost(true);
                break;
            case 2: //breakable wall
                ps.createWalls(3);
                break;
            case 3: //control switch
                ps.doSwitch(SWITCH_SECONDS, true, car);
                break;
            case 4: //invisibility
                for (Car carTemp : cars) {
                    if (carTemp != car){
                        carTemp.setPowerupInvisibility(true);
                    }
                }
                break;
            case 5: //teleporter
                int tempx, tempy;
                boolean valid;
                do{
                    valid = true;
                    tempx = (int)(Math.random() * RacerMain.getInstance().width);
                    tempy = (int)(Math.random() * RacerMain.getInstance().height);
                    if (!map.isPointInMap(cars, tempx, tempy, RADIUS)){
                        valid = false;
                    }
                }while (!valid);
                car.setPosition(tempx, tempy);
                ps.lastZoom = 1;
        }
    }

    public void dispose() {
        texture.dispose();
    }
}
