package ubiquigame.games.tetris;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ubiquigame.common.Player;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.games.tetris.gameObjects.Block;
import ubiquigame.games.tetris.gameObjects.BlockGenerator;
import ubiquigame.games.tetris.gameObjects.PlayerData;
import ubiquigame.games.tetris.overlay.HUD;

import java.util.*;

public class TetrisGameLogic {
	// Main instance
	private TetrisGame tetrisGame;
	// TextureManager
	private TextureManager tm;
	// Camera
	private OrthographicCamera camera;

	private List<PlayerData> allPlayersData;
	private int randomColumnIndex = -1;

	public TetrisGameLogic(HUD hud, Player[] players) {
		tetrisGame = TetrisGame.getInstance();
		tm = tetrisGame.getTextureManager();
		BlockGenerator.getInstance().setTextureManager(tm);
		camera = new OrthographicCamera(tetrisGame.width, tetrisGame.height);
		camera.setToOrtho(false, tetrisGame.width, tetrisGame.height);

		allPlayersData = new ArrayList<PlayerData>();
		for (Player player : players) {
			allPlayersData.add(new PlayerData(player));
			hud.addLabelWithPlayerName(player.getName() + "            ");
		}

		int gameFieldWidth = allPlayersData.get(0).getGameField().getTotalRenderWidth();
		int emptySpaceWidth = 100;
		int numEmptySpaces = allPlayersData.size() - 1;
		int windowWidth = tetrisGame.width;
		int spaceLeft = windowWidth - gameFieldWidth * allPlayersData.size() - numEmptySpaces * emptySpaceWidth;

		int bottomLeftX = spaceLeft / 2;
		for (PlayerData pd : allPlayersData) {
			pd.getGameField().setBottomLeftPoint(bottomLeftX, 20);
			bottomLeftX += gameFieldWidth + emptySpaceWidth;
		}

		Random random = new Random();
		randomColumnIndex = random.nextInt(allPlayersData.get(0).getGameField().getWidth());
	}

	public void render(SpriteBatch sb, ShapeRenderer sr) {
		Gdx.gl.glClearColor(0.01f, 0.368f, 0.513f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		for (PlayerData pd : allPlayersData) {
			pd.renderPlayerData(sr, sb);
		}
	}

	public void update(float delta) {
		for (PlayerData pd : allPlayersData) {
			pd.updatePlayerData(delta);
			pd.handlePlayerInput();

			List<Block[]> stack = pd.getStack();
			if (stack.size() < 1) {
				continue;
			}

			Random random = new Random();
			randomColumnIndex = random.nextInt(allPlayersData.get(0).getGameField().getWidth());

			while (stack.size() > 1) {
				Block[] row = stack.get(0);
				stack.remove(0);
				PlayerData randomPlayer = getRandomPlayerExceptMe(pd);
				row[randomColumnIndex] = null;
				randomPlayer.getGameField().addRowFromBottom(row);
			}
		}

		if (isGameOver()) {
			List<PlayerData> rankingsPlayerData = new ArrayList<PlayerData>();
			for (PlayerData pd : allPlayersData) {
				rankingsPlayerData.add(pd);
			}

			Collections.sort(rankingsPlayerData, new Comparator<PlayerData>() {
				public int compare(PlayerData p1, PlayerData p2) {
					if (p1.isGameOver() != p2.isGameOver()) {
						if (!p1.isGameOver())
							return -1;

						return 1;
					}

					return p1.getGameOverAfter() > p2.getGameOverAfter() ? -1 : 1;
				}
			});

			List<Player> ranking = new ArrayList<Player>();
			Map<Player, Integer> score = new HashMap<Player, Integer>();
			int currentScore = 3;
			for (PlayerData pd : rankingsPlayerData) {
				ranking.add(pd.getPlayer());
				score.put(pd.getPlayer(), currentScore);
				currentScore--;
			}

			GameOverMessage msg = new GameOverMessage(ranking, score);
			tetrisGame.gameover(msg);
		}
	}

	private boolean isGameOver() {
		int numAlive = 0;
		for (PlayerData pd : allPlayersData) {
			if (!pd.isGameOver()) {
				numAlive++;
			}
		}

		return numAlive <= 1;
	}

	private PlayerData getRandomPlayerExceptMe(PlayerData me) {
		Random random = new Random();
		while (true) {
			int index = random.nextInt(allPlayersData.size());
			PlayerData pd = allPlayersData.get(index);
			if (pd == me) {
				continue;
			}

			return pd;
		}
	}

	public void dispose() {
	}
}
