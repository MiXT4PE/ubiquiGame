package ubiquigame.games.your_game_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class YourGameLogic {
    //Main instance
    YourMainClass main;
    //TextureManager
    TextureManager tm;
    //Camera
    private OrthographicCamera camera;


    public YourGameLogic(){
        main = YourMainClass.getInstance();
        tm = main.getTextureManager();
        camera = new OrthographicCamera(main.width, main.height);
        camera.setToOrtho(false, main.width, main.height);
    }

    public void render(SpriteBatch sb, ShapeRenderer sr){
        //Default clear call
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Background Color :)
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
        sb.begin();
        sb.draw(tm.getTexture("badlogic"), 0, 0);
        sb.end();
    }

    private void handleInput(){

    }

    public void update(float delta){
        handleInput();
    }

    public void dispose() {
    }
}
