package ubiquigame.games.EnchantedLabyrinth.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import ubiquigame.common.Player;
import ubiquigame.common.impl.InputState;
import ubiquigame.games.EnchantedLabyrinth.TextureManager;

public class LabyrinthPlayer {
	private Position position;
	private SpriteBatch spriteBatch;
	private Texture noHammerTexture;
	private Texture hammerTexture;
	private final Labyrinth labyrinth;
	private int numCollectedStones;
	private Label labelToUpdate;
	private boolean treasureTaken;
	private Player platformPlayer;

	public LabyrinthPlayer(int playerNumber, Labyrinth labyrinth, Player platformPlayer, TextureManager textureManager,
			Label label) {
		position = labyrinth.getStartPosition().clone();
		String noHammerKey = "playerNoHammer" + (playerNumber);
		String hammerKey = "playerHammer" + (playerNumber);
		noHammerTexture = textureManager.getTexture(noHammerKey);
		hammerTexture = textureManager.getTexture(hammerKey);
		spriteBatch = new SpriteBatch();
		this.labyrinth = labyrinth;
		this.platformPlayer = platformPlayer;
		setLabelToUpdate(label);
	}

	public void render() {
		spriteBatch.begin();
		Texture texture = hasHammer() ? hammerTexture : noHammerTexture;
		int tileSize = labyrinth.getTileSize();
		int renderX = labyrinth.getRenderX(position.getCol());
		int renderY = labyrinth.getRenderY(position.getRow());
		spriteBatch.draw(texture, renderX, renderY, tileSize, tileSize);
		spriteBatch.end();

		Position toLeftPosition = new Position(position.getRow(), position.getCol() - 1);
		InputState input = platformPlayer.getController().getInput();
		InputState lastInput = platformPlayer.getController().getLastInput();
		if (input.isLeft() && !lastInput.isLeft() && canEnter(toLeftPosition)) {
			position.toLeft();
		}

		Position toRightPosition = new Position(position.getRow(), position.getCol() + 1);
		if (input.isRight() && !lastInput.isRight() && canEnter(toRightPosition)) {
			position.toRight();
		}

		Position toUpPosition = new Position(position.getRow() - 1, position.getCol());
		if (input.isUp() && !lastInput.isUp() && canEnter(toUpPosition)) {
			position.toUp();
		}

		Position toDownPosition = new Position(position.getRow() + 1, position.getCol());
		if (input.isDown() && !lastInput.isDown() && canEnter(toDownPosition)) {
			position.toDown();
		}

		if (position.equals(labyrinth.getTreasurePosition())) {
			labyrinth.breakWalls(position);
			treasureTaken = true;
		}
	}

	public int getScore() {
		return numCollectedStones + (isTreasureTaken() ? 5 : 0);
	}

	public boolean isTreasureTaken() {
		return treasureTaken;
	}

	public Position getPosition() {
		return position;
	}

	public void addStone() {
		numCollectedStones++;
		updateLabelData();
	}

	public boolean canCollect() {
		return numCollectedStones < 5;
	}

	private boolean hasHammer() {

		return numCollectedStones == 5;
	}

	private boolean canEnter(Position targetPosition) {
		if (targetPosition.equals(labyrinth.getTreasurePosition())) {
			return hasHammer();

		}

		return labyrinth.isPassable(position.getRow(), position.getCol(), targetPosition.getRow(),
				targetPosition.getCol());
	}

	public void setLabelToUpdate(Label label) {
		labelToUpdate = label;
		updateLabelData();
	}

	private void updateLabelData() {

		String stoneInfo = String.format("%d Stein%s", numCollectedStones, numCollectedStones == 1 ? "" : "e");

		if (hasHammer()) {

			stoneInfo = "HAMMERTIME";
		}
		labelToUpdate.setWidth(500);
		labelToUpdate.setHeight(5);
		labelToUpdate.setColor(Color.BLACK);
		labelToUpdate.setText(platformPlayer.getName() + " - " + stoneInfo + "    ");

	}

	public Player getPlatformPlayer() {
		return platformPlayer;
	}
}
