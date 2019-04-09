package ubiquigame.platform.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;

import ubiquigame.common.GameInfo;
import ubiquigame.platform.FontSuite;
import ubiquigame.platform.helpers.ColorUtil;
import ubiquigame.platform.helpers.Listener;
import ubiquigame.platform.helpers.StyleUtils;
import ubiquigame.platform.mocks.Mocks;

public class TutorialScreen extends AbstractMenuScreen {

	public static final int SCREEN_TIMEOUT = 6;
	private GameInfo gameInfo;
	private float secondsScreenShown = 0;
	private boolean hideRequested = false;
	private Image tutorialManual;
	private Label timerLabel;

	public TutorialScreen(GameInfo gameInfo) {
		super();
		this.gameInfo = gameInfo;
		createUI();
	}

	private void createUI() {
		rootTable.setBackground(ColorUtil.getSolidColor(Color.BLACK));
		tutorialManual = gameInfo.getTutorialManual();
		if (tutorialManual == null) {
			tutorialManual = Mocks.getDummyGames().get(0).getGameInfo().getTutorialManual();
		}
		tutorialManual.setScaling(Scaling.fit);
		timerLabel = new Label(updateTimerText(), skin);
		
		LabelStyle timerFontStyle = StyleUtils.createLabelStyleWithFont(timerLabel.getStyle(), FontSuite.OPEN_SANS_32);
		
		timerFontStyle.fontColor = BG_BOX;
		timerLabel.setStyle(timerFontStyle);
		
		tutorialManual.setZIndex(0);
		rootTable.add(tutorialManual).grow();
		rootTable.setZIndex(0);
		Table overlay = new Table();
		overlay.setFillParent(true);
		overlay.setZIndex(3);
		stage.addActor(overlay);
		overlay.add(timerLabel).bottom().left().pad(20).expand();
		this.stage.addListener(Listener.onKeyDown(Input.Keys.SPACE,inputEvent -> {
			this.secondsScreenShown = 500f;
		}));
		rootTable.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hideRequested = true;
			}

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER) {
					hideRequested = true;
				}
				return true;
			}
		});
	}

	@Override
	public void render(float delta) {
		timerLabel.setText(updateTimerText());
		if (isHideRequested()) {
			menuManager.startGame();
		}
		secondsScreenShown += delta;
		super.render(delta);
	}

	private String updateTimerText() {
		int countdown = (SCREEN_TIMEOUT - Math.round(secondsScreenShown));
		return localize("startsIn") + countdown + "...";
	}

	public boolean isHideRequested() {
		return hideRequested || secondsScreenShown >= SCREEN_TIMEOUT;
	}

	@Override
	public void show() {
		super.show();

	}

}
