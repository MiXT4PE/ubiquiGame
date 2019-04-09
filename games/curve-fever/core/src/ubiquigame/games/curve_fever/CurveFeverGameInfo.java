package ubiquigame.games.curve_fever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ubiquigame.common.GameInfo;

public class CurveFeverGameInfo implements GameInfo {

	@Override
	public String getDescription() {
		return "A clone of the classic curvefever.com game. If you collide with any trace you die.";
	}

	@Override
	public Image getThumbnail() {
		return new Image(new Texture(Gdx.files.internal("curvefever/thumbnail.png")));
	}

	@Override
	public Image getTutorialManual() {
		return new Image(new Texture(Gdx.files.internal("curvefever/thumbnail.png")));
	}

	@Override
	public String getGameTitle() {
		return "Curve Fever";
	}

}
