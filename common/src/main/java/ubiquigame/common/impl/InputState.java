package ubiquigame.common.impl;

import java.io.Serializable;

/**
 * InputState defines every Button from all Games
 *
 * Booleans and in which games they are used:
 * "next":         is the next button for the game Ticking Bombs
 * "left":         is the left button on a dPad, used in Memory, Space Shooter, Tetris, Enchanted Labyrinth, Square Run
 * "right":        is the right button on a dPad, used in Memory, Space Shooter, Tetris, Enchanted Labyrinth, Square Run
 * "up":           is the up button on a dPad, used in Memory, Space Shooter, Tetris, Enchanted Labyrinth, Square Run
 * "down":         is the down button on a dPad, used in Memory, Space Shooter, Tetris, Enchanted Labyrinth, Square Run
 * "a":            is a standard A button, used in Memory, Space Shooter, Tetris, Enchanted Labyrinth
 * "b":            is a standard A button, used in Enchanted Labyrinth
 * "boost":        is the boost button for the game Racer
 * "accelerator":  is the accelerate button for the game Racer
 */
public class InputState implements Serializable {

	private static final long serialVersionUID = -4593143796161194425L;
	public static InputState s = new InputState();
	private boolean next, left, right, up, down, a, b, boost, accelerator;

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean flip) {
        this.next = flip;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isA() {
        return a;
    }

    public void setA(boolean a) {
        this.a = a;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public boolean isBoost() {
        return boost;
    }

    public void setBoost(boolean boost) {
        this.boost = boost;
    }

    public boolean isAccelerator() {
        return accelerator;
    }

    public void setAccelerator(boolean accelerator) {
        this.accelerator = accelerator;
    }
}
