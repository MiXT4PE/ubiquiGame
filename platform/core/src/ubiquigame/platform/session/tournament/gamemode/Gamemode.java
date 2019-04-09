package ubiquigame.platform.session.tournament.gamemode;

public enum Gamemode {
	ALLGAMES(GamemodeInfo.ALL_GAMES), RANDOM(GamemodeInfo.RANDOM), CUSTOM(GamemodeInfo.CUSTOM);

	private GamemodeInfo info;

	Gamemode(GamemodeInfo info) {
		this.info = info;
	}

	public GamemodeInfo getInfo() {
		return info;
	}

}
