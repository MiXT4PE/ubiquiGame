package ubiquigame.games.tetris.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Block {
    private BlockPart[] blockParts;
    private int row;
    private int col;
    private boolean isRotationPossible;
    private Texture colorTexture;

    public Block(BlockPart[] blockParts, int row, int col, Texture colorTexture, boolean isRotationPossible) {
        this.blockParts = blockParts;
        this.row = row;
        this.col = col;
        this.colorTexture = colorTexture;
        this.isRotationPossible = isRotationPossible;
    }

    public void rotateBy90() {
        if(!isRotationPossible) {
            return;
        }

        BlockPart[] newBlockParts = new BlockPart[blockParts.length];
        for(int i = 0; i < blockParts.length; i++) {
            int row = blockParts[i].getOffsetRow();
            int col = blockParts[i].getOffsetCol();
            int rrow = col;
            int rcol = -row;

            newBlockParts[i] = new BlockPart(rrow, rcol);
        }

        blockParts = newBlockParts;
    }

    public void rotateBackBy90() {
        if(!isRotationPossible) {
            return;
        }

        BlockPart[] newBlockParts = new BlockPart[blockParts.length];
        for(int i = 0; i < blockParts.length; i++) {
            int row = blockParts[i].getOffsetRow();
            int col = blockParts[i].getOffsetCol();
            int rrow = -col;
            int rcol = row;

            newBlockParts[i] = new BlockPart(rrow, rcol);
        }

        blockParts = newBlockParts;
    }

    public BlockPart[] getBlockParts() {
        return blockParts;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void draw(int x, int y, int blockSize, SpriteBatch sb) {
    	sb.begin();

        for(BlockPart blockPart : blockParts)
        {
            int xPos = x + blockPart.getOffsetCol()*blockSize - blockSize / 2;
            int yPos = y - blockPart.getOffsetRow()*blockSize - blockSize / 2;
            sb.draw(colorTexture, xPos, yPos, blockSize, blockSize);
        }


        sb.end();
    }

    public void moveLeft() {
        col--;
    }

    public void moveRight() {
        col++;
    }

    public void moveUp() {
        row--;
    }

    public void moveDown() {
        row++;
    }

    public Texture getColorTexture() {
        return colorTexture;
    }
}
