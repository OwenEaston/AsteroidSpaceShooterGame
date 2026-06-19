package game;

import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static game.Constants.FRAME_HEIGHT;
import static game.Constants.FRAME_WIDTH;

// goal is for enemy ship to spawn, lock onto player, and charge into them

public class KamikazeEnemy extends GameObject {

    private PlayerShip target;
    public static final double SPEED = 120;
    double angle = 0;

    public KamikazeEnemy(PlayerShip target) {
        super(new Vector2D(0,0), new Vector2D(0,0), 10);

        this.target = target;

        int side = (int)(Math.random() * 4);

        if (side == 0) { // top
            position = new Vector2D(Math.random() * FRAME_WIDTH, 0);
        }
        else if (side == 1) { // bottom
            position = new Vector2D(Math.random() * FRAME_WIDTH, FRAME_HEIGHT);
        }
        else if (side == 2) { // left
            position = new Vector2D(0, Math.random() * FRAME_HEIGHT);
        }
        else { // right
            position = new Vector2D(FRAME_WIDTH, Math.random() * FRAME_HEIGHT);
        }
    }

    @Override
    public void update() {

        Vector2D direction = new Vector2D(target.position).subtract(position).normalise();

        // kamikaze ship speeds up towards player
        velocity.addScaled(direction, 200 * Constants.DT);
        velocity.mult(0.99);

        // make ship face the player
        angle = Math.atan2(direction.y, direction.x);

        super.update();
    }

    @Override
    public void draw(Graphics2D g) {

        AffineTransform at = g.getTransform();

        g.translate(position.x, position.y);
        g.rotate(angle + Math.PI / 2);

        int[] x = {0, -8, 8};
        int[] y = {-12, 8, 8};

        g.setColor(Color.RED);
        g.fillPolygon(x, y, 3);

        g.setTransform(at);
    }

    @Override
    public void hit() {
        super.hit();
    }
}