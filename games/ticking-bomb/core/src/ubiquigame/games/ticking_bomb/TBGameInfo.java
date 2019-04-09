package ubiquigame.games.ticking_bomb;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ubiquigame.common.GameInfo;

public class TBGameInfo implements GameInfo {
    private Image thumbnail;
    private Image tutorialManual;

    public TBGameInfo(){
        this.thumbnail = new Image(new Texture("ticking_bomb/thumbnail.png"));
        this.tutorialManual = new Image(new Texture("ticking_bomb/tutorialManual.png"));
    }

    @Override
    public String getGameTitle() {
        return "Ticking Bomb";
    }

    @Override
    public String getDescription() {
        return "In this game you have to care about your life, a bomb is given around while it is ticking!";
    }

    @Override
    public Image getThumbnail() {
        return this.thumbnail;
    }

    @Override
    public Image getTutorialManual() {
        return this.tutorialManual;
    }

}
