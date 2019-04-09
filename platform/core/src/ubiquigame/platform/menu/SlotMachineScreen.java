package ubiquigame.platform.menu;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import ubiquigame.common.UbiquiGame;
import ubiquigame.platform.menu.gamelibrary.GameTile;
import ubiquigame.platform.session.tournament.TournamentSession;
import ubiquigame.platform.session.tournament.gamemode.RandomGameMode;

public class SlotMachineScreen extends AbstractMenuScreen {

	private boolean isScrolling = true;

	private float timeScrolled = 0;
	private float scrollTimeSpeedDecay = 4; // scrollanimation for 5 sec
	private float scrollTimeAtMax = 2;
	private float scrollSpeed = 5000; // 1 tile per second

	private int minGameTiles = 10;

	private float pad = 20;

	private int tileSize = 200;

	private float tileChosenMaxZoom = 2;

	private float tileChosenZoom = 0;

	private float zoomSpeed = .02f;

	private List<GameTile> gameTiles = new LinkedList<>();

	private Map<GameTile, UbiquiGame> tileMap = new HashMap<>();

	private GameTile lastTile;

	private ShapeRenderer sr;

	private GameTile chosenTile;
	private RandomGameMode gameMode;

	// timer after zoom
	private float timerMaxWait = 3;
	private float timerWait = 0;

	public SlotMachineScreen(RandomGameMode gameMode) {
		super();
		this.gameMode = gameMode;
		sr = new ShapeRenderer();
		while (gameTiles.size() < minGameTiles) {
			for (UbiquiGame game : gameMode.getAllowedGames()) {
				GameTile gameTile = createGameTile(game);
				tileMap.put(gameTile, game);
				gameTiles.add(gameTile);
			}
		}

		lastTile = gameTiles.get(gameTiles.size() - 1);
		Random rng = new Random();
		scrollTimeAtMax = rng.nextFloat() * 3;
	}

	private GameTile createGameTile(UbiquiGame game) {
		GameTile gameTile = new GameTile(game.getGameInfo().getGameTitle(), game.getGameInfo().getThumbnail(), skin);
		gameTile.setSize(tileSize, tileSize);
		return gameTile;
	}

	@Override
	public void show() {
		super.show();
		for (int i = 0; i < gameTiles.size(); i++) {
			GameTile tile = gameTiles.get(i);
			tile.setPosition(i * (tileSize + 2 * pad), stage.getHeight() / 2 - tile.getHeight() / 2);
		}
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		if (isScrolling) {
			timeScrolled += delta;
			if (timeScrolled < scrollTimeAtMax + scrollTimeSpeedDecay) {
				scroll(delta);
			} else {
				if (chosenTile == null) {
					chosenTile = findNearest();
				}
				scrollTo(delta, chosenTile);
			}

		} else {
			tileChosenZoom += zoomSpeed * delta;
			tileChosenZoom = Math.min(1, tileChosenZoom);
			float zoomInterp = Interpolation.pow2Out.apply(tileChosenZoom);

			tileChosenZoom = Math.min(tileChosenMaxZoom, zoomInterp * 2);
			// max zoom
			if (tileChosenZoom >= 2f) {
				timerWait += delta;
				if (timerWait >= timerMaxWait) {
					gameMode.setNextGame(tileMap.get(chosenTile));
					TournamentSession.getCurrent().getNextGame();
					menuManager.showTutorialScreen();
					return;
				}
			}
		}

		renderTiles();
		if (isScrolling) {
			renderLine();
		} else {
			Batch batch = stage.getBatch();
			batch.begin();
			float newSize = tileSize * tileChosenZoom;
			chosenTile.setSize(newSize, newSize);
			chosenTile.setPosition(stage.getWidth() / 2 - newSize / 2, stage.getHeight() / 2 - newSize / 2);
			chosenTile.draw(batch, 1);
			batch.end();
		}

	}

	private void renderLine() {
		sr.begin(ShapeType.Line);
		sr.setColor(Color.RED);
		Vector2 centerTop = new Vector2(stage.getWidth() / 2, stage.getHeight() / 2 - tileSize * .6f);
		Vector2 centerbot = new Vector2(stage.getWidth() / 2, stage.getHeight() / 2 + tileSize * .6f);
		sr.line(centerTop, centerbot);
		sr.end();
	}

	private void scrollTo(float delta, GameTile tile) {
		float stepX = -delta * scrollSpeed * .05f;
		float toMoveX = tile.getX() + tile.getWidth() / 2 - stage.getWidth() / 2;
		if (Math.abs(toMoveX) < 0.01) {
			isScrolling = false;
		}
		scrollX(Math.max(stepX, -toMoveX));
	}

	private void scroll(float delta) {
		float velocityMult = 1;
		if (timeScrolled > scrollTimeAtMax) {
			velocityMult = Math.min(1, (timeScrolled - scrollTimeAtMax) / scrollTimeSpeedDecay);

			velocityMult = (float) Math.max(.05, 1 - velocityMult);

		}
		float moveX = -delta * scrollSpeed * velocityMult;
		scrollX(moveX);

	}

	private void scrollX(float moveX) {

		for (int i = 0; i < gameTiles.size(); i++) {
			GameTile tile = gameTiles.get(i);
			tile.setX(tile.getX() + moveX);
			if (tile.getX() < -tileSize) {
				gameTiles.remove(i);
				tile.setX(lastTile.getX() + tileSize + pad);
				gameTiles.add(tile);
				lastTile = tile;
			}

		}

	}

	private GameTile findNearest() {
		GameTile nearest = gameTiles.get(0);
		float bestX = 99999;
		float screenWidthHalf = stage.getWidth() / 2;

		for (GameTile tile : gameTiles) {
			float dtX = tile.getX() - screenWidthHalf;
			if (dtX >= 0 && dtX < bestX) {
				bestX = dtX;
				nearest = tile;
			}

		}
		return nearest;
	}

	private void renderTiles() {

		Batch batch = stage.getBatch();
		batch.begin();
		for (GameTile tile : gameTiles) {
			tile.draw(batch, 1);
		}
		batch.end();
	}

}
