package ubiquigame.games.memory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ubiquigame.common.ControllerFace;
import ubiquigame.common.GameInfo;
import ubiquigame.common.UbiquiGamePlatform;
import ubiquigame.common.impl.AbstractUbiquiGame;
import ubiquigame.common.Player;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.common.impl.InputState;
import ubiquigame.common.impl.ScreenDimension;

import java.awt.Dimension;
import java.util.*;
import java.util.List;

public class MemoryGame extends AbstractUbiquiGame {

	SpriteBatch batch;
	Texture lemon, orange, peanut, monkey, avocado, back, peach;
	MemoryCard[][] cardSet;
	Texture[][] defaultPictures;
	Sprite[][] sprite;
	Map<Player, Integer> scoreMap;
	Player[] players;
	Stack<Sprite> peer;
	Player current;
	MemoryCard selectedCard;
	ShapeRenderer shapeRenderer;
	Vector3 screenCoordinates;
	Vector3 worldCoordinates;
	OrthographicCamera cam;
	Sound walk, matchTone, applause;
	BitmapFont font;
	int clicks, amountofCards, openCards, cardSelectedX, cardSelectedY, startX, startY;
	boolean gameOver;
	boolean enter = true;
	float lastKeyPressed = 0;
	float keyWait = .12f;
	private Sprite backSprite;
	private Rectangle[] playerLocations;
	static int currentplayer;

	public MemoryGame(UbiquiGamePlatform platform) {
		super(platform);
	}

	@Override
	public ControllerFace getControllerFace() {
		return ControllerFace.Controller_Cross_A;
	}

	@Override
	public void create() {
		back = new Texture("memory/Traube.PNG");
		lemon = new Texture("memory/Lemon.jpg");
		orange = new Texture("memory/Orange.jpg");
		peanut = new Texture("memory/Erdnuss.jpg");
		monkey = new Texture("memory/Monkey.jpg");
		avocado = new Texture("memory/Avocado.jpg");
		peach = new Texture("memory/Peach.jpg");
		backSprite = new Sprite(back);
		this.players = getPlatformInstance().getPlayers();
		batch = new SpriteBatch();
		screenCoordinates = new Vector3();
		worldCoordinates = new Vector3();
		cam = new OrthographicCamera();
		peer = new Stack<Sprite>();
		startX = 470;
		startY = 200;
		clicks = 0;
		openCards = 0;
		scoreMap = new HashMap<>();
		cardSelectedX = 2;
		cardSelectedY = 0;
		currentplayer = 0;
		walk = Gdx.audio.newSound(Gdx.files.internal("memory/clickTon.wav"));
		matchTone = Gdx.audio.newSound(Gdx.files.internal("memory/bling.wav"));
		applause = Gdx.audio.newSound(Gdx.files.internal("memory/applause6.wav"));

		for (int i = 0; i < players.length; i++) {
			scoreMap.put(players[i], 0);
		}

		current = players[0];
		createCards();
		shapeRenderer = new ShapeRenderer();

		font = new BitmapFont();
		font.getData().scale(1);
		font.setColor(Color.BLACK);
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
	}

	@Override
	public void update(float delta) {
		lastKeyPressed += delta;
		gameLogic(delta);

	}

	/**
	 * The Game Logic: a Stack takes two Sprites with two Textures and checks them
	 * for equality. if they are equal the Current Player gets points if not the
	 * next Player turn starts.The Stack will be have only two elements. it´s used
	 * to check for equality and for counting purpose.
	 */
	boolean changed = false;
	boolean wrongCard = false;
	final float wrongCardTime = 0.3f;
	float wrongCardTimer = wrongCardTime;
	private Viewport viewPort;

	private void gameLogic(float delta) {

		InputState input = current.getController().getInput();
		if (input.isA()) {

			for (int k = 0; k < sprite.length; k++) {
				for (int i = 0; i < sprite[k].length; i++) {
					Sprite chosen = this.sprite[k][i];
					MemoryCard currentCard = cardSet[k][i];

					if (currentCard.equals(selectedCard) && isCovered(chosen)) {

						if (peer.size() <= 1) {
							peer.push(chosen).setTexture(currentCard.getPicture());

						}

						if (peer.size() == 2) {
							if (peer.get(0).getTexture().equals(peer.get(1).getTexture())) {
								matchTone.play(getPlatformInstance().getSoundVolume());
								addPoints(current, 1);
								openCards += 2;
								peer.clear();
							}

							else {
								System.out.println("Else");
								if (!changed) {
									wrongCard = true;
									changed = true;
								}

							}

						}
					}
				}

			}
		}
		if (wrongCardTimer < 0) {
			System.out.println("Timer");
			peer.peek().setTexture(back);
			peer.pop();
			peer.peek().setTexture(back);
			peer.pop();
			setNextPlayer();
			wrongCard = false;
			changed = false;
			wrongCardTimer = wrongCardTime;
		}
		if (wrongCard) {
			wrongCardTimer -= delta;
		}

		endTone();

		if (lastKeyPressed > keyWait) {
			lastKeyPressed = 0;

			if (input.isRight()) {
				cardSelectedY = (cardSelectedY + 1) % (cardSet.length);
				walk.play(getPlatformInstance().getSoundVolume());

			}
			if (input.isLeft()) {
				cardSelectedY = (cardSelectedY - 1) % (cardSet.length);
				walk.play(getPlatformInstance().getSoundVolume());
			}
			if (input.isUp()) {

				cardSelectedX = (cardSelectedX + 1) % (cardSet[0].length);
				walk.play(getPlatformInstance().getSoundVolume());
			}
			if (input.isDown()) {

				cardSelectedX = (cardSelectedX - 1) % (cardSet[0].length);
				walk.play(getPlatformInstance().getSoundVolume());
			}
			if (cardSelectedX < 0) {
				cardSelectedX = cardSet[0].length + cardSelectedX;

			}
			if (cardSelectedY < 0) {
				cardSelectedY = cardSet.length + cardSelectedY;

			}
		}
		selectedCard = cardSet[cardSelectedY][cardSelectedX];
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
ScreenDimension dim = getPlatformInstance().getScreenDimension();
		cam.setToOrtho(false, dim.getWidth(), dim.getHeight());
		cam.update(); //unproject(worldCoordinates);
		drawCards(dim.getWidth() /2, dim.getHeight()/2);
		playerInfos();

	}

	@Override
	public GameInfo getGameInfo() {
		return new MemoryGameInfo();
	}

	/**
	 * Creates a Texture Array ans a sprite Array that ist drawn with the width and
	 * hights of the Texture and a Padding between each rechtangle. A selected card
	 * is set at the beginig of the Set of Cards.
	 */
	private void createCards() {
		sprite = new Sprite[4][3];
		cardSet = new MemoryCard[4][3];

		defaultPictures = new Texture[][] { { lemon, peach, orange }, { peanut, monkey, avocado },
				{ peach, lemon, orange }, { peanut, avocado, monkey } };
		shuffle(defaultPictures);
		for (int b = 0; b < defaultPictures.length; b++) {
			for (int c = 0; c < defaultPictures[b].length; c++) {
				amountofCards = cardSet.length * cardSet[c].length;
				sprite[b][c] = new Sprite(back);
				cardSet[b][c] = new MemoryCard(defaultPictures[b][c], defaultPictures[b][c].getWidth() + 50,
						defaultPictures[b][c].getHeight() + 50);
			}
		}
		selectedCard = cardSet[0][0];
	}

	/**
	 * Shuffles a given Texture Array by changing random cells.
	 *
	 * @param a
	 */
	void shuffle(Texture[][] a) {
		Random random = new Random();
		for (int i = a.length - 1; i > 0; i--) {
			for (int j = a[i].length - 1; j > 0; j--) {
				int m = random.nextInt(i + 1);
				int n = random.nextInt(j + 1);
				Texture temp = a[i][j];
				a[i][j] = a[m][n];
				a[m][n] = temp;
			}
		}
	}

	/**
	 * Draws a Card Array on a beginning coordinate(x,Y) and with the Card width k
	 * times and hight i times. a Batch has to begin and end. A Selected Cards Color
	 * could be set in any Color.
	 */
	private void drawCards(int x, int y) {
		batch.begin();
		batch.setProjectionMatrix(cam.combined);
		
		
		int countY = sprite[0].length;
		int countX = sprite.length;
		
		
		int totalWidth = (int) ((countX -1) * 50 + countX * sprite[0][0].getWidth());
		int totalHeight = (int) ((countY -1) * 50 + countY * sprite[0][0].getHeight());
		int offsetX = x - totalWidth /2;
		int offsetY = y - totalHeight /2;
		
		for (int k = 0; k < sprite.length; k++) {
			for (int i = 0; i < sprite[k].length; i++) {
				MemoryCard currentCard = cardSet[k][i];
				sprite[k][i].setPosition(offsetX + currentCard.getX() * k, offsetY + currentCard.getX() * i);

				if (currentCard.equals(selectedCard)) {
					batch.setColor(1f, 1f, 0f, 1f);

				} else {
					batch.setColor(1f, 1f, 1f, 1f);
				}

				batch.draw(sprite[k][i], offsetX + currentCard.getX() * k, offsetY + currentCard.getX() * i);

			}

		}
		batch.end();

	}

	/**
	 * renders rectangles on the screen for fix coordinates, width and hight. a Font
	 * is drawn on this rectangles with Player input.
	 */
	private void playerInfos() {

		currentplayer %= players.length;

		ScreenDimension dim = getPlatformInstance().getScreenDimension();
		int offset = 20;
		int offsetFont = 10;
		int infoWidth = 300;
		int infoHeight = 100;

		playerLocations = new Rectangle[] {
				new Rectangle(offset, dim.getHeight() - offset - infoHeight, infoWidth, infoHeight),
				new Rectangle(dim.getWidth() - offset - infoWidth, dim.getHeight() - offset - infoHeight, infoWidth,
						infoHeight),
				new Rectangle(offset, offset, infoWidth, infoHeight),
				new Rectangle(dim.getWidth() - offset - infoWidth, offset, infoWidth, infoHeight) };

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setProjectionMatrix(cam.combined);
		for (int i = 0; i < players.length; i++) {
			Rectangle hudRect = playerLocations[i];
			if (i == currentplayer) {
				shapeRenderer.setColor(Color.RED);
			} else {
				shapeRenderer.setColor(Color.GOLD);
			}
			shapeRenderer.rect(hudRect.x, hudRect.y, hudRect.width, hudRect.height);
		}
		shapeRenderer.end();

		batch.begin();
		batch.setProjectionMatrix(cam.combined);
		for (int i = 0; i < players.length; i++) {
			font.draw(batch, players[i].getName() + " " + "Punkte: " + getPoints(players[i]),
					playerLocations[i].getX() + offsetFont, playerLocations[i].getY() + 60);
		}

		batch.end();
		if (openCards == amountofCards) {

			GameOverMessage message = new GameOverMessage(getSortedPlayerArray(players), scoreMap);
			this.getPlatformInstance().notifyCurrGameOver(message);

		}
		if (openCards == amountofCards) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			shapeRenderer.setColor(Color.GOLD);
			shapeRenderer.rect(0, 400, 1975, 300);
			shapeRenderer.end();

			font = new BitmapFont();
			font.getData().scale(5);
			font.setColor(Color.RED);
			font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

			int max = 0;
			for (int i = 0; i < players.length; i++) {
				if (getPoints(players[i]) > max) {
					max = getPoints(players[i]);
					batch.begin();
					font.draw(batch, players[i].getName() + " " + max, 600, 580);
					batch.end();

				}
			}
		}
	}

	public void endTone() {
		if (openCards == amountofCards) {
			matchTone.stop();
			applause.play(getPlatformInstance().getSoundVolume());

		}
	}

	/**
	 * Sorts a given Player Array from High score to lower score and returns the
	 * sorted List.
	 *
	 * @param players
	 * @return {@code} returns a List of Player
	 */
	private List<Player> getSortedPlayerArray(Player[] players) {
		List<Player> list = Arrays.asList(players);
		list.sort(Comparator.comparingInt(player -> scoreMap.get(player)));
		Collections.reverse(list);
		return list;
	}

	/**
	 * Adds Points of Player in a HashMap.
	 *
	 * @param player
	 * @param points
	 */
	private void addPoints(Player player, int points) {
		int score = scoreMap.get(player);
		score = score + points;
		scoreMap.put(player, score);

	}

	/**
	 * returns a scoreMap within total Player points.
	 *
	 * @param player
	 * @return
	 */
	private int getPoints(Player player) {
		return scoreMap.get(player);
	}

	/**
	 * returns true or false. it
	 *
	 * @param sprite
	 * @return {@code true} if Sprites Rectangle has Cursor Coordinates inside,
	 *         {@code false} iif Sprites Rectangle has no Cursor Coordinates inside.
	 */
	private boolean isSpriteClicked(Sprite sprite) {
		return sprite.getBoundingRectangle().contains(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
	}

	/**
	 * Returns if the card is still covered.
	 *
	 * @param sprite
	 * @return {@code true} if card is covered, {@code false} else
	 */
	private boolean isCovered(Sprite sprite) {

		return sprite.getTexture().equals(back);
	}

	/**
	 * sets the next Player and increments an Integer variable for seting the
	 * Players ShapeColor in PlayerInfo.
	 */
	private void setNextPlayer() {
		currentplayer++;
		for (int i = 0; i < players.length; i++) {
			shapeRenderer.setColor(Color.RED);
			if (current.equals(players[i])) {
				current = players[(i + 1) % players.length];

				return;
			}
		}

	}

}
