package ubiquigame.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class PlatformAssets {

	private Texture defaultAvatar;
	private Skin skin;
	private Texture menuBackground;

	public void load() {
		defaultAvatar = new Texture(Gdx.files.internal("avatar_default.png"));
		
		
		createSkin();
		menuBackground = new Texture("bg-platform.png");
	}
	
	private void createSkin() {
		skin = new Skin();
		FontSuite.generate();
		BitmapFont font = FontSuite.OPEN_SANS_BOLD_MEDIUM;
		
		FileHandle fileHandle = Gdx.files.internal("skin/uiskin.json");
		FileHandle atlasFile = fileHandle.sibling("uiskin.atlas");
		if (atlasFile.exists()) {
		    skin.addRegions(new TextureAtlas(atlasFile));
		}
		skin.add("default-font", font, BitmapFont.class);
		skin.load(fileHandle);
//		skin.remove("default-font", BitmapFont.class);
	}

	public Texture getDefaultAvatar() {
		return defaultAvatar;
	}

	public Skin getSkin() {
		return skin;
	}

	public Texture getMenuBackground() {
		return menuBackground;
	}

}
