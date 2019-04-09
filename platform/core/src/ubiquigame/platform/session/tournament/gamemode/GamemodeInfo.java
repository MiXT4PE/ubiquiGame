package ubiquigame.platform.session.tournament.gamemode;

import ubiquigame.platform.PlatformImpl;

import static ubiquigame.platform.menu.AbstractMenuScreen.localize;

public abstract class GamemodeInfo {

	public static final GamemodeInfo ALL_GAMES = new GamemodeInfo() {
		@Override
		public String getName() {
			return String.format(localize("playAllGames"),
					PlatformImpl.getInstance().getGameManager().getGames().size());
		}

		@Override
		public String getDescription() {
			return localize("descAllGames");
		}

	};

	public static final GamemodeInfo RANDOM = new GamemodeInfo() {
		@Override
		public String getName() {
			return localize("randomGames");
		}

		@Override
		public String getDescription() {
			return localize("descRandomGames");
		}

	};

	public static final GamemodeInfo CUSTOM = new GamemodeInfo() {
		@Override
		public String getName() {
			return "Customized";
		}

		@Override
		public String getDescription() {
			return "Choose what games you want to play, how many rounds, and so on. The most customizable game mode for the biggest hipster!";
		}

	};

	public abstract String getName();

	public abstract String getDescription();

}
