package ubiquigame.games.EnchantedLabyrinth.gameObjects;

public class Position {
    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Position clone() {
        return new Position(row, col);
    }

    public void toLeft() {
        col--;
    }

    public void toRight() {
        col++;
    }

    public void toUp() {
        row--;
    }

    public void toDown() {
        row++;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Position))
            return false;

        Position position = (Position)obj;
        return row == position.getRow() && col == position.getCol();
    }

}
