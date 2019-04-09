package ubiquigame.common;

/**
 * Identifiers for the different controller faces, shared between game and
 * controller app
 */
public enum ControllerFace {
	Default("default"),
	Controller_Cross_AB("controller_cross_ab"), // Enchanted Labyrinth
    Controller_Cross_A("controller_cross_a"),   // Memory, Space Shooter, Tetris
    Controller_Cross("controller_cross"),       // Square Run
    Controller_Racer("controller_racer"),       // Racer
    Controller_Bomb("controller_bomb");         // Ticking Bombs


	private String activityName;

	ControllerFace(String activityName) {
		this.activityName = activityName;
	}

	public String getFaceName() {
		return activityName;
	}
}
