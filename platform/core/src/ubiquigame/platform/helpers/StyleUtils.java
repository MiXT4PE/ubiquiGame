package ubiquigame.platform.helpers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class StyleUtils {

	public static LabelStyle createLabelStyleWithFont(LabelStyle base, BitmapFont font) {
		LabelStyle labelStyle = new LabelStyle(base);
		labelStyle.font = font;
		return labelStyle;
	}
}
