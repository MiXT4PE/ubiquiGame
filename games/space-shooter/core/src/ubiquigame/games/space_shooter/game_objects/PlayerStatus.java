package ubiquigame.games.space_shooter.game_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PlayerStatus {

    public static final float PADDING = 8f;
    public static final float BAR_PADDING = 4f;
    public static final float BAR_WIDTH = 5f; // height depends on font

    private final GlyphLayout layout;

    private Ship ship;
    private Rectangle[] list;

    private Vector2 position;
    private float width;
    private float height;

    private BitmapFont font;

    private static final int MAX_NAME_LENGTH = 7;

    private Vector2 namePosition;
    private Vector2 barPosition;

    private Color tint;

    public PlayerStatus(Ship ship, float width) {
        this.ship = ship;
        this.list = new Rectangle[Ship.MAX_ENERGY];

        generateFont();
        layout = new GlyphLayout(font, ship.getPlayer().getName());

        this.width = width;
        this.height = layout.height + 2*PADDING;

        tint = ship.getColor();
    }

    private void generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("space-shooter/fonts/space-wham.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 14;

        // generate font with color of player
        parameter.color = Color.WHITE;
        // create Bitmap Font from generator
        font = generator.generateFont(parameter);

        generator.dispose();
    }

    public void render(SpriteBatch sb, ShapeRenderer sr) {
        // render name
        sb.begin();
        font.setColor(tint);
        if (ship.getPlayer().getName().length() > MAX_NAME_LENGTH) {
            font.draw(sb, ship.getPlayer().getName().substring(0, MAX_NAME_LENGTH - 1)+"...", namePosition.x, namePosition.y);
        } else {
            font.draw(sb, ship.getPlayer().getName(), namePosition.x, namePosition.y);
        }

        sb.end();

        // render bars
        sr.setColor(tint);
        sr.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < ship.getEnergy(); i++) {
            Rectangle r = list[i];
            sr.rect(r.x, r.y, r.width, r.height);
        }
        sr.end();
    }

    public void setPosition(float x, float y) {
        this.position = new Vector2(x, y);

        // name positioning
        namePosition = position.cpy();
        namePosition.x += PADDING;
        namePosition.y += PADDING + layout.height;

        // bar positioning
        barPosition = position.cpy();
        barPosition.x += width;
        barPosition.x -= Ship.MAX_ENERGY * (BAR_PADDING + BAR_WIDTH);
        barPosition.y += PADDING;

        for (int i = 0; i < Ship.MAX_ENERGY; i++) {
            Rectangle rect = new Rectangle();
            rect.x = barPosition.x + i * (BAR_WIDTH + BAR_PADDING);
            rect.y = barPosition.y;
            rect.width = BAR_WIDTH;
            rect.height = layout.height;
            list[i] = rect;
        }
    }

    public float getHeight() {
        return height;
    }
}
