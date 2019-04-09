package ubiquigame.games.square_run.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ubiquigame.games.square_run.SquareRunMain;

public class HUD {
    public Stage stage;
    private Viewport viewport;
    private SquareRunMain squareRunMain;
    //Labels are what is drawn on the HUD
    Label player1;

    public HUD(SpriteBatch sb) {
        squareRunMain = SquareRunMain.getInstance();
        viewport = new FitViewport(squareRunMain.width, squareRunMain.height, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Table table = new Table();
        //Table settings
        table.setFillParent(true);
        table.top();
        //Add table to the stage
        stage.addActor(table);
        //Create Labels
        player1 = new Label("Player 1", new Label.LabelStyle(new BitmapFont(), Color.CORAL));
        //Add labels to the table with the correct settings
        table.add(player1);
    }
}