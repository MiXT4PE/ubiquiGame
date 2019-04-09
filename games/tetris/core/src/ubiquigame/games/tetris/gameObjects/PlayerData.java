package ubiquigame.games.tetris.gameObjects;

import ubiquigame.common.Controller;
import ubiquigame.common.Player;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PlayerData {
    private Player player;
    private GameField gameField;
    private Block block;
    private List<Block[]> stack = new ArrayList<Block[]>();

    private float NORMAL_SPEED = 0.2f;
    private float FAST_SPEED = 0.1f;
    private float CONTROL_TIME_OFFSET = 0.175f;
    private float elapsedTime = 0.0f;
    private float down_every = NORMAL_SPEED;
    private boolean isGameOver = false;
    private float gameOverAfter = 0.0f;

    private float leftTime = CONTROL_TIME_OFFSET;
    private float rightTime = CONTROL_TIME_OFFSET;
    private float rotationTime = CONTROL_TIME_OFFSET;

    public Player getPlayer() {
        return player;
    }

    public GameField getGameField() {
        return gameField;
    }

    public PlayerData(Player player) {
        this.player = player;
        gameField = new GameField(12, 30);
        block = BlockGenerator.getInstance().createRandomBlock(0, gameField.getWidth() / 2);
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public float getGameOverAfter() {
        return gameOverAfter;
    }

    public void renderPlayerData(ShapeRenderer sr, SpriteBatch sb) {
        gameField.draw(sr, sb);
        if(block != null) {
            block.draw(gameField.getCenterX(block.getCol()), gameField.getCenterY(block.getRow()), gameField.getBlockSize(), sb);
        }
    }

    public void updatePlayerData(float deltaTime) {
        if(isGameOver) {
            return;
        }
        elapsedTime += deltaTime;
        if(elapsedTime > down_every) {
            elapsedTime -= down_every;
            block.moveDown();
            if (gameField.collidesWithBottomBorder(block) || gameField.collidesWithBlocks(block)) {
                block.moveUp();
                List<Block[]> deletedRows = gameField.saveBlock(block);
                for(Block[] row : deletedRows) {
                    stack.add(row);
                }
                block = BlockGenerator.getInstance().createRandomBlock(0, gameField.getWidth() / 2);
                if(gameField.collidesWithBlocks(block)) {
                    isGameOver = true;
                    block = null;
                }
                down_every = NORMAL_SPEED;
            }
        }

        gameOverAfter += deltaTime;

        leftTime += deltaTime;
        rightTime += deltaTime;
        rotationTime += deltaTime;
    }

    public void handlePlayerInput() {
        if(isGameOver) {
            return;
        }
        Controller controller = player.getController();

        if(controller.getInput().isLeft()) {
            if (leftTime >= CONTROL_TIME_OFFSET) {
                leftTime = 0.0f;
                block.moveLeft();
                if (gameField.collidesWithLeftRightBorder(block) || gameField.collidesWithBlocks(block)) {
                    block.moveRight();
                }
            }
        }
        else {
            leftTime = CONTROL_TIME_OFFSET;
        }


        if(controller.getInput().isRight()) {
            if(rightTime >= CONTROL_TIME_OFFSET) {
                rightTime = 0.0f;
                block.moveRight();
                if (gameField.collidesWithLeftRightBorder(block) || gameField.collidesWithBlocks(block)) {
                    block.moveLeft();
                }
            }
        }
        else {
            rightTime = CONTROL_TIME_OFFSET;
        }

        if(controller.getInput().isA()) {
            if(rotationTime >= CONTROL_TIME_OFFSET) {
                rotationTime = 0.0f;
                block.rotateBy90();
                if(gameField.collidesWithLeftRightBorder(block) || gameField.collidesWithBlocks(block)) {
                    block.rotateBackBy90();
                }
            }
        }
        else {
            rotationTime = CONTROL_TIME_OFFSET;
        }


        if(controller.getInput().isDown()) {
            down_every = FAST_SPEED;
        }
    }

    public List<Block[]> getStack() {
        return stack;
    }
}
