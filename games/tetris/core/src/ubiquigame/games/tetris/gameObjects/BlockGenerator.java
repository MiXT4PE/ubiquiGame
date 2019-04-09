package ubiquigame.games.tetris.gameObjects;

import ubiquigame.games.tetris.TextureManager;

import java.util.Random;

public class BlockGenerator {

    private TextureManager tm;

    private BlockGenerator(){}

    public static BlockGenerator getInstance() {
        return instance;
    }

    public void setTextureManager(TextureManager tm) {
        this.tm = tm;
    }

    public Block createBlockLeftZ(int row, int col) {
        BlockPart[] blockParts = new BlockPart[]
        {
                new BlockPart(0, -1),
                new BlockPart(0, 0),
                new BlockPart(1, 0),
                new BlockPart(1, 1)
        };

        return new Block(blockParts, row, col, tm.getTexture("pixel_red"), true);
    }

    public Block createBlockLeftL(int row, int col) {
        BlockPart[] blockParts = new BlockPart[]
        {
                new BlockPart(0, 1),
                new BlockPart(1, 1),
                new BlockPart(1, 0),
                new BlockPart(1, -1)
        };

        return new Block(blockParts, row, col, tm.getTexture("pixel_orange"), true);
    }

    public Block createBlockO(int row, int col) {
        BlockPart[] blockParts = new BlockPart[]
        {
                new BlockPart(0, 0),
                new BlockPart(0, 1),
                new BlockPart(1, 1),
                new BlockPart(1, 0)
        };

        return new Block(blockParts, row, col, tm.getTexture("pixel_yellow"), false);
    }

    public Block createBlockRightZ(int row, int col) {
        BlockPart[] blockParts = new BlockPart[]
        {
                new BlockPart(0, 1),
                new BlockPart(0, 0),
                new BlockPart(1, 0),
                new BlockPart(1, -1)
        };

        return new Block(blockParts, row, col, tm.getTexture("pixel_green"), true);
    }

    public Block createBlockI(int row, int col) {
        BlockPart[] blockParts = new BlockPart[]
        {
                new BlockPart(0, -1),
                new BlockPart(0, 0),
                new BlockPart(0, 1),
                new BlockPart(0, 2)
        };

        return new Block(blockParts, row, col, tm.getTexture("pixel_aqua"), true);
    }

    public Block createBlockRightL(int row, int col) {
        BlockPart[] blockParts = new BlockPart[]
        {
                new BlockPart(0, -1),
                new BlockPart(1, -1),
                new BlockPart(1, 0),
                new BlockPart(1, 1)
        };

        return new Block(blockParts, row, col, tm.getTexture("pixel_blue"), true);
    }

    public Block createBlockT(int row, int col) {
        BlockPart[] blockParts = new BlockPart[]
        {
                new BlockPart(0, -1),
                new BlockPart(0, 0),
                new BlockPart(0, 1),
                new BlockPart(1, 0)
        };

        return new Block(blockParts, row, col, tm.getTexture("pixel_purple"), true);
    }

    public Block createRandomBlock(int row, int col) {
        Random random = new Random();
        int choice = random.nextInt(7);

        if(choice == 0) {
            return createBlockLeftZ(row, col);
        }
        else if(choice == 1) {
            return createBlockLeftL(row, col);
        }
        else if(choice == 2) {
            return createBlockO(row, col);
        }
        else if(choice == 3) {
            return createBlockRightZ(row, col);
        }
        else if(choice == 4) {
            return createBlockI(row, col);
        }
        else if(choice == 5) {
            return createBlockRightL(row, col);
        }

        return createBlockT(row, col);
    }

    private static BlockGenerator instance = new BlockGenerator();
}
