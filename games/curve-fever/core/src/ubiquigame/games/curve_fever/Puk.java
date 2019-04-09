package ubiquigame.games.curve_fever;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

import ubiquigame.common.Player;
import ubiquigame.common.impl.InputState;

public class Puk {

	private static final float DEFAULT_SIZE = 25;
	private Color color;
	private float size = DEFAULT_SIZE;
	private float velocity = 80;
	private Vector2 orientation = new Vector2(1, 0);
	private Vector2 position = new Vector2();
	private Vector2 prevPos = new Vector2();
	private float rotationSpeed = 100f;
	private Player player;
	private Board board;

	private float timeSinceLastGap = 0;
	private float timerNenextGapIn = 0;
	private boolean gapStarted = true;

	private float gapLength = 0;

	private static final float GAP_TIME_LENGTH_BASE = 1f;
	private static final float GAP_TIME_LENGTH_DERIVATION = .2f;
	private static final float GAP_TIME_WAIT_BASE = 1f;
	private static final float GAP_TIME_WAIT_DERIVATION = .2f;

	private Random rng = new Random();
	private boolean alive = true;

	public Puk(Player player, Board board, Vector2 startPosition, Color color) {
		orientation.rotate(rng.nextFloat() * 360);
		this.position = new Vector2(startPosition);
		System.out.println(startPosition);
		this.color = color;

		this.player = player;
		this.board = board;
		setTimerForNextGap();
		setTimerNextGapLength();
	}

	public void update(float delta) {
		if (!alive)
			return;

		timerNenextGapIn = Math.max(0,timerNenextGapIn - delta);
		if (timerNenextGapIn == 0) {
		}
		InputState input = player.getController().getInput();
		if (input.isLeft()) {
			orientation.rotate(-rotationSpeed * delta);
		}
		if (input.isRight()) {
			orientation.rotate(rotationSpeed * delta);
		}

		prevPos = position.cpy();
		position.mulAdd(orientation, delta * velocity);

		if (position.x > board.getSize().x) {
			position.x = 0;
		}

		if (position.y > board.getSize().y) {
			position.y = 0;
		}

		if (position.x < 0) {
			position.x = board.getSize().x;
		}

		if (position.y < 0) {
			position.y = board.getSize().y;
		}
		checkCollision();
	}

	private void setTimerForNextGap() {
		timerNenextGapIn = GAP_TIME_WAIT_BASE + rng.nextFloat() * GAP_TIME_WAIT_DERIVATION * 2
				- GAP_TIME_WAIT_DERIVATION;
	}

	private void setTimerNextGapLength() {
		gapLength = GAP_TIME_LENGTH_BASE + rng.nextFloat() * GAP_TIME_LENGTH_DERIVATION * 2
				- GAP_TIME_LENGTH_DERIVATION;
	}

	private void checkCollision() {
		FrameBuffer backBuffer = board.getBackBuffer();
		ShapeRenderer sr = Renderer.getShapeRenderer();
		Pixmap pixMapLastFrame = board.getPixMapLastFrame();
		backBuffer.begin();
		int numSamples = 20;
		int scanArcAngle = 170;
//		sr.begin(ShapeType.Line);
		boolean collided = false;
		for (int i = 0; i < numSamples; i++) {
			Vector2 v2 = orientation.cpy().scl(size / 2 + 2).rotate(scanArcAngle * i / numSamples - scanArcAngle / 2)
					.add(position);
			if (pixMapLastFrame == null) {
				break;
			}
			int pixelValue = pixMapLastFrame.getPixel((int) v2.x, (int) v2.y);
			if (pixelValue != 0) {
				collided = true;
				break;
			}

//			sr.setColor(Color.GRAY);
//			sr.circle(position.x, position.y, size / 2);
//			sr.setColor(Color.GREEN);
			// sr.point(v2.x, v2.y, 0);
		}
		if (collided) {
			//alive = false;
		}
		sr.end();
		backBuffer.end();

	}

	public void render(float delta) {

		ShapeRenderer shapeRenderer = Renderer.getShapeRenderer();
		shapeRenderer.begin(ShapeType.Filled);
		if (!alive)
			color = Color.GRAY;
		shapeRenderer.setColor(color);
		shapeRenderer.circle(position.x, position.y, size / 2);
		shapeRenderer.end();

	}

}
