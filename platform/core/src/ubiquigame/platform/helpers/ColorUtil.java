package ubiquigame.platform.helpers;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ColorUtil {

	private static Map<Color, TextureRegionDrawable> cache = new HashMap<>();

	public static TextureRegionDrawable getSolidColor(Color color) {
		return cache.computeIfAbsent(color, c -> {
			Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
			bgPixmap.setColor(color);
			bgPixmap.fill();
			return new TextureRegionDrawable(new TextureRegion(new Texture(bgPixmap)));
		});
	}
}
