package ubiquigame.platform.menu.tournament;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import ubiquigame.platform.session.tournament.gamemode.AllGamesGameMode;

public class AllGamesOptionsPartial extends AbstractGameModeOptionsPartial<AllGamesGameMode> {

	public AllGamesOptionsPartial(AllGamesGameMode gameModeStrategy, Skin skin) {
		super(gameModeStrategy, skin);
		this.add(new Label("test", getSkin()));
	}

}
