package ubiquigame.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontSuite {

	public static BitmapFont OPEN_SANS_16;
	public static BitmapFont OPEN_SANS_32;
	public static BitmapFont OPEN_SANS_48;
	public static BitmapFont OPEN_SANS_64;
	public static BitmapFont OPEN_SANS_80;
	public static BitmapFont OPEN_SANS_128;
	public static BitmapFont OPEN_SANS_BOLD_MEDIUM;
	
	public static void generate() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rimouski sb.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.genMipMaps = true;
		parameter.magFilter = TextureFilter.MipMapLinearLinear;
		parameter.minFilter = TextureFilter.MipMapLinearLinear;
		parameter.size = 16;
		OPEN_SANS_16 = generator.generateFont(parameter);
		parameter.size = 32;
		OPEN_SANS_32 = generator.generateFont(parameter);
		parameter.size = 48;
		OPEN_SANS_48 = generator.generateFont(parameter);
		parameter.size = 64;
		OPEN_SANS_64 = generator.generateFont(parameter);
		parameter.size = 80;
		OPEN_SANS_80 = generator.generateFont(parameter);
		parameter.size = 128;
		OPEN_SANS_128 = generator.generateFont(parameter);
		generator.dispose();

		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/rimouski sb.ttf"));
		parameter.size = 24;
		OPEN_SANS_BOLD_MEDIUM = generator.generateFont(parameter);
		generator.dispose();
	}

}
