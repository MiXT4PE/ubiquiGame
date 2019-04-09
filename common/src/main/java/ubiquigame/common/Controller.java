package ubiquigame.common;

import ubiquigame.common.impl.InputState;

/**
 * Proxy for the smartphone controller app. Used as interface to query the player's
 * inputs through polling button states
 */
public interface Controller {
	/**
	 * Check if a specific button of the current controller face is pressed,
	 * if the buttonName is invalid it returns false.
	 *
	 * @param buttonName a String identifier defined in current button face
	 * @return true if button is pressed
	 */
	default InputState getInput() {
		return null;
	}

	/**
	 * Returns the last InputState, before the current
	 * @return
	 */
	default InputState getLastInput() {
		return new InputState();
	}

	/**
	 * Tells the smartphone controller to give the player physical feedback
	 * @param feedback type of feedback 
	 * @param params additonal params for feedback e.g. duration, intensity
	 */
	void feedback(ControllerFeedbacks feedback, FeedbackParams params);

}
