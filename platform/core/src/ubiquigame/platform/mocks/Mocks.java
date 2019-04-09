package ubiquigame.platform.mocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ubiquigame.common.Player;
import ubiquigame.common.UbiquiGame;
import ubiquigame.platform.PlatformImpl;
import ubiquigame.platform.session.GameSession;
import ubiquigame.platform.session.player.PlayerInternal;
import ubiquigame.platform.session.tournament.TournamentSession;

public class Mocks {

	private static List<UbiquiGame> games = null;
	
	public static List<UbiquiGame> getDummyGames() {
		if(games != null) {
			return games;
		}
		List<UbiquiGame> arrayList = new ArrayList<>();
		
		arrayList.add(new DummyGame(PlatformImpl.getInstance()));
		arrayList.add(new DummyGame(PlatformImpl.getInstance()));
		arrayList.add(new DummyGame(PlatformImpl.getInstance()));
		arrayList.add(new DummyGame(PlatformImpl.getInstance()));
		arrayList.add(new DummyGame(PlatformImpl.getInstance()));
		arrayList.add(new DummyGame(PlatformImpl.getInstance()));
		arrayList.add(new DummyGame(PlatformImpl.getInstance()));
		
		games= arrayList;
		return games;
	}

	public static GameSession getDummyTournamentSession() {
		return new TournamentSession();
	}

	public static List<Player> getPlayers() {
		Player p1 = new PlayerInternal("Alice", null, null);
		Player p2 = new PlayerInternal("Bob", null, null);
		Player p3 = new PlayerInternal("Carol", null, null);
		Player p4 = new PlayerInternal("Dennis", null, null);
		return Arrays.asList(p1, p2, p3, p4);
	}

}
