package ubiquigame.games.space_shooter.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ubiquigame.games.space_shooter.SpaceShooter;

public class CountDownState extends State {

    private String[] messages = {"3", "2", "1", "LET'S GO!!"};
    private float timer = 0f;

    private Color color;
    private BitmapFont font;
    private Vector2 pos;
    private String message;

    private GlyphLayout layout;

    private Color backgroundColor;
    private Texture background;
    private float y;
    private float height;

    public CountDownState(GameStateManager gsm) {
        super(gsm);

        layout = new GlyphLayout();

        color = new Color(Color.BLACK);
        // use FreeFont extension to easily create BitmapFonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("space-shooter/fonts/space-wham.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.color = color;
        // create Bitmap Font from generator
        font = generator.generateFont(parameter);
        generator.dispose();

        pos = new Vector2();
        setMessage(messages[0]);

        background = new Texture(Gdx.files.internal("space-shooter/level/banner.png"));
        backgroundColor = new Color(1, 1, 1, 0.9f);
        height = font.getCapHeight() + 16f;
        y = (SpaceShooter.getInstance().height - height) / 2f;
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {
        // count down
        timer += dt;

        if(timer >= messages.length) {
            // start with the action
            gsm.set(new PlayState(gsm));
            return;
        }

        int index = MathUtils.floor(timer);
        String msg = messages[index];
        if(index == messages.length - 1)
            msg = messages[index];

        setMessage(msg);
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        // Default clear call
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Background Color
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);

        // display black bar
        sb.begin();
//        sb.setColor(backgroundColor);
        sb.draw(background, 0, spaceShooter.height/2 - background.getHeight()/2);
//        sb.setColor(Color.WHITE);

        // display number
        font.draw(sb, message, pos.x, pos.y);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
    }

    public void setMessage(String message) {
        this.message = message;

        // use layout to set every message from messages in middle of screen
        layout.setText(font, message);
        pos.x = (spaceShooter.width - layout.width) / 2f;
        pos.y = spaceShooter.height - ((spaceShooter.height - layout.height) / 2f);
    }
}
