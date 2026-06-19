package game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import static game.Constants.FRAME_HEIGHT;
import static game.Constants.FRAME_WIDTH;
import static utilities.SoundManager.bangMedium;

import utilities.SoundManager;
import utilities.Vector2D;

public class Saucer extends  Ship{
    public static final int HEIGHT = 12;
    public static final int WIDTH = 24;
    public static final int WIDTH_ELLIPSE = 20;

    public Color colorBelt;
    public Saucer(Controller ctrl, Color colorBody, Color colorBelt){
        super(new Vector2D(FRAME_WIDTH*Math.random(), FRAME_HEIGHT*Math.random()), new Vector2D(0, -1), 10);
        this.ctrl = ctrl;
        direction = new Vector2D(0,-1);
        thrusting = false;
        bullet = null;
        color = colorBody;
        this.colorBelt = colorBelt;
    }

    public void draw(Graphics2D g) {
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        Ellipse2D ellipse = new Ellipse2D.Double(-WIDTH_ELLIPSE / 2.0,
                -HEIGHT / 2.0, WIDTH_ELLIPSE, HEIGHT);
        g.setColor(color);
        g.fill(ellipse);
        g.setColor(colorBelt);
        g.drawLine(-WIDTH / 2, 0, WIDTH / 2, 0);
        g.setTransform(at);
    }

    @Override
    public boolean canCollide(GameObject other) {
        return !(other instanceof Asteroid);
    }

    public void hit() {
        super.hit();
        SoundManager.play(bangMedium);
    }
}
