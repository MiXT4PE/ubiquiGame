package ubiquigame.games.curve_fever;

import com.badlogic.gdx.graphics.Color;

public class Utils {
	public static Color getGdxColor(java.awt.Color awtColor) {
		return new Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), awtColor.getAlpha());
	}
}
