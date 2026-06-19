package game;

import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static game.Constants.FRAME_HEIGHT;
import static game.Constants.FRAME_WIDTH;

public class Asteroid extends GameObject {

    private int[] pX;
    private int[] pY;
    private int nPoints;

    private double rotationPerFrame;
    private double angle = 0;

    public static final double MAX_SPEED = 100;
    public boolean isLarge = true;
    public List<Asteroid> spawnedAsteroids = new ArrayList<Asteroid>();

    private static final int MIN_POINTS = 8;
    private static final int MAX_POINTS = 14;

    private static final double MIN_RADIUS = 15;
    private static final double MAX_RADIUS = 35;

    private static final double MIN_ROTATION = -0.05;
    private static final double MAX_ROTATION = 0.05;

    private static final BufferedImage texture =
            (BufferedImage) Constants.ASTEROID1;

    public Asteroid(Vector2D pos, double vx, double vy, boolean isLarge) {
        super(pos, new Vector2D(vx, vy), 0);
        this.isLarge = isLarge;
        generatePolygon();
        if (!isLarge) scalePolygon(0.6);
    }

    public Asteroid() {
        super(
                new Vector2D(Math.random() * FRAME_WIDTH, Math.random() * FRAME_HEIGHT),
                new Vector2D(
                        (Math.random() - 0.5) * MAX_SPEED,
                        (Math.random() - 0.5) * MAX_SPEED
                ),
                0
        );
        generatePolygon();
    }

    private void generatePolygon() {
        nPoints = MIN_POINTS + (int)(Math.random() * (MAX_POINTS - MIN_POINTS));

        pX = new int[nPoints];
        pY = new int[nPoints];

        double totalRadius = 0;

        for (int i = 0; i < nPoints; i++) {
            double theta = 2 * Math.PI * (i + Math.random()) / nPoints;
            double r = MIN_RADIUS + Math.random() * (MAX_RADIUS - MIN_RADIUS);

            int x = (int)(r * Math.cos(theta));
            int y = (int)(r * Math.sin(theta));

            pX[i] = x;
            pY[i] = y;

            totalRadius += r;
        }

        this.radius = totalRadius / nPoints;

        rotationPerFrame = MIN_ROTATION + Math.random() * (MAX_ROTATION - MIN_ROTATION);
    }

    private void scalePolygon(double scale) {
        for (int i = 0; i < nPoints; i++) {
            pX[i] *= scale;
            pY[i] *= scale;
        }
        radius *= scale;
    }

    private void spawn() {
        for (int i = 0; i < 2; i++) {
            double vx = (Math.random() - 0.5) * MAX_SPEED;
            double vy = (Math.random() - 0.5) * MAX_SPEED;
            spawnedAsteroids.add(new Asteroid(new Vector2D(position.x, position.y), vx, vy, false));
        }
    }

    @Override
    public void hit() {
        super.hit();
        if (isLarge) {
            SoundManager.play(SoundManager.bangMedium);
            spawn();
        } else {
            SoundManager.play(SoundManager.bangSmall);
        }
    }

    @Override
    public boolean canCollide(GameObject other) {
        return !(other instanceof Saucer);
    }

    @Override
    public void update() {
        super.update();
        angle += rotationPerFrame;
    }

    @Override
    public void draw(Graphics2D g) {

        AffineTransform at = g.getTransform();

        g.translate(position.x, position.y);
        g.rotate(angle);

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (int i = 0; i < nPoints; i++) {
            if (pX[i] < minX) minX = pX[i];
            if (pY[i] < minY) minY = pY[i];
            if (pX[i] > maxX) maxX = pX[i];
            if (pY[i] > maxY) maxY = pY[i];
        }

        Rectangle rect = new Rectangle(minX, minY,
                maxX - minX,
                maxY - minY);

        TexturePaint tp = new TexturePaint(texture, rect);

        g.setPaint(tp);
        g.fillPolygon(pX, pY, nPoints);
        g.setColor(Color.DARK_GRAY);

        g.setTransform(at);
    }

    public String toString() {
        return "Asteroid: " + super.toString();
    }
}