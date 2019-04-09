package ubiquigame.games.tetris.gameObjects;

public class BlockPart {
    private int offsetRow;
    private int offsetCol;

    public BlockPart(int offsetRow, int offsetCol) {
        this.offsetRow = offsetRow;
        this.offsetCol = offsetCol;
    }

    public int getOffsetRow() {
        return offsetRow;
    }

    public int getOffsetCol() {
        return offsetCol;
    }
}
