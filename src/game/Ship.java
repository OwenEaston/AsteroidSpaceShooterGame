package game;

import static game.Constants.DT;
import utilities.Vector2D;
import utilities.SoundManager;

import java.awt.*;

import static utilities.SoundManager.thrust;

public abstract class Ship extends GameObject {
    public Bullet bullet;
    protected Controller ctrl;
    public Vector2D direction;
    public boolean thrusting;
    public Color color;

    public int XP[] = { -6, 0, 6, 0 }, YP[] = { 8, 4, 8, -8 };
    public int XPTHRUST[] = { -5, 0, 5, 0 }, YPTHRUST[] = { 7, 3, 7, -7 }; public static final int RADIUS = 8;

    public static final double STEER_RATE = 2 * Math.PI;

    public static final double MAG_ACC = 200;

    public static final double DRAG = 0.01;

    public static final double DRAWING_SCALE = 1.5;

    public static final int MUZZLE_VELOCITY = 100;
    private long timeLastShot;
    public static final long SHOT_DELAY=150;

    public Ship(Vector2D position, Vector2D velocity, double radius) {
        super(position, velocity, radius);
    }

    protected void mkBullet() {
        Vector2D bulletPos = new Vector2D(position);
        Vector2D bulletVel = new Vector2D(velocity);
        bulletVel.addScaled(direction, PlayerShip.MUZZLE_VELOCITY);
        bullet = new Bullet(bulletPos, bulletVel, this instanceof PlayerShip);
        bullet.position.addScaled(direction, (radius + bullet.radius) * 2);
        SoundManager.fire();
    }

    @Override
    public void update() {
        Action action = ctrl.action();
        if (action.shoot) {

                long time = System.currentTimeMillis();
                if (time-timeLastShot>SHOT_DELAY) {

                    mkBullet();
                    //System.out.println("made bullet");
                    action.shoot = false;
                    timeLastShot = time;
                    SoundManager.fire();
                }
        }
        thrusting = action.thrust != 0;
        if (thrusting) SoundManager.play(thrust);
        direction.rotate(action.turn * PlayerShip.STEER_RATE * DT);
        velocity = new Vector2D(direction).mult(velocity.mag());
        velocity.addScaled(direction, PlayerShip.MAG_ACC * DT * action.thrust);
        velocity.addScaled(velocity, -PlayerShip.DRAG);
        super.update();
    }

    public boolean canHit(GameObject other) {
        return (other instanceof Asteroid  || other instanceof PlayerShip);
    }

}
