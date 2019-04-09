package ubiquigame.games.tetris.gameObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GameField {
	private int width; // Width - anzahl der Spalten
	private int height; // Hohe - anzahl der Reihen
	private int blockSize;

	private int bottomLeftX;
	private int bottomLeftY;

	private Block[][] blocks;

	public GameField(int width, int height) {
		this.width = width;
		this.height = height;
		this.blockSize = 25;

		blocks = new Block[height][width];

	}

	public void draw(ShapeRenderer sr, SpriteBatch sb) {
		sr.setColor(Color.DARK_GRAY);

		Vector2 bl = getBottomLeftPoint();
		Vector2 br = getBottomRightPoint();
		Vector2 tr = getTopRightPoint();
		Vector2 tl = getTopLeftPoint();

		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.setColor(Color.WHITE);
		sr.rect(bl.x, bl.y, getTotalRenderWidth(), getTotalRenderHeight());
		sr.setColor(Color.GRAY);
		sr.rectLine(tl, bl, 5.0f);
		sr.rectLine(bl, br, 5.0f);
		sr.rectLine(br, tr, 5.0f);
		sr.end();

		sr.begin(ShapeRenderer.ShapeType.Line);

		sr.setColor(Color.LIGHT_GRAY);
		for (int i = 1; i < height; i++) {
			int rowY = (int) bl.y + i * blockSize;
			sr.line(new Vector2(bl.x, rowY), new Vector2(br.x, rowY));
		}

		for (int i = 1; i < width; i++) {
			int colX = (int) bl.x + i * blockSize;
			sr.line(new Vector2(colX, bl.y), new Vector2(colX, tl.y));
		}

		sr.end();

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Block block = blocks[i][j];
				if (block == null)
					continue;

				block.draw(getCenterX(j), getCenterY(i), blockSize, sb);
			}
		}
	}

	private Vector2 getBottomLeftPoint() {
		return new Vector2(bottomLeftX, bottomLeftY);
	}

	private Vector2 getBottomRightPoint() {
		Vector2 bl = getBottomLeftPoint();
		return new Vector2(bl.x + getTotalRenderWidth(), bl.y);
	}

	private Vector2 getTopLeftPoint() {
		Vector2 bl = getBottomLeftPoint();
		return new Vector2(bl.x, bl.y + getTotalRenderHeight());
	}

	private Vector2 getTopRightPoint() {
		Vector2 br = getBottomRightPoint();
		return new Vector2(br.x, br.y + getTotalRenderHeight());
	}

	public int getTotalRenderWidth() {
		return width * blockSize;
	}

	public int getTotalRenderHeight() {
		return height * blockSize;
	}

	public int getCenterX(int col) {
		return (int) getTopLeftPoint().x + col * blockSize + blockSize / 2;
	}

	public int getCenterY(int row) {
		return (int) getTopLeftPoint().y - row * blockSize - blockSize / 2;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public boolean collidesWithBlocks(Block block) {
		for (BlockPart blockPart : block.getBlockParts()) {
			int row = block.getRow() + blockPart.getOffsetRow();
			int col = block.getCol() + blockPart.getOffsetCol();

			if (row < 0 || row >= height || col < 0 || col >= width) {
				continue;
			}

			if (blocks[row][col] != null) {
				return true;
			}
		}

		return false;
	}

	public boolean collidesWithLeftRightBorder(Block block) {
		for (BlockPart blockPart : block.getBlockParts()) {
			int col = block.getCol() + blockPart.getOffsetCol();

			if (col < 0 || col >= width) {
				return true;
			}
		}

		return false;
	}

	public boolean collidesWithBottomBorder(Block block) {
		for (BlockPart blockPart : block.getBlockParts()) {
			int row = block.getRow() + blockPart.getOffsetRow();

			if (row >= height) {
				return true;
			}
		}

		return false;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public List<Block[]> saveBlock(Block block) {
		for (BlockPart blockPart : block.getBlockParts()) {
			int row = block.getRow() + blockPart.getOffsetRow();
			int col = block.getCol() + blockPart.getOffsetCol();
			BlockPart[] blockParts = new BlockPart[] { new BlockPart(0, 0) };
			blocks[row][col] = new Block(blockParts, row, col, block.getColorTexture(), false);
		}

		return clearRows();
	}

	private List<Block[]> clearRows() {
		List<Block[]> deletedRows = new ArrayList<Block[]>();
		boolean somethingToClear = false;
		for (int i = 0; i < height; i++) {
			boolean allFilled = true;
			for (int j = 0; j < width; j++) {
				if (blocks[i][j] == null) {
					allFilled = false;
					break;
				}
			}

			if (allFilled) {
				somethingToClear = true;
				break;
			}
		}

		if (!somethingToClear) {
			return deletedRows;
		}

		Block[][] newBlocks = new Block[height][width];
		int writeRow = height - 1;

		for (int i = height - 1; i >= 0; i--) {
			boolean allFilled = true;
			for (int j = 0; j < width; j++) {
				if (blocks[i][j] == null) {
					allFilled = false;
					break;
				}
			}

			if (allFilled) {
				Block[] rowCopy = new Block[width];
				for (int j = 0; j < width; j++)
					rowCopy[j] = blocks[i][j];

				deletedRows.add(rowCopy);
				continue;
			}

			for (int j = 0; j < width; j++)
				newBlocks[writeRow][j] = blocks[i][j];

			writeRow--;
		}

		blocks = newBlocks;

		return deletedRows;
	}

	public void setBottomLeftPoint(int x, int y) {
		bottomLeftX = x;
		bottomLeftY = y;
	}

	public void addRowFromBottom(Block[] row) {
		Block[][] newBlocks = new Block[height][width];
		for (int j = 0; j < width; j++) {
			newBlocks[height - 1][j] = row[j];
		}

		for (int i = height - 1; i >= 1; i--) {
			for (int j = 0; j < width; j++) {
				newBlocks[i - 1][j] = blocks[i][j];
			}
		}

		blocks = newBlocks;
	}
}
