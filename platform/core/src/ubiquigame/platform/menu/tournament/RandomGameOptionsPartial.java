package ubiquigame.platform.menu.tournament;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import ubiquigame.platform.session.tournament.gamemode.RandomGameMode;

import static ubiquigame.platform.menu.AbstractMenuScreen.localize;

public class RandomGameOptionsPartial extends AbstractGameModeOptionsPartial<RandomGameMode> {

	private Slider sliderRounds;
	private Label labelRounds;
	private CheckBox multipleTimes;
	public RandomGameOptionsPartial(RandomGameMode gameModeStrategy, Skin skin) {
		super(gameModeStrategy, skin);
		sliderRounds = new Slider(2, 8, 1, false, skin);
		String formatLabel = localize("rounds")+":  %d";
		labelRounds = new Label(String.format(formatLabel, (int)sliderRounds.getValue()), skin);
		multipleTimes = new CheckBox(localize("gamemode.random.option.multiple"), skin);
		this.add(labelRounds);
		this.row();
		this.add(sliderRounds).padBottom(20);
		this.row();
		this.add(multipleTimes);
		sliderRounds.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				gameModeStrategy.setMaxRounds((int)sliderRounds.getValue()); 
				labelRounds.setText(String.format(formatLabel, (int)sliderRounds.getValue()));
				return false;
			}
		});
	}

	
	@Override
	public void apply() {
		gameModeStrategy.setGameMultipleTimesInARow(multipleTimes.isChecked());
		gameModeStrategy.setMaxRounds((int)sliderRounds.getValue()); 
	}

}
