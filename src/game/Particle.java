package game;

import utilities.Vector2D;

import java.awt.*;

import static game.Constants.DT;

public class Particle extends GameObject {

    public static final int PARTICLE_SPEED = 150;
    public static final int TTL = 60;
    public static final double DRAG = 0.98;
    public static final int SIZE = 3;
    public final Color COLOR = Color.ORANGE;

    int ttl;

    public Particle(Vector2D position, Vector2D velocity) {
        super(new Vector2D(position),
                randomVelocity().add(velocity),
                SIZE);
        this.ttl = (int)(Math.random() * TTL);
    }

    public static Vector2D randomVelocity() {
        return Vector2D.polar(
                Math.random() * 2 * Math.PI,
                Math.abs(new java.util.Random().nextGaussian() * PARTICLE_SPEED)
        );
    }

    @Override
    public void update() {
        velocity.mult(DRAG);
        super.update();
        ttl--;
        if (ttl <= 0) dead = true;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(COLOR);
        g.fillOval((int) position.x - SIZE / 2,
                (int) position.y - SIZE / 2,
                SIZE,
                SIZE);
    }

    @Override
    public boolean canCollide(GameObject other) {
        return false;
    }
}