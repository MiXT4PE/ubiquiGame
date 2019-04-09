package ubiquigame.games.space_shooter.game_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import ubiquigame.common.Player;
import ubiquigame.common.constants.PlayerIdentifier;
import ubiquigame.common.impl.InputState;
import ubiquigame.games.space_shooter.Level;

import java.util.ArrayList;

public class Ship extends SpaceObject {

	public static final int MAX_ENERGY = 10;

	private final Player player;

	private float[] shipBounds;
	private Polygon ship;

	private Color color;

	private ArrayList<Bullet> bullets;
	private static final int MAX_BULLETS = 10;

	private boolean left;
	private boolean right;
	private boolean up;

	private float maxSpeed;
	private float acceleration;
	private float deceleration;

	private boolean dead;
	private boolean hitObstacle;
	private boolean hitBullet;
	private int energy = MAX_ENERGY;

	// for checking collision and adding to list
	private Level level;

	public Ship(Level level, Player player, float facingDir, int playerIndex, int x, int y) {
		this.player = player;
		this.level = level;

		java.awt.Color awtColor = PlayerIdentifier.getColorForIndex(playerIndex + 1);
		this.color = new Color(awtColor.getRed()/255f, awtColor.getGreen()/255f, awtColor.getBlue()/255f, awtColor.getAlpha()/255f);

		this.bullets = new ArrayList<>();

		this.x = x;
		this.y = y;

		maxSpeed = 200;
		acceleration = 150;
		deceleration = 50;

		shapex = new float[4];
		shapey = new float[4];

		radians = facingDir;
		rotationSpeed = 3;

		setShape();
	}

	// shapex and shapey or obstacleBounds will contain points which will be
	// connected to a polygon in render
	private void setShape() {
		shapex[0] = x + MathUtils.cos(radians) * 8;
		shapey[0] = y + MathUtils.sin(radians) * 8;

		shapex[1] = x + MathUtils.cos(radians - 4 * MathUtils.PI / 5) * 15;
		shapey[1] = y + MathUtils.sin(radians - 4 * MathUtils.PI / 5) * 15;

		shapex[2] = x + MathUtils.cos(radians + MathUtils.PI) * 13;
		shapey[2] = y + MathUtils.sin(radians + MathUtils.PI) * 13;

		shapex[3] = x + MathUtils.cos(radians + 4 * MathUtils.PI / 5) * 15;
		shapey[3] = y + MathUtils.sin(radians + 4 * MathUtils.PI / 5) * 15;

		shipBounds = new float[] { shapex[0], shapey[0], shapex[1], shapey[1], shapex[2], shapey[2], shapex[3],
				shapey[3] };
		ship = new Polygon(shipBounds);
	}

//    public void setLeft(boolean b) {
//        left = b;
//    }
//    public void setRight(boolean b) {
//        right = b;
//    }
//    public void setUp(boolean b) {
//        up = b;
//    }

	public void shoot() {
		if (bullets.size() == MAX_BULLETS)
			return;
		bullets.add(new Bullet(level, x, y, radians, color));
	}

	// bullets make more damage than obstacles
	public void setHit(SpaceObject spaceObject) {
		if (spaceObject instanceof Bullet)
			hitBullet = true;
		if (spaceObject instanceof Obstacle)
			hitObstacle = true;
	}

	public boolean getDead() {
		return dead;
	}

	public void handleInput() {
		InputState input = player.getController().getInput();
		left = input.isLeft();
		right = input.isRight();
		up = input.isUp();
		if (input.isA() && !player.getController().getLastInput().isA())
			shoot();
	}

	public void update(float dt) {

		// for debug purposes
//        dt = 1f/60f;

		handleInput();

		// turning
		turn(rotationSpeed * dt);
//        if(left) {
//            radians += rotationSpeed*dt;
//        } else if(right) {
//            radians -= rotationSpeed*dt;
//        }

		// acceleration
		if (up) {
			dx += MathUtils.cos(radians) * acceleration * dt;
			dy += MathUtils.sin(radians) * acceleration * dt;
		}

		// deceleration
		float vec = (float) Math.sqrt(dx * dx + dy * dy);
		if (vec > 0) {
			dx -= (dx / vec) * deceleration * dt;
			dy -= (dy / vec) * deceleration * dt;
		}
		// cap to max speed
		if (vec > maxSpeed) {
			dx = (dx / vec) * maxSpeed;
			dy = (dy / vec) * maxSpeed;
		}

		// set position
		move(dx * dt, dy * dt);
//        x += dx*dt;
//        y += dy*dt;

		// set shape
		setShape();

		// screen wrap
		wrap();

		// update bullet of ship
		for (int i = bullets.size() - 1; i >= 0; i--) {
			bullets.get(i).update(dt);
			if (bullets.get(i).shouldRemove())
				bullets.remove(i);
		}

		// got hit by bullet
		if (hitBullet) {
			hitBullet = false;
			energy -= 2;
			if (energy <= 0)
				dead = true;
		}

		// got hit by obstacle
		if (hitObstacle) {
			hitObstacle = false;
			energy--;
			if (energy <= 0)
				dead = true;
		}
	}

	public void render(ShapeRenderer sr) {

        Gdx.gl20.glLineWidth(3);
		sr.setColor(color);

		// draw ship
		sr.begin(ShapeRenderer.ShapeType.Line);
		sr.polygon(shipBounds);
		sr.end();

		// draw Bullet
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(sr);
		}

        Gdx.gl20.glLineWidth(1);
	}

	// if ship will go offscreen it will come from the opposite side
	public void wrap() {
		if (x < 0)
			x = 1920; //SpaceShooter.getInstance().width;
		if (x > 1920 /*SpaceShooter.getInstance().width*/)
			x = 0;
		if (y < 0)
			y = 1080; //SpaceShooter.getInstance().height;
		if (y > 1080 /*SpaceShooter.getInstance().height*/)
			y = 0;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public Polygon getBounds() {
		setShape();
		return ship;
	}

	public Player getPlayer() {
		return player;
	}

	public int getEnergy() {
		return energy;
	}

	// before a ship turns check if there is the wall
	private void turn(float turnVal) {

		// save old position
		float radiansOld = this.radians;

		if (left) {
			this.radians += turnVal;
		} else if (right) {
			this.radians -= turnVal;
		}
		if (level.checkCollision(this) instanceof Wall)
			this.radians = radiansOld;
	}

	// checks if the ship can move the full lenght. It will be trimmed to allowed
	// length
	private void move(float xDir, float yDir) {

		SpaceObject obj = null;

		// save old position
		float xPrev = this.x;
		float yPrev = this.y;

		// cut decimal and store for next time
		xDir += this.cachedX;
		int xDirTransformed = MathUtils.floor(xDir);
		this.cachedX = xDir - xDirTransformed;

		yDir += this.cachedY;
		int yDirTransformed = MathUtils.floor(yDir);
		this.cachedY = yDir - yDirTransformed;

		// x direction
		int i = 0;
		for (; i < Math.abs(xDirTransformed); i++) {
			this.x = xPrev + Math.signum(xDir) * i;
			obj = level.checkCollision(this);
			// collision with wall
			if (obj instanceof Wall) {
				i -= 1;
				break;
			}
		}
		this.x = xPrev + Math.signum(xDir) * i;
		// rebound
		if (obj instanceof Wall) {
			if (!((Wall) obj).isHorizontal()) {
				// bounce off vertical wall by mirroring the x direction
				if (Math.abs(this.dx) < Wall.MIN_BOUNCE) {
					this.dx = -1 * Math.signum(this.dx) * Wall.MIN_BOUNCE;
				} else {
					this.dx = -1 * this.dx;
				}
			}
		}

		// y direction
		int j = 0;
		for (; j < Math.abs(yDirTransformed); j++) {
			this.y = yPrev + Math.signum(yDir) * j;
			obj = level.checkCollision(this);
			if (obj instanceof Wall) {
				j -= 1;
				break;
			}
		}
		this.y = yPrev + Math.signum(yDir) * j;
		// rebound
		if (obj instanceof Wall) {
			if (((Wall) obj).isHorizontal()) {
				// bounce of vertical walls by mirroring the y direction
				if (Math.abs(this.dy) < Wall.MIN_BOUNCE) {
					this.dy = -1 * Math.signum(this.dy) * Wall.MIN_BOUNCE;
				} else {
					this.dy = -1 * this.dy;
				}
			}
		}
		
		
	}

}
