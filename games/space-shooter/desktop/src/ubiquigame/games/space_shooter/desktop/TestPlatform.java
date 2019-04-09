package ubiquigame.games.space_shooter.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import ubiquigame.common.*;
import ubiquigame.common.impl.GameOverMessage;
import ubiquigame.common.impl.InputState;
import ubiquigame.common.impl.ScreenDimension;
import ubiquigame.games.space_shooter.SpaceShooter;

public class TestPlatform extends Game implements UbiquiGamePlatform {

	private GameRunnerScreen gameRunnerScreen;
	private Player[] players;

	public TestPlatform() {
		Player p1 = new Player() {
			@Override
			public String getName() {
				return "Hallo";
			}

			@Override
			public Controller getController() {
				return new Controller() {
					InputState input = new InputState();

					@Override
					public InputState getInput() {
						input.setRight(Gdx.input.isKeyPressed(Input.Keys.RIGHT));
						input.setLeft(Gdx.input.isKeyPressed(Input.Keys.LEFT));
						input.setUp(Gdx.input.isKeyPressed(Input.Keys.UP));
						input.setA(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_0));
						return input;
					}

					@Override
					public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {}
				};
			}

			@Override
			public Image getAvatar() {
				return null;
			}
		};
		Player p2 = new Player() {
			@Override
			public String getName() {
				return "HalloHallo";
			}

			@Override
			public Controller getController() {
				return new Controller() {
					InputState input = new InputState();

					@Override
					public InputState getInput() {
						input.setRight(Gdx.input.isKeyPressed(Input.Keys.D));
						input.setLeft(Gdx.input.isKeyPressed(Input.Keys.A));
						input.setUp(Gdx.input.isKeyPressed(Input.Keys.W));
						input.setA(Gdx.input.isKeyJustPressed(Input.Keys.SPACE));
						return input;
					}

					@Override
					public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {}
				};
			}

			@Override
			public Image getAvatar() {
				return null;
			}
		};
        Player p3 = new Player() {
            @Override
            public String getName() {
                return "Hi";
            }

            @Override
            public Controller getController() {
                return new Controller() {
                    InputState input = new InputState();

                    @Override
                    public InputState getInput() {
                        input.setRight(Gdx.input.isKeyPressed(Input.Keys.L));
                        input.setLeft(Gdx.input.isKeyPressed(Input.Keys.J));
                        input.setUp(Gdx.input.isKeyPressed(Input.Keys.I));
                        input.setA(Gdx.input.isKeyJustPressed(Input.Keys.K));
                        return input;
                    }

                    @Override
                    public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {}
                };
            }

            @Override
            public Image getAvatar() {
                return null;
            }
        };
		Player p4 = new Player() {
			@Override
			public String getName() {
				return "mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmh";
			}

			@Override
			public Controller getController() {
				return new Controller() {
					InputState input = new InputState();

					@Override
					public InputState getInput() {
						input.setRight(Gdx.input.isKeyPressed(Input.Keys.L));
						input.setLeft(Gdx.input.isKeyPressed(Input.Keys.J));
						input.setUp(Gdx.input.isKeyPressed(Input.Keys.I));
						input.setA(Gdx.input.isKeyJustPressed(Input.Keys.K));
						return input;
					}

					@Override
					public void feedback(ControllerFeedbacks feedback, FeedbackParams params) {}
				};
			}

			@Override
			public Image getAvatar() {
				return null;
			}
		};

		players = new Player[] { p1, p2, p3, p4 };
	}

	@Override
	public void create() {
		gameRunnerScreen.create();
		setScreen(gameRunnerScreen);
		gameRunnerScreen.start();
		
	}

	@Override
	public void render() {
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			System.exit(0);
		}
		if (screen != null)
			screen.render(Gdx.graphics.getDeltaTime());
	}

	@Override
	public Player[] getPlayers() {
		return players;
	}

	@Override
	public void notifyCurrGameOver(GameOverMessage state) {
		gameRunnerScreen.stop();
		setGame(new SpaceShooter(this));
	}

	@Override
	public ScreenDimension getScreenDimension() {
		return new ScreenDimension(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public float getMusicVolume() {
		return 0;
	}

	@Override
	public float getSoundVolume() {
		return 0;
	}

	public void setGame(UbiquiGame game) {
		gameRunnerScreen = new GameRunnerScreen(game);
		

	}
}
