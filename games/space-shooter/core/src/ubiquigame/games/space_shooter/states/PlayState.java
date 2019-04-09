package ubiquigame.games.space_shooter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ubiquigame.common.Player;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.games.space_shooter.GameOverListener;
import ubiquigame.games.space_shooter.Level;
import ubiquigame.games.space_shooter.game_objects.Ship;

import java.util.*;

public class PlayState extends State implements GameOverListener {
//
//	private final Player player1;
//	private final Player player2;
//	private final Player player3;

	private final Level level;

//	private final Ship ship1;
//	private final Ship ship2;
//	private final Ship ship3;

	private boolean gameOver;
	private Stack<Player> ranking;

	private Viewport viewport;

	public PlayState(GameStateManager gsm) {
		super(gsm);
		int w1 = 1280;
		int h1 = 800;

		int w = 1920;
		int h = 1080;

		camera.setToOrtho(false, w, h);
		camera.position.x = w/2f;
		camera.position.y = h/2f;
		camera.update();

		viewport = new FitViewport(w, h, camera);
//
//		player1 = platform.getPlayers()[0];
//		player2 = platform.getPlayers()[1];
//		player3 = platform.getPlayers()[2];

		level = new Level(this);

		// read position of player from json file
		JsonReader jr = new JsonReader();
		JsonValue jv = jr.parse(Gdx.files.internal("space-shooter/level/level.json"));
		JsonValue playerLocations = jv.get(0);

		Player[] players = platform.getPlayers();
		for (int i = 0; i < players.length; i++) {
		    float facingDir = 0;
		    if(i % 2 != 0)
		        facingDir = MathUtils.PI;
			int x = playerLocations.get(i).getInt("x");
			int y = playerLocations.get(i).getInt("y");
			Ship ship = new Ship(level, players[i],facingDir, i, x, y);
			level.addShip(ship);
		}

		level.setNumPlayers(players.length);

//		ship1 = new Ship(level, player1, playerLocations.get(0).getInt("x"), playerLocations.get(0).getInt("y"));
//		ship2 = new Ship(level, player2, playerLocations.get(1).getInt("x"), playerLocations.get(1).getInt("y"));
//		ship3 = new Ship(level, player3, playerLocations.get(2).getInt("x"), playerLocations.get(2).getInt("y"));
//
//		level.addShip(ship1);
//		level.addShip(ship2);
//		level.addShip(ship3);
//		level.setNumPlayers(3);

		level.create();
	}

	@Override
	public void handleInput() {

	}

	@Override
	public void update(float dt) {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Should handle inputs first
//        handleInput();

		// If needed you can change the camera position
//        camera.position.x = 0 + spaceShooter.width/2;
//        camera.update();

		level.update(dt);

		// send game over message to platform
		if (gameOver) {
			List<Player> list = new ArrayList<>(ranking);
			// reverses in-place!!
			Collections.reverse(list);
			Map<Player, Integer> score = new HashMap<>();
			int points = 1000;
			for (int i = 0; i < list.size(); i++) {
				score.put(list.get(i), points);
				points -= 200;
			}
			spaceShooter.gameover(new GameOverMessage(list, score));
			gsm.pop();
		}
	}

	@Override
	public void render(SpriteBatch sb, ShapeRenderer sr) {
		// Default clear call
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Background Color
		Gdx.gl.glClearColor(0, 0, 0, 1);

		// Is needed to move the camera relative to the textures
        sb.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

		level.render(sb, sr);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void onGameOver(Stack<Player> ranking) {
		this.gameOver = true;
		this.ranking = ranking;
	}
}
