package ubiquigame.games.ticking_bomb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.constants.PlayerIdentifier;

public class TBHud {
    public Stage stage;
    private Viewport viewport;

    private TBMain tbMain;
    private TBSupplier supplier;
    private UbiquiGamePlatform platform;

    private BitmapFont font;

    private final float textScale = 2f;

    //Tables for formatting stuff
    Table tableTop;
    Table tableBot;
    Table tableMid;
    Table tableBG;

    //Labels are what is drawn on the TBHud
    private Label player0;
    private Label player1;
    private Label player2;
    private Label player3;
    private Label center;

    //For banners
    private Image banner;

    //Countdown
    private float COUNTDOWN_TIME = 5f;
    private float countdown = COUNTDOWN_TIME;

    //Bomb explode
    private float showExpPlayerTime = 4f;
    private float sEPTimer = showExpPlayerTime;

    public TBHud(TBSupplier supplier, SpriteBatch sb){
        this.supplier = supplier;
        this.tbMain = supplier.getMain();
        this.platform = supplier.getPlatform();

        FreeTypeFontGenerator fontGen = new FreeTypeFontGenerator(Gdx.files.internal("ticking_bomb/fonts/rimouski sb.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        this.font = fontGen.generateFont(parameter);
        fontGen.dispose();

        this.viewport = new FitViewport(tbMain.width, tbMain.height, new OrthographicCamera());
        this.stage = new Stage(viewport, sb);

        this.tableTop = new Table();
        this.tableBot = new Table();
        this.tableMid = new Table();
        this.tableBG = new Table();

        //Table settings
        tableTop.setFillParent(true);
        tableTop.top();
        tableBot.setFillParent(true);
        tableBot.bottom();
        tableMid.setFillParent(true);
        tableMid.center();
        tableBG.setFillParent(true);
        tableBG.center();

        //Add table to the stage
        stage.addActor(tableBG);
        stage.addActor(tableTop);
        stage.addActor(tableBot);
        stage.addActor(tableMid);

        //Create Labels
        player0 = new Label(platform.getPlayers()[0].getName(),
                new Label.LabelStyle(font, new Color( ((float) PlayerIdentifier.PLAYER1.color.getRed() / 255f), ((float) PlayerIdentifier.PLAYER1.color.getGreen() / 255f), ((float) PlayerIdentifier.PLAYER1.color.getBlue() / 255f), ((float) PlayerIdentifier.PLAYER1.color.getAlpha() / 255f))));
        player0.setFontScale(textScale);
        tableTop.add(player0).left().top().expandX().pad(8f, 8f, 0f, 0f);

        player1 = new Label(platform.getPlayers()[1].getName(),
                new Label.LabelStyle(font, new Color( ((float) PlayerIdentifier.PLAYER2.color.getRed() / 255f), ((float) PlayerIdentifier.PLAYER2.color.getGreen() / 255f), ((float) PlayerIdentifier.PLAYER2.color.getBlue() / 255f), ((float) PlayerIdentifier.PLAYER2.color.getAlpha() / 255f))));
        player1.setFontScale(textScale);
        tableTop.add(player1).right().top().expandX().pad(8f, 0f, 0f, 8f);

        if(platform.getPlayers().length > 2){
            player2 = new Label(platform.getPlayers()[2].getName(),
                    new Label.LabelStyle(font, new Color( ((float) PlayerIdentifier.PLAYER3.color.getRed() / 255f), ((float) PlayerIdentifier.PLAYER3.color.getGreen() / 255f), ((float) PlayerIdentifier.PLAYER3.color.getBlue() / 255f), ((float) PlayerIdentifier.PLAYER3.color.getAlpha() / 255f))));
            player2.setFontScale(textScale);
            tableBot.add(player2).left().bottom().expandX().pad(0f, 8f, 8f, 0f);
        }

        if(platform.getPlayers().length > 3){
            player3 = new Label(platform.getPlayers()[3].getName(),
                    new Label.LabelStyle(font, new Color( ((float) PlayerIdentifier.PLAYER4.color.getRed() / 255f), ((float) PlayerIdentifier.PLAYER4.color.getGreen() / 255f), ((float) PlayerIdentifier.PLAYER4.color.getBlue() / 255f), ((float) PlayerIdentifier.PLAYER4.color.getAlpha() / 255f))));
            player3.setFontScale(textScale);
            tableBot.add(player3).right().bottom().expandX().pad(0f, 0f, 8f, 8f);
        }


        //Creates center text
        center = new Label("Bereit", new Label.LabelStyle(font, Color.BLACK));
        center.setFontScale(textScale);
        tableMid.add(center).center().expandX();

        //Create banner in middle
        Sprite sBanner = new Sprite(supplier.getTexture("banner"));
        sBanner.setSize(tbMain.width, (tbMain.height / 4));
        banner = new Image(new SpriteDrawable(sBanner));
        tableBG.add(banner).center().expandX();
    }

    public void update(float delta){
        if(supplier.isCountdown() && !supplier.hasBombExploded()){
            tableMid.setVisible(true);
            tableBG.setVisible(true);
            if((int)countdown > 3){
                center.setText("Ready?");
            } else if((int)countdown == 0){
                center.setText("Go!");
            } else {
                center.setText(""+(int)countdown);
            }
            countdown -= delta;
        }
        if(countdown < 0){
            supplier.setCountdown(false);
            countdown = COUNTDOWN_TIME;
            tableMid.setVisible(false);
            tableBG.setVisible(false);
            supplier.setBetween_C_E(false);
        }

        if(supplier.showsExpPlayer()){
            tableMid.setVisible(true);
            tableBG.setVisible(true);
            if(sEPTimer > 2){
                center.setText("Player: "+ supplier.getExplodedOn().getPlayer().getName() + " died!");
            }
            if(sEPTimer <= 2){
                if(!supplier.hasGameEnded()){
                    center.setText("Next Round");
                }
            }
            sEPTimer -= delta;
        }
        if(sEPTimer < 0){
            supplier.setCountdown(true);
            supplier.setShowExpPlayer(false);
            sEPTimer = showExpPlayerTime;
            tableMid.setVisible(false);
        }
    }

    public void setP0Text(String text){
        player0.setText(text);
    }

    public void setP1Text(String text){
        player1.setText(text);
    }

    public void setP2Text(String text){
        player2.setText(text);
    }

    public void setP3Text(String text){
        player3.setText(text);
    }

    public void setCenterText(String text){
        center.setText(text);
    }
}
