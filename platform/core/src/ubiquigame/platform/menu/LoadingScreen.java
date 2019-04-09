package ubiquigame.platform.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class LoadingScreen extends AbstractMenuScreen {

	SpriteBatch sb = new SpriteBatch();
	ShapeRenderer shape = new ShapeRenderer();
	float timeSpent = 0;
	private final float updateSpeed = .10f;

	private OrthographicCamera camera;

	public LoadingScreen() {
		super();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		stage.getViewport().setCamera(camera);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		float numCircles = 9f;
		int circleRadius = 6;
		float r = 40;
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		shape.setProjectionMatrix(camera.projection);
		shape.begin(ShapeType.Filled);

		float angle = (float) (Math.PI * 2 / numCircles);
		for (int i = 0; i < numCircles; i++) {
			float alpha = i / numCircles;
			alpha = (float) Math.pow(alpha, 1) * 1f;
			shape.setColor(.7f, .8f, 1, 1 - alpha);
			float x = (float) (r * Math.cos(i * angle));
			float y = (float) (r * Math.sin(i * angle));
			shape.circle(x, y, circleRadius);
		}
		if (timeSpent >= updateSpeed) {
			timeSpent %= updateSpeed;
			shape.rotate(0, 0, 1, -1 / numCircles * 360f);
		}
		shape.end();
		timeSpent += delta;
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		camera.setToOrtho(false, width / 2, height / 2);
	}

}
