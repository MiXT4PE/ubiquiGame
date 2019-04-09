package ubiquigame.platform.menu.highscore;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import ubiquigame.platform.PlatformImpl;
import ubiquigame.platform.database.dto.User;
import ubiquigame.platform.helpers.ColorUtil;

public class ScoreEntry extends Table {

	private static final int ENTRY_HEIGHT = 100;
	private Label playerNameLabel;
	private Label placeLabel;
	private Label scoreLabel;
	Image avatar;
	private Color bgColor;

	public ScoreEntry(User playerInfo, int place, Skin skin, Color bgColor) {

		this(playerInfo.getName(), createAvatarImage(playerInfo.getAvatar()), playerInfo.getScore(), place, skin,
				bgColor);

	}

	public ScoreEntry(String name, Image avatar, int score, int place, Skin skin, Color bgColor) {
		this.bgColor = bgColor;
		this.setSkin(skin);
		createEntry(place, avatar, name, score);

	}

	private static Image createAvatarImage(byte[] encodedData) {
		Texture texture;
		if (encodedData != null && encodedData.length > 0) {
			try {
				texture = new Texture(new Pixmap(encodedData, 0, encodedData.length));
			} catch (Exception e) {
				texture = PlatformImpl.getInstance().getAssets().getDefaultAvatar();
			}
		} else
			texture = PlatformImpl.getInstance().getAssets().getDefaultAvatar();
		return new Image(texture);
	}

	private void createEntry(int place, Image avatar, String playername, int score) {
		this.left();
		this.setBackground(ColorUtil.getSolidColor(bgColor));
		placeLabel = new Label(String.valueOf(place) + ".", getSkin());
		playerNameLabel = new Label(playername, getSkin());
		scoreLabel = new Label(String.valueOf(score), getSkin());
		this.add(placeLabel).left().padRight(10).padLeft(20).width(60);
		this.add(avatar).left().size(80, 80).padRight(10);
		this.add(playerNameLabel).left().growX();

		this.add(scoreLabel).height(ENTRY_HEIGHT).right().padRight(20);
	}
}
