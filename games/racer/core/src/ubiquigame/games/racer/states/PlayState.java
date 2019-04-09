package ubiquigame.games.racer.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;
import com.badlogic.gdx.utils.Array;
import ubiquigame.common.Player;
import ubiquigame.common.constants.PlayerIdentifier;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.common.impl.InputState;
import ubiquigame.games.racer.RacerMain;
import ubiquigame.games.racer.gameObjects.Car;
import ubiquigame.games.racer.gameObjects.PolygonObject;
import ubiquigame.games.racer.gameObjects.Map;
import ubiquigame.games.racer.gameObjects.Powerup;
import ubiquigame.games.racer.overlay.HUD;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayState extends State {
    public static final int SHIFT_X = 190;
    public static final int SHIFT_Y = 220;
    private final int[] BORDER_OUTER_X = new int[]{557, 478, 415, 347, 298, 272, 245, 236, 237, 236,
            242, 259, 284, 318, 360, 428, 505, 565, 606, 632, 640, 621, 864, 850, 849,
            864, 889, 938, 1001, 1076, 1159, 1222, 1245, 1254, 1254, 1254, 1294, 1289, 1301, 1309,
            1343, 1373, 1403, 1454, 1533, 1588, 1623, 1646, 1682, 1737, 1797, 1859, 1922, 1967, 1978,
            1979, 1978, 1980, 1972, 1959, 1937, 1912, 1871, 1954, 2028, 2073, 2076, 2069, 2031, 1970,
            1912, 1828, 1752, 1678, 1597, 1532, 1477, 1408, 1351, 1281, 1183, 1106, 1027, 992, 982,
            1021, 1012, 994, 940, 885, 944, 971, 981, 946, 884, 557};
    private final int[] BORDER_OUTER_Y = new int[]{279, 284, 298, 324, 360, 392, 444, 496, 741, 996,
            1080, 1140, 1190, 1228, 1253, 1260, 1237, 1185, 1121, 1043, 984, 954, 954, 982, 1084,
            1122, 1153, 1179, 1191, 1192, 1180, 1149, 1113, 1041, 965, 885, 886, 974, 1043, 1105,
            1162, 1205, 1234, 1264, 1266, 1238, 1199, 1142, 1199, 1231, 1239, 1234, 1203, 1153, 1089,
            1021, 946, 873, 799, 742, 690, 653, 611, 616, 592, 547, 505, 437, 383, 334,
            300, 266, 245, 239, 238, 249, 264, 318, 266, 252, 254, 274, 299, 325, 428,
            479, 533, 576, 562, 527, 467, 426, 326, 298, 279, 279};
    private final int[] BORDER_INNER_X = new int[]{565, 471, 412, 379, 381, 383, 389, 400, 415, 453,
            482, 495, 496, 505, 540, 600, 670, 731, 788, 967, 987, 996, 996, 994, 995,
            1052, 1107, 1108, 1111, 1148, 1194, 1258, 1317, 1377, 1421, 1440, 1444, 1451, 1468, 1492,
            1534, 1577, 1635, 1690, 1730, 1753, 1768, 1773, 1776, 1782, 1805, 1828, 1833, 1832, 1817,
            1791, 1747, 1686, 1643, 1651, 1691, 1767, 1839, 1910, 1830, 1725, 1656, 1592, 1541, 1490,
            1407, 1352, 1319, 1287, 1252, 1147, 1196, 1260, 1308, 1355, 1374, 1373, 1336, 1282, 1193,
            1101, 1018, 971, 790, 754, 748, 773, 805};
    private final int[] BORDER_INNER_Y = new int[]{446, 456, 483, 530, 737, 990, 1056, 1093, 1121, 1088,
            1031, 939, 788, 736, 686, 648, 626, 620, 624, 712, 750, 798, 885, 990, 1048,
            1056, 1044, 889, 826, 773, 751, 743, 746, 760, 794, 830, 980, 1037, 1083, 1117,
            1043, 960, 891, 875, 887, 917, 958, 1002, 1051, 1081, 1094, 1084, 1019, 873, 794,
            736, 702, 658, 598, 538, 499, 477, 473, 472, 425, 390, 380, 383, 401, 437,
            472, 464, 440, 405, 398, 415, 451, 495, 535, 588, 626, 677, 717, 737, 740,
            730, 716, 705, 618, 576, 533, 486, 448};
    private final PolygonObject[] GRASS_OBJECTS = new PolygonObject[]{
            new PolygonObject(new int[]{569 - SHIFT_X, 568 - SHIFT_X, 608 - SHIFT_X, 606 - SHIFT_X},
                    new int[]{841 - SHIFT_Y, 972 - SHIFT_Y, 972 - SHIFT_Y, 840 - SHIFT_Y}, null, 0, 0),
            new PolygonObject(new int[]{1671 - SHIFT_X, 1653 - SHIFT_X, 1632 - SHIFT_X, 1620 - SHIFT_X, 1638 - SHIFT_X, 1659 - SHIFT_X, 1670 - SHIFT_X, 1679 - SHIFT_X, 1681 - SHIFT_X},
                    new int[]{1005 - SHIFT_Y, 1021 - SHIFT_Y, 1071 - SHIFT_Y, 1126 - SHIFT_Y, 1114 - SHIFT_Y, 1115 - SHIFT_Y, 1127 - SHIFT_Y, 1066 - SHIFT_Y, 1026 - SHIFT_Y}, null, 0, 0),
            new PolygonObject(new int[]{1927 - SHIFT_X, 1880 - SHIFT_X, 1837 - SHIFT_X, 1813 - SHIFT_X, 1819 - SHIFT_X, 1844 - SHIFT_X, 1880 - SHIFT_X, 1857 - SHIFT_X, 1853 - SHIFT_X, 1873 - SHIFT_X},
                    new int[]{573 - SHIFT_Y, 564 - SHIFT_Y, 564 - SHIFT_Y, 582 - SHIFT_Y, 613 - SHIFT_Y, 642 - SHIFT_Y, 667 - SHIFT_Y, 634 - SHIFT_Y, 606 - SHIFT_Y, 582 - SHIFT_Y}, null, 0, 0),
            new PolygonObject(new int[]{1328 - SHIFT_X, 1349 - SHIFT_X, 1373 - SHIFT_X, 1416 - SHIFT_X, 1464 - SHIFT_X, 1493 - SHIFT_X},
                    new int[]{389 - SHIFT_Y, 416 - SHIFT_Y, 436 - SHIFT_Y, 445 - SHIFT_Y, 424 - SHIFT_Y, 394 - SHIFT_Y}, null, 0, 0),
            new PolygonObject(new int[]{1297 - SHIFT_X, 1317 - SHIFT_X, 1316 - SHIFT_X, 1305 - SHIFT_X, 1279 - SHIFT_X, 1316 - SHIFT_X, 1338 - SHIFT_X, 1355 - SHIFT_X, 1353 - SHIFT_X, 1334 - SHIFT_X},
                    new int[]{559 - SHIFT_Y, 600 - SHIFT_Y, 635 - SHIFT_Y, 681 - SHIFT_Y, 718 - SHIFT_Y, 706 - SHIFT_Y, 690 - SHIFT_Y, 656 - SHIFT_Y, 622 - SHIFT_Y, 588 - SHIFT_Y}, null, 0, 0)
    };
    private ArrayList<PolygonObject> breakableWallObjects = new ArrayList<>();
    private ArrayList<PolygonObject> wallIslands = new ArrayList<>();

    private final Segment FINISHLINE = new Segment(new Vector3(544 - 190, 245 - 220, 0), new Vector3(544 - 190, 486 - 220, 0));
    public static final int START_POSITION_X = 410;
    public static final int START_POSITION_Y = RacerMain.getInstance().height - 80;
    private final int CAMERA_PADDING = 400;
    final float CAMERA_STEP = 0.002f;

    //Other Variables
    private Music music;
    private HUD hud;
    private ArrayList<Car> cars;
    private Map map;
    private ArrayList<Player> ranking;
    private HashMap<Player, Integer> score;
    private boolean gamestart;
    private boolean gameover;
    private float timerGameover;
    private final int GAMEOVER_DURATION = 15;
    private int ranking_counter;
    private Car tempCar;
    private boolean switchDone;
    private final int SWITCH_DURATION = 10;
    private int switchDuration;
    private float timerSwitch_seconds;
    private float timerSwitch;
    private boolean timerSwitch_running_playState;
    public float lastZoom;

    private ParticleEffect particle_firework_red;
    private ParticleEffect particle_firework_mix;
    private ParticleEffect particle_firework_blue;
    private ParticleEffect particle_firework_red2;
    private ParticleEffect particle_firework_blue2;
    private ParticleEffectPool particle_firework_red_pool;
    private ParticleEffectPool particle_firework_mix_pool;
    private ParticleEffectPool particle_firework_blue_pool;
    private ParticleEffectPool particle_firework_red2_pool;
    private ParticleEffectPool particle_firework_blue2_pool;
    private Array<PooledEffect> effects;
    private final int MAX_PARTICLES = 10;
    private int effects_counter;

    private float timer_powerup;
    private ArrayList <Powerup> powerups;
    private final float POWERUP_WAITTIME = 7.5f;
    private static final int POWERUP_WALLS_RADIUS_MIN = 15;
    private static final int POWERUP_WALLS_RADIUS_MAX = 35;

    private Sound soundfirework;
    private boolean soundplaying;

    public PlayState(GameStateManager gsm, HUD hud) {
        super(gsm);
        this.hud = hud;
        wallIslands.add(new PolygonObject(new int[]{1116 - SHIFT_X, 1113 - SHIFT_X, 1171 - SHIFT_X, 1190 - SHIFT_X, 1194 - SHIFT_X, 1163 - SHIFT_X},
                new int[]{546 - SHIFT_Y, 608 - SHIFT_Y, 617 - SHIFT_Y, 618 - SHIFT_Y, 605 - SHIFT_Y, 580 - SHIFT_Y}, null, 0, 2));
        wallIslands.add(new PolygonObject(new int[]{624 - SHIFT_X, 867 - SHIFT_X, 869 - SHIFT_X, 862 - SHIFT_X, 827 - SHIFT_X, 784 - SHIFT_X, 731 - SHIFT_X, 673 - SHIFT_X, 625 - SHIFT_X, 622 - SHIFT_X},
                new int[]{864 - SHIFT_Y, 864 - SHIFT_Y, 821 - SHIFT_Y, 781 - SHIFT_Y, 757 - SHIFT_Y, 746 - SHIFT_Y, 744 - SHIFT_Y, 753 - SHIFT_Y, 785 - SHIFT_Y, 827 - SHIFT_Y}, null, 0, 2));

        breakableWallObjects.add(new PolygonObject(calculateBorderX(660 - SHIFT_X, 20), calculateBorderY(RacerMain.getInstance().height - (870 - SHIFT_Y), 20), new Vector2(660 - SHIFT_X, RacerMain.getInstance().height - (870 - SHIFT_Y)), 20, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(675 - SHIFT_X, 20), calculateBorderY(RacerMain.getInstance().height - (960 - SHIFT_Y), 20), new Vector2(675 - SHIFT_X, RacerMain.getInstance().height - (960 - SHIFT_Y)), 20, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(740 - SHIFT_X, 35), calculateBorderY(RacerMain.getInstance().height - (867 - SHIFT_Y), 35), new Vector2(740 - SHIFT_X, RacerMain.getInstance().height - (867 - SHIFT_Y)), 35, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(708 - SHIFT_X, 15), calculateBorderY(RacerMain.getInstance().height - (941 - SHIFT_Y), 15), new Vector2(708 - SHIFT_X, RacerMain.getInstance().height - (941 - SHIFT_Y)), 15, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(687 - SHIFT_X, 11), calculateBorderY(RacerMain.getInstance().height - (848 - SHIFT_Y), 11), new Vector2(687 - SHIFT_X, RacerMain.getInstance().height - (848 - SHIFT_Y)), 11, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(755 - SHIFT_X, 22), calculateBorderY(RacerMain.getInstance().height - (948 - SHIFT_Y), 22), new Vector2(755 - SHIFT_X, RacerMain.getInstance().height - (948 - SHIFT_Y)), 22, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(828 - SHIFT_X, 10), calculateBorderY(RacerMain.getInstance().height - (910 - SHIFT_Y), 10), new Vector2(828 - SHIFT_X, RacerMain.getInstance().height - (910 - SHIFT_Y)), 10, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(807 - SHIFT_X, 27), calculateBorderY(RacerMain.getInstance().height - (852 - SHIFT_Y), 27), new Vector2(807 - SHIFT_X, RacerMain.getInstance().height - (852 - SHIFT_Y)), 27, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(848 - SHIFT_X, 14), calculateBorderY(RacerMain.getInstance().height - (868 - SHIFT_Y), 14), new Vector2(848 - SHIFT_X, RacerMain.getInstance().height - (868 - SHIFT_Y)), 14, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(808 - SHIFT_X, 20), calculateBorderY(RacerMain.getInstance().height - (950 - SHIFT_Y), 20), new Vector2(808 - SHIFT_X, RacerMain.getInstance().height - (950 - SHIFT_Y)), 20, 1));

        breakableWallObjects.add(new PolygonObject(calculateBorderX(1027 - SHIFT_X, 25), calculateBorderY(RacerMain.getInstance().height - (505 - SHIFT_Y), 25), new Vector2(1027 - SHIFT_X, RacerMain.getInstance().height - (505 - SHIFT_Y)), 25, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(1100 - SHIFT_X, 20), calculateBorderY(RacerMain.getInstance().height - (550 - SHIFT_Y), 20), new Vector2(1100 - SHIFT_X, RacerMain.getInstance().height - (550 - SHIFT_Y)), 20, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(1105 - SHIFT_X, 11), calculateBorderY(RacerMain.getInstance().height - (595 - SHIFT_Y), 11), new Vector2(1105 - SHIFT_X, RacerMain.getInstance().height - (595 - SHIFT_Y)), 11, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(1062 - SHIFT_X, 15), calculateBorderY(RacerMain.getInstance().height - (577 - SHIFT_Y), 15), new Vector2(1062 - SHIFT_X, RacerMain.getInstance().height - (577 - SHIFT_Y)), 15, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(1016 - SHIFT_X, 20), calculateBorderY(RacerMain.getInstance().height - (564 - SHIFT_Y), 20), new Vector2(1016 - SHIFT_X, RacerMain.getInstance().height - (564 - SHIFT_Y)), 20, 1));
        breakableWallObjects.add(new PolygonObject(calculateBorderX(1061 - SHIFT_X, 7), calculateBorderY(RacerMain.getInstance().height - (533 - SHIFT_Y), 7), new Vector2(1061 - SHIFT_X, RacerMain.getInstance().height - (533 - SHIFT_Y)), 7, 1));

        camera.setToOrtho(false, main.width, main.height);
        cars = new ArrayList<>();
        for (int i = 0; i < platform.getPlayers().length; i++){
            Color color = new Color(PlayerIdentifier.getColorForIndex(i + 1).getRed()/255f,PlayerIdentifier.getColorForIndex(i + 1).getGreen()/255f, PlayerIdentifier.getColorForIndex(i + 1).getBlue()/255f, PlayerIdentifier.getColorForIndex(i + 1).getAlpha()/255f);
            Car car = new Car(START_POSITION_X, START_POSITION_Y - i * 40, color);
            cars.add(car);
        }
        gamestart = false;
        gameover = false;
        timerGameover = 0;
        //fit map to image
        int[] tempOuterX = new int[BORDER_OUTER_X.length];
        int[] tempOuterY = new int[BORDER_OUTER_Y.length];
        int[] tempInnerX = new int[BORDER_INNER_X.length];
        int[] tempInnerY = new int[BORDER_INNER_Y.length];
        for (int i = 0; i < BORDER_OUTER_X.length; i++){
            tempOuterX[i] = BORDER_OUTER_X[i] - SHIFT_X;
        }
        for (int i = 0; i < BORDER_OUTER_Y.length; i++){
            tempOuterY[i] = BORDER_OUTER_Y[i] - SHIFT_Y;
        }
        for (int i = 0; i < BORDER_INNER_X.length; i++){
            tempInnerX[i] = BORDER_INNER_X[i] - SHIFT_X;
        }
        for (int i = 0; i < BORDER_INNER_Y.length; i++){
            tempInnerY[i] = BORDER_INNER_Y[i] - SHIFT_Y;
        }
        map = new Map(tempOuterX, tempOuterY, tempInnerX, tempInnerY, GRASS_OBJECTS, FINISHLINE, breakableWallObjects, wallIslands);
        ranking_counter = 1;
        ranking = new ArrayList();
        score = new HashMap();
        tempCar = cars.get(1);
        switchDone = false;
        switchDuration = 0;
        timerSwitch_seconds = 0;
        timerSwitch = 0;
        timerSwitch_running_playState = false;
        lastZoom = 1f;

        //Particles
        effects = new Array();
        effects_counter = 0;
        setParticles();

        timer_powerup =  - HUD.COUNTDOWNSECONDS - 1;
        powerups = new ArrayList();

        //Source: https://www.musicfox.com/info/kostenlose-gemafreie-musik.php
        music = Gdx.audio.newMusic(Gdx.files.internal("racer/sounds/music.ogg"));
        music.setVolume(platform.getMusicVolume());
        music.setLooping(true);
        music.play();

        soundfirework = Gdx.audio.newSound(Gdx.files.internal("racer/sounds/firework.ogg"));
        soundplaying = false;
    }

    private void setParticles(){
        //particle templates
        particle_firework_red = new ParticleEffect();
        particle_firework_red.load(Gdx.files.internal("racer/firework/firework_red_1.p"), Gdx.files.internal("racer/firework/"));

        particle_firework_blue = new ParticleEffect();
        particle_firework_blue.load(Gdx.files.internal("racer/firework/firework_blue_1.p"), Gdx.files.internal("racer/firework/"));

        particle_firework_mix = new ParticleEffect();
        particle_firework_mix.load(Gdx.files.internal("racer/firework/firework_mix_1.p"), Gdx.files.internal("racer/firework/"));

        particle_firework_red2 = new ParticleEffect();
        particle_firework_red2.load(Gdx.files.internal("racer/firework/firework_red_2.p"), Gdx.files.internal("racer/firework/"));

        particle_firework_blue2 = new ParticleEffect();
        particle_firework_blue2.load(Gdx.files.internal("racer/firework/firework_blue_2.p"), Gdx.files.internal("racer/firework/"));

        //particle pool
        particle_firework_red_pool = new ParticleEffectPool(particle_firework_red, MAX_PARTICLES, MAX_PARTICLES);
        particle_firework_blue_pool = new ParticleEffectPool(particle_firework_blue, MAX_PARTICLES, MAX_PARTICLES);
        particle_firework_mix_pool = new ParticleEffectPool(particle_firework_mix, MAX_PARTICLES, MAX_PARTICLES);
        particle_firework_red2_pool = new ParticleEffectPool(particle_firework_red2, MAX_PARTICLES, MAX_PARTICLES);
        particle_firework_blue2_pool = new ParticleEffectPool(particle_firework_blue2, MAX_PARTICLES, MAX_PARTICLES);
    }

    @Override
    public void handleInput() {
        if (gamestart){
            //sets the controller input
            for (int i = 0; i < platform.getPlayers().length; i++) {
                InputState input = platform.getPlayers()[i].getController().getInput();
                if (cars.get(i).getSwitch()){
                    cars.get(i).setLeft(input.isRight());
                    cars.get(i).setRight(input.isLeft());
                }else{
                    cars.get(i).setLeft(input.isLeft());
                    cars.get(i).setRight(input.isRight());
                }
                cars.get(i).setUp(input.isAccelerator());
                cars.get(i).setSpace(input.isBoost());
            }
        }
    }

    @Override
    public void update(float dt) {
        hud.startGameCountdown();
        if (hud.getReadyToStart() && !gamestart){
            gamestart = true;
            //start timer for cars
            for (Car car : cars) {
                car.setRacing(true);
            }
        }
        //update timer
        if (timerSwitch_running_playState){
            timerSwitch += dt;
            timerSwitch_seconds += dt;
            if (timerSwitch_seconds > 1){
                timerSwitch_seconds -= 1;
                hud.updateSwitch(switchDuration - (int) timerSwitch);
            }
            checkSwitchTimer(switchDuration);
        }
        if (gameover){
            timerGameover += dt;
        }
        timer_powerup += dt;

        //Update Objects
        handleInput();
        for (Car car : cars) {
            car.update(dt);
            //Collision: car with grass
            car.checkGrassCollision(map);
            //Collision: car with map/car
            car.checkWallCollision(map, dt);
            car.checkCarCollision(cars, car);
            //Update rounds
            car.updateRound(map);
            //Collision: car with powerup
            powerups = car.checkPowerupCollision(cars, powerups, map ,this);
        }
        //check control switch
        if (!switchDone && ((platform.getPlayers().length == 2 && cars.get(0).getRound() <= 1 && cars.get(1).getRound() <= 1) ||
                (platform.getPlayers().length == 3 && cars.get(0).getRound() <= 1 && cars.get(1).getRound() <= 1 && cars.get(2).getRound() <= 1) ||
                (platform.getPlayers().length == 4 && cars.get(0).getRound() <= 1 && cars.get(1).getRound() <= 1 && cars.get(2).getRound() <= 1 && cars.get(3).getRound() <= 1))){
            doSwitch(SWITCH_DURATION, false, null);
        }
        for (Car car : cars){
            if (car.getRound() > tempCar.getRound()){
                tempCar = car;
            }
        }
        //check ranking
        for (Car car : cars) {
            if (car.getRound() == 0 && car.getRanking() == 0) {
                ranking.add(platform.getPlayers()[cars.indexOf(car)]);
                car.setRanking(ranking_counter);
                car.setRacing(false);
                ranking_counter++;
            }
        }
        if (ranking_counter == platform.getPlayers().length && !gameover) { //GAME OVER
            gameover = true;
            //determine last player
            for (Car car : cars) {
                if (car.getRanking() == 0) {
                    car.setRanking(ranking_counter);
                    ranking.add(platform.getPlayers()[cars.indexOf(car)]);
                }
            }
            //set score
            for (Car car : cars){
                score.put(platform.getPlayers()[cars.indexOf(car)], (platform.getPlayers().length - car.getRanking()) * 250);
            }
        }

        //send GAME OVER
        if (timerGameover >= GAMEOVER_DURATION){
            main.gameover(new GameOverMessage(ranking, score));
            return;
        }

        //update particles
        if (gameover){
            effects_counter++;
            if (!soundplaying){
                soundplaying = true;
                soundfirework.play(platform.getSoundVolume());
            }
            soundfirework.play(platform.getSoundVolume());
            if (effects_counter >= 10 && effects.size < MAX_PARTICLES){
                effects_counter = 0;
                int x = (int) (Math.random() * camera.zoom * camera.viewportWidth);
                int y = (int) (Math.random() * camera.zoom * camera.viewportHeight);
                PooledEffect effect;
                double rand = Math.random();
                if (rand < 0.2){
                    effect = particle_firework_red_pool.obtain();
                }else if (rand > 0.2 && rand < 0.4) {
                    effect = particle_firework_mix_pool.obtain();
                }else if (rand > 0.4 && rand < 0.6){
                    effect = particle_firework_blue_pool.obtain();
                }else if (rand > 0.6 && rand < 0.8){
                    effect = particle_firework_red2_pool.obtain();
                }else{
                    effect = particle_firework_blue2_pool.obtain();
                }
                effect.scaleEffect((float)Math.random() * 0.4f);
                effect.setPosition(camera.position.x + (x - camera.zoom * camera.viewportWidth / 2), camera.position.y + (y - camera.zoom * camera.viewportHeight / 2));
                effects.add(effect);
            }
        }

        //Update HUD
        for (int i = 0; i < cars.size(); i++) {
            hud.setRound(i + 1, cars.get(i).getRound());
            hud.setBoost(i + 1, cars.get(i).getBoosts());
            hud.setRanking(i + 1, cars.get(i).getRanking());
            hud.setTime(i + 1, cars.get(i).getTime());
        }
        hud.update(dt);

        //create powerups
        if (timer_powerup >= POWERUP_WAITTIME){
            createPowerUp();
        }
    }

    @Override
    public void render(SpriteBatch sb, ShapeRenderer sr) {
        if (gameover){
            updateCamera(true);
        } else {
            updateCamera(false);
        }
        sb.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);
        map.render(sr, sb);
        for (PolygonObject wall : wallIslands){
            wall.render(sr, sb);
        }
        for (Car car : cars) {
            car.render(sr, sb);
        }
        for (PolygonObject grass : GRASS_OBJECTS){
            grass.render(sr, sb);
        }
        for (PolygonObject wall : breakableWallObjects){
            wall.render(sr, sb);
        }
        for (Powerup powerup: powerups){
            powerup.render(sr, sb);
        }
        //Particles
        if (gameover){
            for (int i = 0; i < cars.size(); i++){
                if (cars.get(i).getRanking() == 1){
                    hud.gameover(sb, effects, ranking.get(0).getName(), new Color(PlayerIdentifier.getColorForIndex(i+ 1).getRed()/255f, PlayerIdentifier.getColorForIndex(i + 1).getGreen()/255f, PlayerIdentifier.getColorForIndex(i + 1).getBlue()/255f, PlayerIdentifier.getColorForIndex(i + 1).getAlpha()/255f));
                    break;
                }
            }
        }
    }

    @Override
    public void dispose() {
        main.hud.dispose();
        music.stop();
        music.dispose();
        soundfirework.stop();
        soundfirework.dispose();

        for (int i = 0; i < effects.size - 1; i++)
            effects.get(i).free(); //free all the effects back to the pool
        effects.clear();

        for (Powerup powerup: powerups) {
            powerup.dispose();
        }
    }

    private void updateCamera(boolean gameover){
        int minX, minY;
        int maxX, maxY;
        float dx, dy;
        if (!gameover){
            minX = minY = Integer.MAX_VALUE;
            maxX = maxY = 0;

            for (Car car : cars){
                if (car.getX() > maxX){
                    maxX = car.getX();
                }
                if (car.getX() < minX){
                    minX = car.getX();
                }
                if (car.getY() > maxY){
                    maxY = car.getY();
                }
                if (car.getY() < minY){
                    minY = car.getY();
                }
            }

            dx = maxX - minX;
            dy = maxY - minY;
            camera.position.x = (maxX + minX) / 2;
            camera.position.y = (maxY + minY) / 2;

            dx = (dx + CAMERA_PADDING)/ main.width;
            dy = (dy + CAMERA_PADDING) / main.height;
            float max = Math.max(dx, dy);
            if (max < 0.1){
                max = 0.1f;
            }
            if(lastZoom > max && Math.abs(lastZoom - max) >= 0.1){
                lastZoom -= CAMERA_STEP;
            } else if(lastZoom < max && Math.abs(lastZoom - max) >= 0.1){
                lastZoom += CAMERA_STEP;
            }
            //maxZoom
            if (lastZoom > 1.05f){
                lastZoom = 1.05f;
            }
        } else {
            Car tempCar = null;
            for (Car car : cars){
                if (car.getRanking() == 1){
                    tempCar = car;
                    break;
                }
            }
            if (Math.abs(camera.position.x - tempCar.getX()) >= 1) {
                if (camera.position.x > tempCar.getX()) {
                    camera.position.x -= (int) Math.abs(camera.position.x - tempCar.getX()) * 0.1;
                } else {
                    camera.position.x += (int) Math.abs(camera.position.x - tempCar.getX()) * 0.1;
                }
            }
            if (Math.abs(camera.position.y - tempCar.getY()) >= 1){
                if (camera.position.y > tempCar.getY()){
                    camera.position.y -= (int) Math.abs(camera.position.y - tempCar.getY()) * 0.1;
                } else {
                    camera.position.y += (int) Math.abs(camera.position.y - tempCar.getY()) * 0.1;
                }
            }

            if(lastZoom > 0.3f){
                lastZoom -= CAMERA_STEP;
            }

        }
        camera.zoom = lastZoom;
        camera.update();
    }

    public void doSwitch(int duration, boolean isPowerup, Car carPowerup){
        timerSwitch_running_playState = true;
        switchDuration = duration;
        hud.updateSwitch(duration);
        if (isPowerup){
            timerSwitch = 0;
            timerSwitch_seconds = 0;
            for (Car car: cars){
                if (car != carPowerup){
                    car.setSwitch(true);
                }
            }
        }else{
            timerSwitch = 0;
            timerSwitch_seconds = 0;
            switchDone = true;
            for (Car car: cars){
                if (car != tempCar){
                    car.setSwitch(true);
                }
            }
        }
    }

    private void checkSwitchTimer(int duration){
        if (timerSwitch >= duration){
            timerSwitch_seconds = 0;
            timerSwitch = 0;
            timerSwitch_running_playState = false;
            for (Car car : cars){
                car.setSwitch(false);
            }
            hud.updateSwitch(0);
        }
    }

    private void createPowerUp(){
        timer_powerup = 0;
        int tempx, tempy, rand;
        boolean valid;
        int tries = 0;
        do{
            tries++;
            valid = true;
            tempx = (int)(Math.random() * main.width);
            tempy = (int)(Math.random() * main.height);
            rand = (int) (Math.random() * 5) + 1;
            if (!map.isPointInMap(cars, tempx, tempy, Powerup.RADIUS)){
                valid = false;
            }
        }while (!valid || tries < 100);
        powerups.add(new Powerup(tempx, tempy, rand));
    }

    public void createWalls(int number){
        for (int i = 0; i < number; i++){
            boolean valid;
            int tempx, tempy;
            float radius;
            int tries = 0;
            do{
                tries++;
                valid = true;
                tempx = (int)(Math.random() * main.width);
                tempy = (int)(Math.random() * main.height);
                radius = (int)(Math.random() * (POWERUP_WALLS_RADIUS_MAX - POWERUP_WALLS_RADIUS_MIN) + POWERUP_WALLS_RADIUS_MIN);
                if (!map.isPointInMap(cars, tempx, tempy, radius)){
                    valid = false;
                }
                //check powerup collision
                for (Powerup powerup : powerups){
                    if (Math.sqrt(Math.pow(tempx - powerup.getPosition().x, 2) + Math.pow(tempy - powerup.getPosition().y, 2)) <= radius + Powerup.RADIUS){
                        valid = false;
                    }
                }
            }while (!valid || tries < 100);
            int[] borderX = calculateBorderX(tempx, radius);
            int[] borderY = calculateBorderY(tempy, radius);
            map.setBreakableWallObject(borderX, borderY, new Vector2(tempx, tempy), radius, 1);
        }
    }

    private int[] calculateBorderX(int tempx, float radius){
        return new int[]{(int)(tempx + MathUtils.cos(2 * MathUtils.PI/5) * radius),
                (int)(tempx + MathUtils.cos(2 * MathUtils.PI/5 * 2) * radius),
                (int)(tempx + MathUtils.cos(2 * MathUtils.PI/5 * 3) * radius),
                (int)(tempx + MathUtils.cos(2 * MathUtils.PI/5 * 4) * radius),
                (int)(tempx + MathUtils.cos(2 * MathUtils.PI/5 * 5) * radius)};
    }

    private int[] calculateBorderY(int tempy, float radius){
        return new int[]{(int)(RacerMain.getInstance().height - (tempy + MathUtils.sin(2 * MathUtils.PI/5) * radius)),
                (int)(RacerMain.getInstance().height - (tempy + MathUtils.sin(2 * MathUtils.PI/5 * 2) * radius)),
                (int)(RacerMain.getInstance().height - (tempy + MathUtils.sin(2 * MathUtils.PI/5 * 3) * radius)),
                (int)(RacerMain.getInstance().height - (tempy + MathUtils.sin(2 * MathUtils.PI/5 * 4) * radius)),
                (int)(RacerMain.getInstance().height - (tempy + MathUtils.sin(2 * MathUtils.PI/5 * 5) * radius))};
    }
}
