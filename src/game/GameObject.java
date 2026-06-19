package game;

import utilities.Vector2D;

import java.awt.*;

import static game.Constants.*;

public abstract class GameObject {
    public Vector2D position;
    public Vector2D velocity;
    public double radius;
    public boolean dead;


    public GameObject(Vector2D position, Vector2D velocity, double radius) {
        this.position = position;
        this.velocity = velocity;
        this.radius = radius;
        this.dead = false;
    }

    public void update() {
        position.addScaled(velocity, DT);
        position.wrap(FRAME_WIDTH, FRAME_HEIGHT);
    }

    public void hit() {
        dead = true;
    }

    public boolean canCollide(GameObject other) {
        return true;
    }

    public boolean overlap(GameObject other) {
        return this.position.distWithWrap(other.position, FRAME_WIDTH, FRAME_HEIGHT) < this.radius + other.radius;
    }

    public void collisionHandling(GameObject other) {
        if (this.getClass() != other.getClass() && this.overlap(other) && this.canCollide(other)) {
            // makes sure the player doesnt die on powerup collision
            if (this instanceof PowerUp && other instanceof PlayerShip) {
                ((PlayerShip) other).applyRandomPowerUp();
                this.dead = true;
                return;
            }
            if (this instanceof PlayerShip && other instanceof PowerUp) {
                ((PlayerShip) this).applyRandomPowerUp();
                other.dead = true;
                return;
            }

            this.hit();
            other.hit();
            if (this instanceof Asteroid && other instanceof Bullet) {
                Bullet b = (Bullet) other;
                if (b.firedByShip){
                    Game.incScore(100);
                    Game.screenShakeTrigger(8, 3);
                }
            }
            if (this instanceof Bullet && other instanceof Asteroid) {
                Bullet b = (Bullet) this;
                if (b.firedByShip) Game.incScore(100);
            }
            if (this instanceof Saucer && other instanceof Bullet) {
                Bullet b = (Bullet) other;
                if (b.firedByShip) Game.incScore(500);
            }
            if (this instanceof Bullet && other instanceof Saucer) {
                Bullet b = (Bullet) this;
                if (b.firedByShip) Game.incScore(500);
            }
            if (this instanceof KamikazeEnemy && other instanceof Bullet) {
                Bullet b = (Bullet) other;
                if (b.firedByShip) Game.incScore(300);
            }
            if (this instanceof Bullet && other instanceof KamikazeEnemy) {
                Bullet b = (Bullet) this;
                if (b.firedByShip) Game.incScore(300);
            }
        }
    }

    public abstract void draw(Graphics2D g);

    public double distance(GameObject other) {
        return position.distWithWrap(other.position, FRAME_WIDTH, FRAME_HEIGHT);
    }

    public String toString() {
        return position.x + "," + position.y;
    }
}
