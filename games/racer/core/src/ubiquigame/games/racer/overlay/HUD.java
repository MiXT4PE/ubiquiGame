package ubiquigame.games.racer.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.constants.PlayerIdentifier;
import ubiquigame.games.racer.RacerMain;

import java.text.DecimalFormat;

public class HUD {
    private final float PADDING = 8f;
    public Stage stage;
    private Viewport viewport;
    private RacerMain main;
    private UbiquiGamePlatform platform;

    //Labels are what is drawn on the HUD
    private BitmapFont font;
    private BitmapFont fontCountdown;
    private BitmapFont fontSwitch;
    private Table tableTop;
    private Table tableBottom;
    private Label player1;
    private Label player2;
    private Label player3;
    private Label player4;
    private Label countdown;
    private Label switchControl;
    private Label winner;
    //Variables for Players
    private int p1round;
    private int p2round;
    private int p3round;
    private int p4round;
    private int p1boosts;
    private int p2boosts;
    private int p3boosts;
    private int p4boosts;
    private int p1ranking;
    private int p2ranking;
    private int p3ranking;
    private int p4ranking;
    private int p1time;
    private int p2time;
    private int p3time;
    private int p4time;

    public static final int COUNTDOWNSECONDS = 5;
    private float timerCountdown;
    private boolean timerCountdown_running;
    private int timerCountdown_seconds;
    private boolean readyToStart;

    //sounds
    private Sound soundCountdown1;
    private Sound soundCountdown2;

    public HUD (SpriteBatch sb, UbiquiGamePlatform platform){
        this.main = RacerMain.getInstance();
        this.platform = platform;
        viewport = new FitViewport(main.width, main.height, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        FreeTypeFontGenerator generatorNormal = new FreeTypeFontGenerator(Gdx.files.internal("racer/fonts/normalfont.ttf"));
        FreeTypeFontGenerator generatorCountdown = new FreeTypeFontGenerator(Gdx.files.internal("racer/fonts/countdown.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 25;
        font = generatorNormal.generateFont(parameter);
        parameter.size = 200;
        fontCountdown = generatorCountdown.generateFont(parameter);
        parameter.size = 25;
        fontSwitch = generatorNormal.generateFont(parameter);
        generatorNormal.dispose();
        generatorCountdown.dispose();

        timerCountdown = 0;
        timerCountdown_running = false;
        timerCountdown_seconds = COUNTDOWNSECONDS;
        readyToStart = false;

        //default values
        p1round = p2round = p3round = p4round = 3;
        p1time = p2time = p3time = p4time = 0;

        //tableTop
        tableTop = new Table();
        tableTop.setFillParent(true);
        tableTop.top();
        stage.addActor(tableTop);
        //tableBottom
        tableBottom = new Table();
        tableBottom.setFillParent(true);
        tableBottom.bottom();
        stage.addActor(tableBottom);

        //Create Labels and add them to the table
        player1 = new Label(platform.getPlayers()[0].getName() + "\nnoch " + p1round + " Runden" + "\nVorhandene Boosts: " + p1boosts + "\nZeit: " + convertTimeToString(p1time),
                new Label.LabelStyle(font, new Color(PlayerIdentifier.PLAYER1.color.getRed()/255f,PlayerIdentifier.PLAYER1.color.getGreen()/255f, PlayerIdentifier.PLAYER1.color.getBlue()/255f, PlayerIdentifier.PLAYER1.color.getAlpha()/255f)));
        tableTop.add(player1).left().expandX().pad(PADDING, PADDING, 0f, 0f);

        player2 = new Label(platform.getPlayers()[1].getName()  + "\nnoch " + p2round + " Runden" + "\nVorhandene Boosts: " + p2boosts + "\nZeit: " + convertTimeToString(p2time),
                new Label.LabelStyle(font, new Color(PlayerIdentifier.PLAYER2.color.getRed()/255f,PlayerIdentifier.PLAYER2.color.getGreen()/255f, PlayerIdentifier.PLAYER2.color.getBlue()/255f, PlayerIdentifier.PLAYER2.color.getAlpha()/255f)));
        tableTop.add(player2).right().expandX().pad(PADDING, 0f, 0f, PADDING);

        if (platform.getPlayers().length >= 3){
            player3 = new Label(platform.getPlayers()[2].getName()  + "\nnoch " + p3round + " Runden" + "\nVorhandene Boosts: " + p3boosts + "\nZeit: " + convertTimeToString(p3time),
                    new Label.LabelStyle(font, new Color(PlayerIdentifier.PLAYER3.color.getRed()/255f, PlayerIdentifier.PLAYER3.color.getGreen()/255f, PlayerIdentifier.PLAYER3.color.getBlue()/255f, PlayerIdentifier.PLAYER3.color.getAlpha()/255f)));
            tableBottom.add(player3).left().expandX().pad(0f, PADDING, PADDING, 0f);
        }

        if (platform.getPlayers().length == 4){
            player4 = new Label(platform.getPlayers()[3].getName()  + "\nnoch " + p4round + " Runden" + "\nVorhandene Boosts: " + p4boosts + "\nZeit: " + convertTimeToString(p4time),
                    new Label.LabelStyle(font, new Color(PlayerIdentifier.PLAYER4.color.getRed()/255f,PlayerIdentifier.PLAYER4.color.getGreen()/255f, PlayerIdentifier.PLAYER4.color.getBlue()/255f, PlayerIdentifier.PLAYER4.color.getAlpha()/255f)));
            tableBottom.add(player4).right().expandX().pad(0f, 0f, PADDING, PADDING);
        }

        //countdown
        countdown = new Label("Bereit?", new Label.LabelStyle(fontCountdown, Color.BLACK));
        countdown.setPosition(main.width / 2 - countdown.getWidth() / 2, main.height / 2 - countdown.getHeight() / 2);
        countdown.setAlignment(Align.center);
        stage.addActor(countdown);

        //switch Control
        switchControl = new Label("", new Label.LabelStyle(fontSwitch, Color.BLACK));
        switchControl.setPosition(main.width / 2, main.height - 3 * PADDING);
        switchControl.setAlignment(Align.center);
        stage.addActor(switchControl);

        //winner
        winner = new Label("", new Label.LabelStyle(fontCountdown, Color.BLACK));
        winner.setPosition(main.width / 2 - winner.getWidth() / 2, main.height / 3 - winner.getHeight() / 2);
        winner.setAlignment(Align.center);
        winner.setVisible(false);
        stage.addActor(winner);

        //sounds
        this.soundCountdown1 = Gdx.audio.newSound(Gdx.files.internal("racer/sounds/countdown1.ogg"));
        this.soundCountdown2 = Gdx.audio.newSound(Gdx.files.internal("racer/sounds/countdown2.ogg"));
    }

    public void update(float dt){
        //Update timerCountdown
        if (timerCountdown_running){
            timerCountdown += dt;
        }

        String text = "\nnoch " + p1round + " Runden";
        if (p1round == 1){
            text = "\nletzte Runde";
        }
        if(p1round == 0){
            text = "\n" + p1ranking + ". Platz";
        }
        text = platform.getPlayers()[0].getName() +  text + "\nVorhandene Boosts: " + p1boosts + "\nZeit: " + convertTimeToString(p1time);
        player1.setText(text);

        text = "\nnoch " + p2round + " Runden";
        if (p2round == 1){
            text = "\nletzte Runde";
        }
        if(p2round == 0){
            text = "\n" + p2ranking + ". Platz";
        }
        text = platform.getPlayers()[1].getName() +  text + "\nVorhandene Boosts: " + p2boosts + "\nZeit: " + convertTimeToString(p2time);
        player2.setText(text);

        if (platform.getPlayers().length >= 3){
            text = "\nnoch " + p3round + " Runden";
            if (p3round == 1){
                text = "\nletzte Runde";
            }
            if(p3round == 0){
                text = "\n" + p3ranking + ". Platz";
            }
            text = platform.getPlayers()[2].getName() +  text + "\nVorhandene Boosts: " + p3boosts + "\nZeit: " + convertTimeToString(p3time);
            player3.setText(text);
        }
        if (platform.getPlayers().length == 4){
            text = "\nnoch " + p4round + " Runden";
            if (p4round == 1){
                text = "\nletzte Runde";
            }
            if(p4round == 0){
                text = "\n" + p4ranking + ". Platz";
            }
            text = platform.getPlayers()[3].getName() + text + "\nVorhandene Boosts: " + p4boosts + "\nZeit: " + convertTimeToString(p4time);
            player4.setText(text);
        }
    }

    public void setRound(int player, int value){
        if (player == 1){
            p1round = value;
        }else if (player == 2){
            p2round = value;
        }else if (player == 3){
            p3round = value;
        }else{
            p4round = value;
        }
    }

    public void setBoost(int player, int value){
        if (player == 1){
            p1boosts = value;
        }else if (player == 2){
            p2boosts = value;
        }else if (player == 3){
            p3boosts = value;
        }else{
            p4boosts = value;
        }
    }

    public void setRanking(int player, int value){
        if (player == 1){
            p1ranking = value;
        }else if (player == 2){
            p2ranking = value;
        }else if (player == 3){
            p3ranking = value;
        }else{
            p4ranking = value;
        }
    }

    public void setTime(int player, int value){
        if (player == 1){
            p1time = value;
        }else if (player == 2){
            p2time = value;
        }else if (player == 3){
            p3time = value;
        }else{
            p4time = value;
        }
    }

    public boolean getReadyToStart(){
        return readyToStart;
    }

    public void startGameCountdown(){
        timerCountdown_running = true;
        if (timerCountdown > 1){
            timerCountdown = 0;
            if (timerCountdown_seconds > 0){
                soundCountdown1.play(platform.getSoundVolume() * 0.3f);
                countdown.setText(timerCountdown_seconds + "");
                timerCountdown_seconds--;
            }else if (timerCountdown_seconds == 0){
                soundCountdown2.play(platform.getSoundVolume() * 0.3f);
                countdown.setText("Los!");
                timerCountdown_seconds--;
                readyToStart = true;
            }else{
                timerCountdown_running = false;
                countdown.setVisible(false);
            }
        }
    }

    private String convertTimeToString(int time){
        int min = time / 60;
        int sec = time % 60;
        DecimalFormat f = new DecimalFormat("#00");
        double formatMin = ((double)Math.round(min*100))/100;
        double formatSec = ((double)Math.round(sec*100))/100;
        return f.format(formatMin) + ":" + f.format(formatSec);
    }

    public void updateSwitch(int time){
        if (time == 0){
            switchControl.setText("");
        }else{
            switchControl.setText("Vertauscher Restzeit: " + time);
        }
    }

    public void gameover(SpriteBatch sb, Array<ParticleEffectPool.PooledEffect> effects, String name, Color color){
        //winner
        winner.setText("1. " + name);
        winner.setStyle(new Label.LabelStyle(fontCountdown, color));
        winner.setVisible(true);

        //firework
        sb.begin();
        for (int i = 0; i < effects.size - 1; i++) {
            ParticleEffectPool.PooledEffect effect = effects.get(i);
            effect.draw(sb, Gdx.graphics.getDeltaTime());
            if (effect.isComplete()) {
                effect.free();
                effects.removeIndex(i);
            }
        }
        sb.end();
    }

    public void dispose(){
        stage.dispose();
        font.dispose();
        soundCountdown1.dispose();
        soundCountdown2.dispose();
    }
}
