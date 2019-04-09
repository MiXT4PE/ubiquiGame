package ubiquigame.games.space_shooter.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import ubiquigame.games.space_shooter.Level;

import java.util.ArrayList;
import java.util.List;

public class HUDWall extends Wall {

    // 1 PlayerStatus object contains player name plus his energy bar
    private List<PlayerStatus> statusList;

    // x and y coordinates corresponds to wall below the status
    public HUDWall(Level level, float x, float y, int width, int height) {
        super(level, x, y, width, height);

        statusList = new ArrayList<PlayerStatus>(level.getNumPlayers());
        List<Ship> ships = level.getShips();
        for (int i = 0; i < ships.size(); i++) {
            PlayerStatus status = new PlayerStatus(ships.get(i), width - 60f);
            status.setPosition(
                    x + 30f,
                    (y + 50f) + i*(status.getHeight() + PlayerStatus.PADDING)
            );

            statusList.add(status);
        }
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
       // Default clear call
       super.render(sb, sr);

        for (int i = 0; i < statusList.size(); i++) {
            statusList.get(i).render(sb, sr);
        }
    }

    @Override
    public Polygon getBounds() {
        return bounds;
    }

}
