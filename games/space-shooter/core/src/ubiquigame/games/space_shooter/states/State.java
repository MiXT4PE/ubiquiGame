package ubiquigame.games.space_shooter.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.games.space_shooter.SpaceShooter;

public abstract class State {
    protected OrthographicCamera camera;
    protected GameStateManager gsm;

    protected final SpaceShooter spaceShooter = SpaceShooter.getInstance();
    protected final UbiquiGamePlatform platform = spaceShooter.getPlatformInstance();

    public State(GameStateManager gsm){
        this.gsm = gsm;
        camera = new OrthographicCamera(spaceShooter.width, spaceShooter.height);
    }

    /*
    * Handles any input done by the player
    */
    public abstract void handleInput();

    /*
     * Updates the camera and should handle inputs
     * @param dt the difference in time between last update and the one now
     */
    public abstract void update(float dt);

    /*
     * Handles all drawings on the SpriteBatch
     * @param sb Spritebatch to be drawn on
     * @param sr ShapeRenderer to be drawn on
     */
    public abstract void render(SpriteBatch sb, ShapeRenderer sr);

    public abstract void dispose();
}
