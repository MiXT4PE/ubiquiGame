package ubiquigame.games.tetris.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ubiquigame.games.tetris.TetrisGame;

import java.util.ArrayList;
import java.util.List;

public class HUD {
    public Stage stage;
    private Viewport viewport;

    private TetrisGame yourMainClass;

    //Labels are what is drawn on the HUD
    Table table = new Table();
    private List<Label> labels = new ArrayList<Label>();

    public HUD (SpriteBatch sb){
        yourMainClass = TetrisGame.getInstance();

        viewport = new FitViewport(yourMainClass.width, yourMainClass.height, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //Table settings
        table.setFillParent(true);
        table.top();

        //Add table to the stage
        stage.addActor(table);
    }

    public void addLabelWithPlayerName(String playerName) {
        BitmapFont bitmapFont = new BitmapFont();
        bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        bitmapFont.getData().setScale(2);
        Label label = new Label(playerName, new Label.LabelStyle(bitmapFont, Color.CORAL));
        labels.add(label);
        table.add(label);
    }
}
