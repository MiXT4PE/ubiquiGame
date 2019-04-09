package ubiquigame.common.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import ubiquigame.common.Player;

public class GameOverMessage {

	private final List<Player> ranking;
	private final Map<Player, Integer> score;

	public GameOverMessage(List<Player> ranking, Map<Player, Integer> score) {
		this.ranking = Collections.unmodifiableList(ranking);
		this.score = Collections.unmodifiableMap(score);
	}

	public final List<Player> getPlayerRanking() {
		return ranking;
	}

	public final int getScore(Player player) {
		return score.get(player);
	}

}
