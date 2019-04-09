package ubiquigame.games.space_shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ubiquigame.common.GameInfo;

public class SpaceShooterInfo implements GameInfo {
    @Override
    public String getGameTitle() {
        return "Space Shooter";
    }

    @Override
    public String getDescription() {
        return "An arcade space shooter with up to four player.";
    }

    @Override
    public Image getThumbnail() {
        return new Image(new Texture(Gdx.files.internal("space-shooter/images/Thumbnail.png")));
    }

    @Override
    public Image getTutorialManual() {
        return new Image(new Texture(Gdx.files.internal("space-shooter/images/SpaceShooterManual.png")));
    }
}
