package ubiquigame.platform.menu;

import java.nio.charset.StandardCharsets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import ubiquigame.platform.FontSuite;
import ubiquigame.platform.helpers.Listener;

public class CreditsScreen extends AbstractMenuScreen {
	private ScrollPane sp;
	private VerticalGroup vg;
	private float pixelScrolled = 0;
	private float scrollSpeed = 40;

	private LabelStyle[] labelStyles;

	public CreditsScreen() {
		super();
		createHeader(localize("Credits"));
		Table content = new Table();
		rootTable.add(content).grow();
		vg = new VerticalGroup();
		sp = new ScrollPane(vg);
		sp.setScrollBarPositions(false, false);
		sp.clearListeners();
		content.add(sp).fill();
		createFooter();
		TextButton backbutton = createFooterButton(localize("cancel"), Align.right).getActor();
		backbutton.addListener(Listener.onClicked(e -> menuManager.showMainMenu()));
		setupLabelStyles();
		readCredits();
	}

	private void setupLabelStyles() {
		LabelStyle base = new Label("", skin).getStyle();

		labelStyles = new LabelStyle[] { createLabelStyleWithFont(base, FontSuite.OPEN_SANS_32),
				createLabelStyleWithFont(base, FontSuite.OPEN_SANS_48),
				createLabelStyleWithFont(base, FontSuite.OPEN_SANS_64),
				createLabelStyleWithFont(base, FontSuite.OPEN_SANS_80), };
	}

	private LabelStyle createLabelStyleWithFont(LabelStyle base, BitmapFont font) {
		LabelStyle labelStyle = new LabelStyle(base);
		labelStyle.font = font;
		return labelStyle;
	}

	@Override
	public void show() {
		super.show();
		sp.setScrollY(pixelScrolled);
	}

	@Override
	public void render(float delta) {
		pixelScrolled += (delta * scrollSpeed);
		sp.setScrollY(pixelScrolled);
		
		super.render(delta);

	}

	private void readCredits() {
		String creditsContent = Gdx.files.internal("credits.txt").readString(StandardCharsets.UTF_8.name());
		for (String line : creditsContent.split("\n")) {
			line = line.trim();
			int style = getFontScale(line);
			line = line.substring(style);

			Label label = new Label(line, skin);
			label.setStyle(labelStyles[style]);
			vg.addActor(label);
		}
	}

	private int getFontScale(String line) {
		int fontScale = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '_')
				fontScale++;
			else {
				break;
			}
		}
		return fontScale;
	}

}
