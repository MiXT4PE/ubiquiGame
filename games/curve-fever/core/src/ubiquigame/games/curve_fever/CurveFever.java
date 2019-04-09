package ubiquigame.games.curve_fever;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import ubiquigame.common.GameInfo;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.AbstractUbiquiGame;

public class CurveFever extends AbstractUbiquiGame {

	private static final GameInfo gameInfo = new CurveFeverGameInfo();
	
	private UbiquiGamePlatform platform;
	private Board board;
	
	public CurveFever(UbiquiGamePlatform platform) {
		super(platform);
	}

	@Override
	public void create() {
		platform = getPlatformInstance();
		board = new Board(platform.getPlayers());
	}

	@Override
	public void update(float delta) {
		board.update(delta);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		board.render(delta);
	}

	@Override
	public GameInfo getGameInfo() {
		return gameInfo;
	}

}
