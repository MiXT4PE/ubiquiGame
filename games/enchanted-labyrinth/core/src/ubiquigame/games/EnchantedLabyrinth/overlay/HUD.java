package ubiquigame.games.EnchantedLabyrinth.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ubiquigame.games.EnchantedLabyrinth.EnchantedLabyrinthGame;

public class HUD {

    public Stage stage;
    private Viewport viewport;
    private EnchantedLabyrinthGame game;
    private Table table;

    public HUD (SpriteBatch sb){
        game = EnchantedLabyrinthGame.getInstance();

        viewport = new FitViewport(game.width, game.height, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        table = new Table();

        //Table settings
        table.setFillParent(true);
        table.top();

        //Add table to the stage
        stage.addActor(table);
    }

    public Label addLabelToTable() {
       Label labelPlayer = new Label("", new Label.LabelStyle(new BitmapFont(), Color.CORAL));
       table.add(labelPlayer);
       return labelPlayer;

    }
}
