package ubiquigame.platform.menu.tournament;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class AbstractGameModeOptionsPartial<T> extends Table {
	protected final T gameModeStrategy;

	public AbstractGameModeOptionsPartial(T gameModeStrategy, Skin skin) {
		super();
		setSkin(skin);
		this.gameModeStrategy = gameModeStrategy;
	}

	public void apply() {

	}

}
