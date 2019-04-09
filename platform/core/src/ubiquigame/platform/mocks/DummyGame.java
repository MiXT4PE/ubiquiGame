package ubiquigame.platform.mocks;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ubiquigame.common.GameInfo;
import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.AbstractUbiquiGame;
import ubiquigame.common.impl.GameOverMessage;

public class DummyGame extends AbstractUbiquiGame {
	private float time = 0;

	private static int i = 0;
	private int index = 0;

	private GameInfo gameinfo;

	private Player[] players;

	public DummyGame(UbiquiGamePlatform platform) {
		super(platform);
		i++;
		index = i;
		gameinfo = new GameInfo() {
			@Override
			public String getDescription() {
				return "Lorem ipsum dolor sit amet, " + "consectetur adipiscing elit, "
						+ "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "
						+ "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris "
						+ "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in "
						+ "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. "
						+ "Excepteur sint occaecat cupidatat non proident, "
						+ "sunt in culpa qui officia deserunt mollit anim id est laborum.";
			}

			@Override
			public Image getThumbnail() {
				Color color = Color.getHSBColor(index / 8f, 1f, .6f);
				Image image = new Image(new Texture("dummy-thumbnail.jpg"));
				image.setColor(color.getRed() / 256f, color.getGreen() / 256f, color.getBlue() / 256f, 1);
				return image;
			}

			@Override
			public String getGameTitle() {
				return "dummy game" + index;
			}

			@Override
			public Image getTutorialManual() {
				return new Image(new Texture("placeholder-tutorial.jpg"));
			}
		};
	}

	@Override
	public void create() {
		players = getPlatformInstance().getPlayers();

	}

	public GameOverMessage getResult() {
		Map<Player, Integer> scoreMap = new HashMap<>();
		List<Player> playerList = Arrays.asList(players);

		Collections.shuffle(playerList);
		int score = 1000;

		for (Player player : playerList) {
			scoreMap.put(player, score);
			score -= 100;
		}
		return new GameOverMessage(playerList, scoreMap);
	}

	@Override
	public void update(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			this.gameover(getResult());
		}
	}

	@Override
	public void render(float delta) {
		time += delta;
		Color c = Color.getHSBColor(time * .1f, 1f, .8f);
		Gdx.gl20.glClearColor(c.getRed() / 256f, c.getGreen() / 256f, c.getBlue() / 256f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void gameover(GameOverMessage state) {
		super.gameover(state);
	}

	@Override
	public GameInfo getGameInfo() {
		return gameinfo;
	}

}
