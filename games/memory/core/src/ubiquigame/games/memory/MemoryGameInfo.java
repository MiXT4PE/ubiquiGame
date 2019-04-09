package ubiquigame.games.memory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import ubiquigame.common.GameInfo;

public class MemoryGameInfo implements GameInfo {

	private static final Image thumbnail = new Image(new Texture(Gdx.files.internal("memory/tH.png")));
	private static final Image tutorial = new Image(new Texture(Gdx.files.internal("memory/MemoryManual.png")));
	private static final String description = "In This Game a player has to find two matching Pictures  to get one point. "
			+ "A player is able to make as many as matches he wants, but if he has a set of not matching pictures,"
			+ " the next players turn starts.In this game are three modificationes  "
			+ "for player frequency:  two,three or for player modi. The game ends if all cards are opened with their twins."
			+ " Good luck.";

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Image getThumbnail() {
		return thumbnail;
	}

	@Override
	public Image getTutorialManual() {
		return tutorial;
	}

	@Override
	public String getGameTitle() {
		return "Memory";
	}
}
