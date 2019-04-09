package ubiquigame.platform.session.tournament;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ubiquigame.common.Player;
import ubiquigame.common.impl.GameOverMessage;

/**
 * Holds the Score for Players of current session
 */
public class Score {

	private Map<Player, Integer> scoreMap;
	private List<Player> players;

	public Score(Player[] players) {
		this.players = Arrays.asList(players);
		scoreMap = new HashMap<>();
		this.players.forEach(p -> scoreMap.put(p, 0));
	}

	public void addTo(Player p, int amount) {
		if (!scoreMap.containsKey(p))
			return;
		int old = scoreMap.get(p);
		scoreMap.put(p, old + amount);
	}

	public void set(Player p, int amount) {
		if (!scoreMap.containsKey(p))
			return;
		scoreMap.put(p, amount);
	}

	public List<Player> getPlayersRanked() {
		Comparator<Player> scoreComparator = Comparator.comparingInt(a -> this.get(a));

		Collections.sort(players, scoreComparator.reversed());
		return players;
	}
	
	public GameOverMessage toGameOverMessage() {
		return new GameOverMessage(new ArrayList<>(getPlayersRanked()), new HashMap<>(scoreMap));
		
	}

	public int get(Player p) {
		if (!scoreMap.containsKey(p))
			return 0;
		return scoreMap.get(p);

	}

}
