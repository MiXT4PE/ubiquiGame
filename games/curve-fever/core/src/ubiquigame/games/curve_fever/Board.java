package ubiquigame.games.curve_fever;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import ubiquigame.common.Player;
import ubiquigame.common.constants.PlayerIdentifier;

public class Board {

	private int culledBoarder = 100;
	private Random rng = new Random();
	List<Puk> puks = new ArrayList<>();
	private SpriteBatch spriteBatch = new SpriteBatch();
	private Vector2 size = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	private FrameBuffer frameBuffer;
	private TextureRegion bufferRegion;
	private Pixmap pixMapLastFrame;

	public Board(Player[] players) {
		for (int i = 0; i < 1; i++) {
			Color gdxColor = Utils.getGdxColor(PlayerIdentifier.values()[i].color);

			puks.add(new Puk(players[i], this, new Vector2(size).scl(rng.nextFloat(), rng.nextFloat()), gdxColor));
		}
		initFrameBuffer();
	}

	public void update(float delta) {
		for (Puk puk : puks) {
			puk.update(delta);
		}
	}

	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		frameBuffer.begin();
		for (Puk puk : puks) {
			puk.render(delta);
		}
		if (pixMapLastFrame != null) {
			pixMapLastFrame.dispose();
		}
		frameBuffer.end();
		frameBuffer.begin();
		spriteBatch.begin();
		spriteBatch.draw(bufferRegion, frameBuffer.getWidth() - culledBoarder, 0,culledBoarder, frameBuffer.getHeight() );
		spriteBatch.end();
		pixMapLastFrame = ScreenUtils.getFrameBufferPixmap(0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
		frameBuffer.end();
		spriteBatch.begin();
		spriteBatch.draw(bufferRegion, -culledBoarder, -culledBoarder, bufferRegion.getRegionWidth() - culledBoarder,
				bufferRegion.getRegionHeight() - culledBoarder);
		
		spriteBatch.end();

	}

	private void initFrameBuffer() {
		if (frameBuffer == null) {

			frameBuffer = new FrameBuffer(Format.RGBA8888, (int) size.x + culledBoarder * 2,
					(int) size.y + culledBoarder * 2, false);
			bufferRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
		}
	}

	public Vector2 getSize() {
		return size;
	}

	public FrameBuffer getBackBuffer() {
		return frameBuffer;

	}

	public Pixmap getPixMapLastFrame() {
		return pixMapLastFrame;
	}

	public TextureRegion getBufferRegion() {
		return bufferRegion;
	}

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

}
