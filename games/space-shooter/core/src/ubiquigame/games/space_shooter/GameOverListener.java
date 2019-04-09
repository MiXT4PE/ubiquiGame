package ubiquigame.games.space_shooter;

import ubiquigame.common.Player;

import java.util.Stack;

public interface GameOverListener {
    void onGameOver(Stack<Player> ranking);
}
