package ubiquigame.games.EnchantedLabyrinth.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ubiquigame.games.EnchantedLabyrinth.TextureManager;
import java.util.*;

public class Labyrinth {

    private int rows;
    private int cols;
    private int renderStartX;
    private int renderStartY;
    private int tileSize;
    private int tileCodes[][];

    private List<Position> freePositions = new ArrayList<Position>();
    private List<Position> treasurePossiblePositions = new ArrayList<Position>();
    private Position treasurePosition;
    private Queue<Position> startPositions = new LinkedList<>();
    private SpriteBatch spriteBatch;
    private Texture wallVertical;
    private Texture wallHorizontal;

    private static final int TOP_WALL_BIT = 0;
    private static final int RIGHT_WALL_BIT = 1;
    private static final int BOTTOM_WALL_BIT = 2;
    private static final int LEFT_WALL_BIT = 3;

    //Oben, rechts, unten, links
    private static final int[] drow = new int[]{-1, 0, 1, 0};
    private static final int[] dcol = new int[]{0, 1, 0, -1};
    private static final int[][] passedWalls = new int[][]
    {
        new int[]{TOP_WALL_BIT, BOTTOM_WALL_BIT},
        new int[]{RIGHT_WALL_BIT, LEFT_WALL_BIT},
        new int[]{BOTTOM_WALL_BIT, TOP_WALL_BIT},
        new int[]{LEFT_WALL_BIT, RIGHT_WALL_BIT}
    };

    public Labyrinth(int screenWidth, int screenHeight, int rows, int cols, TextureManager tm) {

        int tileSizeFromWidth = screenWidth / cols;
        int tileSizeFromHeight = screenHeight / rows;
        tileSize = Math.min(tileSizeFromWidth, tileSizeFromHeight);

        int labyrinthWidth = tileSize * cols;
        int labyrinthHeight = tileSize * rows;
        this.renderStartX = (screenWidth - labyrinthWidth) / 2;
        this.renderStartY = screenHeight - (screenHeight - labyrinthHeight) / 2;
        this.rows = rows;
        this.cols = cols;
        generateRandomLabyrinth();

        spriteBatch = new SpriteBatch();
        wallVertical = tm.getTexture("wall_vertical");
        wallHorizontal = tm.getTexture("wall_horizontal");
    }

    public void generateFreePositions() {
        freePositions = new ArrayList<>();
        treasurePossiblePositions = new ArrayList<>();
        for(int i = 1; i < rows-1; i++) {
            for(int j = 1; j < cols-1; j++) {
                freePositions.add(new Position(i, j));
            }
        }
    }

    public Position getRandomFreePosition() {
        Random random = new Random();
        int randomIndex = random.nextInt(freePositions.size());
        Position result = freePositions.get(randomIndex);
        freePositions.remove(result);
        return result;
    }

    public void render() {
        spriteBatch.begin();
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                int tileCode = tileCodes[row][col];
                int tileX = renderStartX + col * tileSize;
                int tileY = renderStartY - row * tileSize;

                Vector2 topLeftPoint = new Vector2(tileX, tileY);
                Vector2 topRightPoint = new Vector2(tileX + tileSize, tileY);
                Vector2 bottomRightPoint = new Vector2(tileX + tileSize, tileY - tileSize);
                Vector2 bottomLeftPoint = new Vector2(tileX, tileY - tileSize);

                if(isWallPresent(tileCode, TOP_WALL_BIT)) {
                    spriteBatch.draw(wallHorizontal, topLeftPoint.x, topLeftPoint.y, tileSize, wallHorizontal.getHeight());
                }

                if(isWallPresent(tileCode, RIGHT_WALL_BIT)) {
                    spriteBatch.draw(wallVertical, bottomRightPoint.x, bottomRightPoint.y+5, wallVertical.getWidth(), tileSize);
                }

                if(isWallPresent(tileCode, BOTTOM_WALL_BIT)) {
                    spriteBatch.draw(wallHorizontal, bottomLeftPoint.x, bottomLeftPoint.y, tileSize, wallHorizontal.getHeight());
                }
                if(isWallPresent(tileCode, LEFT_WALL_BIT)) {
                    spriteBatch.draw(wallVertical, bottomLeftPoint.x, bottomLeftPoint.y+5, wallVertical.getWidth(), tileSize);
                }
            }
        }
        spriteBatch.end();
    }

    private void generateRandomLabyrinth() {
        int count = 0;
        while(true) {
            count++;
            tileCodes = new int[rows][cols];

            for (int row = 1; row < rows-1; row++) {
                tileCodes[row][1] = addWall(tileCodes[row][1], LEFT_WALL_BIT);
                tileCodes[row][cols - 2] = addWall(tileCodes[row][cols - 2], RIGHT_WALL_BIT);
            }

           for (int col = 1; col < cols-1; col++) {
                tileCodes[1][col] = addWall(tileCodes[1][col], TOP_WALL_BIT);
                tileCodes[rows - 2][col] = addWall(tileCodes[rows - 2][col], BOTTOM_WALL_BIT);
            }

            Random random = new Random();
            // generate top entrance
            int randomCol = 1 + random.nextInt(cols-2);
            tileCodes[1][randomCol] = removeWall(tileCodes[1][randomCol], TOP_WALL_BIT);

            // generate right entrance
            int randomRow = 1 + random.nextInt(rows-2);
            tileCodes[randomRow][cols-2] = removeWall(tileCodes[randomRow][cols-2], RIGHT_WALL_BIT);

            // generate bottom entrance
            randomCol = 1 + random.nextInt(cols-2);
            tileCodes[rows-2][randomCol] = removeWall(tileCodes[rows-2][randomCol], BOTTOM_WALL_BIT);

            // generate left entrance
            randomRow = 1 + random.nextInt(rows-2);
            tileCodes[randomRow][1] = removeWall(tileCodes[randomRow][1], LEFT_WALL_BIT);

            generateFreePositions();
            generateRandomLabyrinthRecursive(1, 1, rows-2, cols-2);
            findTreasurePosition();
            if(treasurePosition != null) {
                break;
            }
        }

        startPositions.add(new Position(0, 0));
        startPositions.add(new Position(0, cols-1));
        startPositions.add(new Position(rows-1, cols-1));
        startPositions.add(new Position(rows-1, 0));
    }

    private void findTreasurePosition() {
        for(Position treasurePosition : treasurePossiblePositions) {
            int row = treasurePosition.getRow();
            int col = treasurePosition.getCol();
            int numWalls = 0;
            for(int i = 0; i < 4; i++) {
                if(isWallPresent(tileCodes[row][col], i)) {
                    numWalls++;
                }
            }

            if(numWalls == 3) {
                this.treasurePosition = treasurePosition;

                Position toDelete = null;
                for(Position freePosition : freePositions) {
                    if(freePosition.equals(this.treasurePosition)) {
                        toDelete = freePosition;
                        break;
                    }
                }

                if(toDelete != null) {
                    freePositions.remove(toDelete);
                }

                tileCodes[row][col] = 15;

                break;
            }
        }
    }

    private class Wall {

        public Wall(int wallToRemoveBit) {
            this.wallToRemoveBit = wallToRemoveBit;
            rows = new ArrayList<Integer>();
            cols = new ArrayList<Integer>();
        }

        public int wallToRemoveBit;
        public List<Integer> rows;
        public List<Integer> cols;
    }

    private void generateRandomLabyrinthRecursive(int startRow, int startCol, int height, int width) {
        int endRow = startRow+height;
        int endCol = startCol+width;

        if(width == 1 && height == 1) {
            treasurePossiblePositions.add(new Position(startRow, startCol));
        }

        if(width <= 1 || height <= 1) {
            return;
        }

        Random random = new Random();
        int splitRow = startRow + random.nextInt(height-1);
        int splitCol = startCol + random.nextInt(width-1);

        for(int i = startRow; i < endRow; i++) {
            tileCodes[i][splitCol] = addWall(tileCodes[i][splitCol], RIGHT_WALL_BIT);
        }

        for(int i = startCol; i < endCol; i++) {
            tileCodes[splitRow][i] = addWall(tileCodes[splitRow][i], BOTTOM_WALL_BIT);
        }

        List<Wall> fourWalls = new ArrayList<Wall>();
        // Obige Wand
        Wall topWall = new Wall(RIGHT_WALL_BIT);
        for(int row = startRow; row < splitRow+1; row++) {
            topWall.rows.add(row);
            topWall.cols.add(splitCol);
        }
        fourWalls.add(topWall);
        // Untere Wand
        Wall bottomWall = new Wall(RIGHT_WALL_BIT);
        for(int row = splitRow+1; row < endRow; row++) {
            bottomWall.rows.add(row);
            bottomWall.cols.add(splitCol);
        }
        fourWalls.add(bottomWall);

        // Left wall
        Wall leftWall = new Wall(BOTTOM_WALL_BIT);
        for(int col = startCol; col < splitCol+1; col++) {
            leftWall.rows.add(splitRow);
            leftWall.cols.add(col);
        }
        fourWalls.add(leftWall);

        // Right wall
        Wall rightWall = new Wall(BOTTOM_WALL_BIT);
        for(int col = splitCol+1; col < endCol; col++) {
            rightWall.rows.add(splitRow);
            rightWall.cols.add(col);
        }
        fourWalls.add(rightWall);

        int chosenWall = random.nextInt(4);
        for(int i = 0; i < fourWalls.size(); i++) {
            Wall wall = fourWalls.get(i);

            if(i == chosenWall)
                continue;

            int randomIndex = random.nextInt(wall.rows.size());
            int wallRow = wall.rows.get(randomIndex);
            int wallCol = wall.cols.get(randomIndex);
            tileCodes[wallRow][wallCol] = removeWall(tileCodes[wallRow][wallCol], wall.wallToRemoveBit);
        }

        generateRandomLabyrinthRecursive(startRow, startCol,
                splitRow-startRow+1, splitCol-startCol+1);


        generateRandomLabyrinthRecursive(startRow, splitCol+1,
                splitRow-startRow+1, endCol-splitCol-1);

        generateRandomLabyrinthRecursive(splitRow+1, splitCol+1,
                endRow-splitRow-1, endCol-splitCol-1);

        generateRandomLabyrinthRecursive(splitRow+1, startCol,
                endRow-splitRow-1, splitCol-startCol+1);

    }

    public boolean isPassable(int rowSrc, int colSrc, int rowDest, int colDest) {
        if(rowDest < 0 || rowDest >= rows || colDest < 0 || colDest >= cols)
            return false;

        int offsetRow = rowDest - rowSrc;
        int offsetCol = colDest - colSrc;
        int direction = -1;
        for(int i = 0; i < 4; i++) {
            if(drow[i] == offsetRow && dcol[i] == offsetCol) {
                direction = i;
                break;
            }
        }
        if(direction == -1)
            return false;

        return !isWallPresent(tileCodes[rowSrc][colSrc], passedWalls[direction][0])
                && !isWallPresent(tileCodes[rowDest][colDest], passedWalls[direction][1]);
    }

    public Position getTreasurePosition() {
        return treasurePosition;
    }

    private int addWall(int tileCode, int wallBit) {
        return tileCode | (1 << wallBit);
    }

    private int removeWall(int tileCode, int wallBit) {
        return tileCode & (~(1 << wallBit));
    }

    private boolean isWallPresent(int tileCode, int wallBit) { return ((tileCode >> wallBit) & 1) == 1; }

    public int getTileSize() {return tileSize;}

    public Position getStartPosition() {
        return startPositions.remove();
    }

    public int getRenderX(int col) {
        return renderStartX + col* tileSize;
    }

    public int getRenderY(int row) {
        return renderStartY - row* tileSize - tileSize;
    }

    public void breakWalls(Position position) { tileCodes[position.getRow()][position.getCol()] = 0; }

    public int getRows() { return rows; }

    public int getCols() { return cols; }
}
