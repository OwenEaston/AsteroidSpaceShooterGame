package game;

import utilities.Vector2D;

import java.awt.*;

public class PowerUp extends GameObject {

    public static final int RADIUS = 10;

    public PowerUp(Vector2D pos) {
        super(pos, new Vector2D(0,0), RADIUS);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.YELLOW);
        g.fillOval((int) position.x - RADIUS, (int) position.y - RADIUS, 2*RADIUS, 2*RADIUS);
    }

    @Override
    public void update() {
    }

    @Override
    public void hit() {
        dead = true;
    }
}