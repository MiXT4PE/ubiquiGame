package ubiquigame.platform.menu;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter.DigitsOnlyFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;

import ubiquigame.common.impl.ScreenDimension;
import ubiquigame.platform.config.PlatformConfiguration;
import ubiquigame.platform.helpers.ColorUtil;
import ubiquigame.platform.helpers.Listener;

public class OptionsScreen extends AbstractMenuScreen {

	private static final ScreenDimension[] RESOLUTIONS = new ScreenDimension[] { //
			new ScreenDimension(1920, 1080), //
			new ScreenDimension(1768, 992), //
			new ScreenDimension(1680, 1050), //
			new ScreenDimension(1600, 1024), //
			new ScreenDimension(1600, 900), //
			new ScreenDimension(1440, 900), //
			new ScreenDimension(1366, 768), //
			new ScreenDimension(1360, 1024), //
			new ScreenDimension(1280, 1024), //
			new ScreenDimension(1280, 960), //
			new ScreenDimension(1280, 800), };

	private ScreenDimension customResolution = null;

	private static final String[] LANGUAGES = new String[] { "de", "en" };

	private Slider masterVolumeSlider;
	private Slider musicVolumeSlider;
	private Slider soundVolumeSlider;

	private Label masterVolumeLabel;
	private Label musicVolumeLabel;
	private Label soundVolumeLabel;

	private Label fullscreenSwitchLabel;
	private Label language;

	private TextField tcpTxtField;

	private TextField udpTxtField;

	private TextField udpBroadTxtField;

	private TextField platformNameTxtField;

	private SelectBox<String> languageBox;

	private Button fullscreenSwitch;

	private SelectBox<String> resolutionSelectBox;

	private final int rollBackDialogWidth = 700;
	private final int rollBackDialogHeight = 200;

	private Cell<ScrollPane> contentCell;

	public OptionsScreen() {
		super();
		initializeUI();
	}

	public OptionsScreen(OptionsModel prevOptions) {
		this();
		showRollbackDialog(prevOptions);
	}

	private void showRollbackDialog(OptionsModel prevOptions) {
		Label timerLabel = new Label("", skin);

		Dialog rollBackDialog = new Dialog(localize("confirmdDisplaySettingsTitle"), skin) {

			private float timeElapsed = 0;
			private final float timeOut = 5;
			private TextureRegionDrawable bg = ColorUtil.getSolidColor(new Color(0, 0, 0, 0.8f));

			protected void result(Object object) {
				if (((int) object) == 0) {
					rollback();
				}
			};

			@Override
			public void act(float delta) {
				if (isVisible()) {
					timeElapsed += delta;
					if (timeElapsed >= timeOut) {
						rollback();
					}
					timerLabel.setText(localize("confirmdDisplaySettingsTitleMessage", (int) (timeOut - timeElapsed)));
				}
				super.act(delta);
			}

			private void rollback() {
				hide();
				prevOptions.apply();
				updateDisplay(prevOptions.getResolution().equals(
						new ScreenDimension(PlatformConfiguration.WINDOW_WIDTH, PlatformConfiguration.WINDOW_HEIGHT)));
				menuManager.showOptionsMenu();
			}

			@Override
			public void draw(Batch batch, float parentAlpha) {
				if (isVisible())
					bg.draw(batch, 0, 0, stage.getWidth(), stage.getHeight());
				super.draw(batch, parentAlpha);
			}

			@Override
			public void hide() {

				super.hide();
			}

			@Override
			public Dialog show(Stage stage) {

				return super.show(stage);
			}
		};
		timerLabel.setWrap(true);
		rollBackDialog.getContentTable().row().grow().size(rollBackDialogWidth, rollBackDialogHeight);
		rollBackDialog.getContentTable().add(timerLabel).top().growX();
		rollBackDialog.getContentTable().row();
		rollBackDialog.button(localize("yes"), 1);
		rollBackDialog.button(localize("no"), 0);

		rollBackDialog.show(stage);
	}

	private String createSliderLabelText(int currValue) {
		return currValue > 0 ? Integer.toString(currValue) : localize("off");
	}

	protected void initializeUI() {
		OptionsModel options = OptionsModel.snapshot();

		createHeader(localize("Options"));
		String udpPort = Integer.toString(options.getPortUDPReceive());
		String tcpPort = Integer.toString(options.getPortTCPReceive());
		String udpBroadPort = Integer.toString(options.getPortUDPBroadcast());
		String name = options.getPlatformName();

		Label audio = new Label(localize("Audio"), skin);

		// master vol slider
		Label labelMaster = new Label(localize("MasterVolume"), skin);
		masterVolumeLabel = new Label("---", skin);
		masterVolumeSlider = createSliderWithLabel(0, 100, 1, masterVolumeLabel);
		masterVolumeSlider.setValue(options.getMasterVolume());
		masterVolumeSlider.fire(new ChangeEvent());

		// music vol slider
		Label labelMusic = new Label(localize("MusicVolume"), skin);
		musicVolumeLabel = new Label("---", skin);
		musicVolumeSlider = createSliderWithLabel(0, 100, 1, musicVolumeLabel);
		musicVolumeSlider.setValue(options.getMusicVolume());
		musicVolumeSlider.fire(new ChangeEvent());

		// sound vol slider
		Label labelSound = new Label(localize("SoundVolume"), skin);
		soundVolumeLabel = new Label("---", skin);
		soundVolumeSlider = createSliderWithLabel(0, 100, 1, soundVolumeLabel);
		soundVolumeSlider.setValue(options.getSoundVolume());
		soundVolumeSlider.fire(new ChangeEvent());

		Label fullscreen = new Label(localize("FullScreen"), skin);
		fullscreenSwitch = new Button(skin, "switch");
		fullscreenSwitch.setChecked(options.isFullscreen());
		fullscreenSwitchLabel = new Label(toSwitchString(options.isFullscreen()), skin);
		Label resolution = new Label(localize("Resolution"), skin);
		resolutionSelectBox = new SelectBox<String>(skin);

		setResolutionItems(options);

		languageBox = new SelectBox<String>(skin);

		String[] languageItems = Arrays.stream(LANGUAGES).map(OptionsScreen::localize).toArray(String[]::new);

		languageBox.setItems(languageItems);

		int languageSelectededIndex = Arrays.asList(LANGUAGES).indexOf(options.getLanguageCode());
		if (languageSelectededIndex == -1)
			languageSelectededIndex = 0;
		languageBox.setSelectedIndex(languageSelectededIndex);

		Label tcpLabel = new Label(localize("TCPPort"), skin);
		Label udpLabel = new Label(localize("UDPPort"), skin);
		Label udpBroadLabel = new Label(localize("UDPBroadcast"), skin);
		Label platformNameLabel = new Label(localize("PlatformName"), skin);
		tcpTxtField = new TextField(tcpPort, skin);
		tcpTxtField.setTextFieldFilter(new DigitsOnlyFilter());
		udpTxtField = new TextField(udpPort, skin);
		tcpTxtField.setTextFieldFilter(new DigitsOnlyFilter());
		udpBroadTxtField = new TextField(udpBroadPort, skin);
		tcpTxtField.setTextFieldFilter(new DigitsOnlyFilter());

		platformNameTxtField = new TextField(name, skin);

		Dialog dialog = new Dialog(localize("restart"), skin);
		dialog.button(localize("Ok"));

		Button cancel = new TextButton(localize("cancel"), skin);
		Button apply = new TextButton(localize("Apply"), skin);
		language = new Label(localize("Language"), skin);

		Table content = new Table(skin);

		content.setBackground(ColorUtil.getSolidColor(new Color(BG_BOX).add(0, 0, 0, -.4f)));
		content.pad(20);
		// Audio
		content.row().padBottom(20);
		content.add(audio).left().growX();

		content.row().padBottom(20);
		content.add(labelMaster).padLeft(50).left();
		content.add(buildSliderHG(masterVolumeSlider, masterVolumeLabel)).left();

		content.row().padBottom(20);
		content.add(labelMusic).padLeft(50).left();
		content.add(buildSliderHG(musicVolumeSlider, musicVolumeLabel)).left();

		content.row().padBottom(30);
		content.add(labelSound).padLeft(50).left();
		content.add(buildSliderHG(soundVolumeSlider, soundVolumeLabel)).left();

		// Display
		content.row().padBottom(20);
		content.add(new Label(localize("Display"), skin)).left();
		content.row().padBottom(20);
		content.add(fullscreen).padLeft(50).left();
		content.add(fullscreenSwitch).center();
		content.add(fullscreenSwitchLabel).left().width(50);

		content.row().padBottom(30);
		content.add(resolution).padLeft(50).left();
		content.add(resolutionSelectBox).left();

		// Network
		content.row().padBottom(20);
		content.add(new Label(localize("Network"), skin)).left();
		content.row().padBottom(20);
		content.add(udpLabel).padLeft(50).left();
		content.add(udpTxtField).left();

		content.row().padBottom(20);
		content.add(udpBroadLabel).padLeft(50).left();
		content.add(udpBroadTxtField).left();

		content.row().padBottom(20);
		content.add(tcpLabel).padLeft(50).left();
		content.add(tcpTxtField).left();

		content.row().padBottom(30);
		content.add(platformNameLabel).padLeft(50).left();
		content.add(platformNameTxtField).left();

		// Misc
		content.row().padBottom(20);
		content.add(new Label(localize("Misc"), skin)).left();

		content.row().padBottom(20);
		content.add(language).padLeft(50).left();
		content.add(languageBox).left();

		ScrollPane sp = new ScrollPane(content, skin);
		sp.setFadeScrollBars(false);
		sp.setupFadeScrollBars(1f, 0);
		sp.setScrollingDisabled(true, false);
		contentCell = rootTable.add(sp).grow().padTop(40).width(Value.percentWidth(.6f, rootTable));

		createFooter();
		cancel = createFooterButton(localize("cancel"), Align.right).getActor();
		cancel.addListener(Listener.onClicked(e -> menuManager.showMainMenu()));
		apply = createFooterButton(localize("Apply"), Align.right).getActor();
		apply.addListener(Listener.onClicked(e -> apply(options)));

		fullscreenSwitch.addListener(Listener.onClicked(e -> {
			fullscreenSwitch.setChecked(fullscreenSwitch.isChecked());
			fullscreenSwitchLabel.setText(toSwitchString(fullscreenSwitch.isChecked()));
		}));

	}

	@Override
	public void resize(int width, int height) {

		super.resize(width, height);
	}

	private Table buildSliderHG(Slider slider, Label label) {
		Table hg = new Table();
		Container<Slider> boxedSlider = new Container<Slider>(slider);
		hg.add(boxedSlider).growX().padRight(10);
		hg.add(label);
		return hg;
	}

	private void setResolutionItems(OptionsModel options) {
		List<ScreenDimension> resolutionList = Arrays.asList(RESOLUTIONS);
		int resolutionSelectedIndex = resolutionList.indexOf(options.getResolution());

		List<String> items = new ArrayList<>();

		if (resolutionSelectedIndex == -1) {
			customResolution = options.getResolution();
			items.add(localize("currentResolution", customResolution.toString()));
			resolutionSelectedIndex = 0;
		}

		for (int i = 0; i < resolutionList.size(); i++) {
			ScreenDimension current = resolutionList.get(i);
			if (current.equals(options.getResolution())) {
				items.add(localize("currentResolution", current.toString()));
			} else {
				items.add(current.toString());
			}
		}
		resolutionSelectBox.setItems(items.toArray(new String[0]));
		resolutionSelectBox.setSelectedIndex(resolutionSelectedIndex);
	}

	private Slider createSliderWithLabel(int min, int max, int step, Label sliderLabel) {
		Slider slider = new Slider(min, max, step, false, skin);
		slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int value = (int) slider.getValue();
				sliderLabel.setText(createSliderLabelText(value));
			}
		});

		return slider;
	}

	private void apply(OptionsModel oldOptions) {
		OptionsModel newOptions = new OptionsModel();
		try {
			newOptions.setFullscreen(fullscreenSwitch.isChecked());
			newOptions.setLanguageCode(LANGUAGES[languageBox.getSelectedIndex()]);
			newOptions.setMasterVolume((int) masterVolumeSlider.getValue());
			newOptions.setMusicVolume((int) musicVolumeSlider.getValue());
			newOptions.setSoundVolume((int) soundVolumeSlider.getValue());
			newOptions.setPlatformName(platformNameTxtField.getText());
			newOptions.setPortTCPReceive(Integer.parseInt(tcpTxtField.getText()));
			newOptions.setPortUDPBroadcast(Integer.parseInt(udpBroadTxtField.getText()));
			newOptions.setPortUDPReceive(Integer.parseInt(udpTxtField.getText()));
			newOptions.setResolution(getSelectedResolution());
			if (!newOptions.validate()) {
				showInputError();
				return;
			}
		} catch (Exception e) {
			showInputError();
			return;
		}
		if (newOptions.isFullscreen() && !oldOptions.isFullscreen()) {
			newOptions.setResolution(getMonitorDimension());
		} else if (newOptions.isFullscreen() && !getSelectedResolution().equals(getMonitorDimension())) {
			newOptions.setFullscreen(false);
		}
		newOptions.apply();

		boolean widthRollback = newOptions.isFullscreen() != oldOptions.isFullscreen()
				|| !newOptions.getResolution().equals(oldOptions.getResolution());

		if (widthRollback) {
			updateDisplay(newOptions.getResolution().equals(oldOptions.getResolution()));
			menuManager.showOptionsMenu(oldOptions);
		} else {
			menuManager.showMainMenu();
		}
	}

	private ScreenDimension getMonitorDimension() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return new ScreenDimension(screenSize.width, screenSize.height);
	}

	private void updateDisplay(boolean force) {
		System.setProperty("org.lwjgl.opengl.Window.undecorated",
				Boolean.toString(PlatformConfiguration.WINDOW_FULLSCREEN));

		// if the dimensions are the same, the display isn't recreated and the
		// decoration state remains
		if (force) {
			Gdx.graphics.setWindowedMode(PlatformConfiguration.WINDOW_WIDTH + 1,
					PlatformConfiguration.WINDOW_HEIGHT + 1);
		}
		Gdx.graphics.setWindowedMode(PlatformConfiguration.WINDOW_WIDTH, PlatformConfiguration.WINDOW_HEIGHT);
	}

	@Override
	public void show() {
		super.show();
	}

	private void showInputError() {
		Dialog dialog = new Dialog(localize("settingsErrorTitle"), skin);
		dialog.text(localize("settingsErrorMessage"));
		dialog.button("Okay", 1);
		dialog.show(stage);
	}

	private ScreenDimension getSelectedResolution() {
		int selected = resolutionSelectBox.getSelectedIndex();
		if (customResolution != null) {
			if (selected == 0) {
				return customResolution;
			}
			selected--;
		}
		return RESOLUTIONS[selected];

	}

	private String toSwitchString(boolean isOn) {
		return isOn ? localize("on") : localize("off");
	}

	@Override
	public void render(float delta) {
		int minWidthContent = 1000;
		if (rootTable.getWidth() * .9f < minWidthContent) {
			contentCell.width(rootTable.getWidth() * .9f);
			rootTable.invalidate();
		} else {
			contentCell.width(minWidthContent);
			rootTable.invalidate();
		}
		super.render(delta);
	}
	
}
