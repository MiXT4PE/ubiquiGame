package ubiquigame.games.EnchantedLabyrinth.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ubiquigame.common.GameInfo;

public class EnchantedLabyrinthGameInfo implements GameInfo {

	@Override
	public String getGameTitle() {
		return "Labyrinth";
	}

	@Override
	public String getDescription() {
		return "Find treasure in labyrinth.";
	}

	@Override
	public Image getThumbnail() {
		return new Image(new Texture(Gdx.files.internal("EnchantedLabyrinth/thumbnail.png")));
	}

	@Override
	public Image getTutorialManual() {
		return new Image(new Texture(Gdx.files.internal("EnchantedLabyrinth/EnchantedLabyrinthManual.png")));
	}
}
