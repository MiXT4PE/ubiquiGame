package ubiquigame.games.square_run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ubiquigame.common.GameInfo;

public class SquareRunGameInfo implements GameInfo {
    @Override
    public String getGameTitle() {
        return "SquareRun";
    }

    @Override
    public String getDescription() {
        return "Run over a lightning square path as fast as possible";
    }

    @Override
    public Image getThumbnail() {
        return new Image(new Texture(Gdx.files.internal("SquareRun/SquaresEmpty.png")));
    }

    @Override
    public Image getTutorialManual() {
        return new Image(new Texture(Gdx.files.internal("SquareRun/SquareRunManual.png")));
    }
}