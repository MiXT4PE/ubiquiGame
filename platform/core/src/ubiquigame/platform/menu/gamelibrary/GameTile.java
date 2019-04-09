package ubiquigame.platform.menu.gamelibrary;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameTile extends Actor {
	private Image thumbnail;
	private ClickListener clickListener;
	private static Texture white = new Texture(Gdx.files.internal("white.png"));
	private Label gameNameLabel;

	public GameTile(String gameName, Image thumbnail, Skin skin) {
		super();
		this.thumbnail = thumbnail;
		clickListener = new ClickListener();
		this.addListener(clickListener);
		gameNameLabel = new Label(gameName, skin);
	}
	

	@Override
	public void draw(Batch batch, float parentAlpha) {

		super.draw(batch, parentAlpha);

		batch.setColor(thumbnail.getColor());
		thumbnail.getDrawable().draw(batch, this.getX(), getY(), this.getWidth(), this.getWidth());
		batch.setColor(.3f, .3f, .3f, .8f);
		batch.draw(white, getX(), getY(), this.getWidth(), this.getHeight() * .2f);

		if (clickListener.isOver()) {
			batch.setColor(0, .5f, 1f, .3f);
			batch.draw(white, this.getX(), getY(), this.getWidth(), this.getWidth());
		}
		repositionLabel();
		batch.setColor(Color.WHITE);
		gameNameLabel.draw(batch, 1);
	}

	private void repositionLabel() {
		float labelWidth = gameNameLabel.getWidth();
		float newX = this.getWidth() / 2 - labelWidth / 2;
		float newY = this.getHeight() * .2f / 2 - gameNameLabel.getHeight() / 2;
		gameNameLabel.setX(getX() + newX);
		gameNameLabel.setY(getY()+ newY);
	}  

}
