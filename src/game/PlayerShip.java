package game;

import utilities.SoundManager;
import utilities.Vector2D;
import static utilities.SoundManager.bangLarge;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static game.Constants.*;

public class PlayerShip extends Ship {
    public int invincibilityTimer;
    public int shieldTimer = 0;
    public int shieldCooldown = 0;
    // how long the shield will last
    public static final int MAX_SHIELD = 120;
    // how long the shield will take to regenerate
    public static final int SHIELD_COOLDOWN = 240;

    // power up vars
    public int powerTimer = 0;
    public double originalRadius = radius;
    public boolean poweredUp = false;

    public PlayerShip(Controller ctrl) {
        super(new Vector2D(FRAME_WIDTH / 2, FRAME_HEIGHT / 2), new Vector2D(0, -1), 10);
        this.ctrl = ctrl;
        direction = new Vector2D(0,-1);
        thrusting = false;
        bullet = null;
        color = Color.CYAN;
        invincibilityTimer = 120;
    }

    @Override
    public void update() {
        Action action = ctrl.action();
        if (action.shield && shieldTimer <= 0 && shieldCooldown <= 0) {
            shieldTimer = MAX_SHIELD;
            shieldCooldown = SHIELD_COOLDOWN;
        }
        // decreases both the shield cooldown and shield timer every update
        if (shieldTimer > 0)
            shieldTimer--;
        if (shieldCooldown > 0)
            shieldCooldown--;

        super.update();
        // decreases the timer every update until 0
        if (invincibilityTimer > 0)
            invincibilityTimer--;

        // invincibility star powerup speed boost
        double acc = PlayerShip.MAG_ACC;
        if (poweredUp) acc *= 2;
        velocity.addScaled(direction, acc * DT * action.thrust);
    }

    @Override
    public void draw(Graphics2D g) {
        if (invincibilityTimer > 0 && (invincibilityTimer / 5) % 2 == 0)
            return;
        AffineTransform at = g.getTransform();
        g.translate(position.x, position.y);
        double rot = direction.angle() + Math.PI / 2;
        g.rotate(rot);
        g.scale(DRAWING_SCALE, DRAWING_SCALE);
        g.setColor(color);
        g.fillPolygon(XP, YP, XP.length);
        if (thrusting) {
            g.setColor(Color.red);
            g.fillPolygon(XPTHRUST, YPTHRUST, XPTHRUST.length);
        }
        if (shieldTimer > 0) {
            g.setColor(new Color(0,150,255,150));
            g.setStroke(new BasicStroke(2));
            g.drawOval(-20, -20, 40, 40);
        }
        g.setTransform(at);
    }

    @Override
    public void hit() {
        if (dead || invincibilityTimer > 0 || shieldTimer > 0) return;
        super.hit();
        Game.loseLife();
        SoundManager.play(bangLarge);
        Game.triggerRedFlash(20);
        Game.screenShakeTrigger(20, 8);
        System.out.println("Ship hit");
    }

    @Override
    public boolean canCollide(GameObject other) {
        // if the player has spawn invincibility or shield active, they cannot be hit
        return invincibilityTimer <= 0 && shieldTimer <= 0 && super.canCollide(other);
    }

    // power up activation
    public void applyRandomPowerUp() {
        int choice = (int)(Math.random() * 3);

        if (choice == 0) activateStar();
        else if (choice == 1) activateExtraLife();
        else activateBurst();
    }
    // POWER UP 1 - INVINCIBILITY STAR
    private void activateStar() {
        powerTimer = 300;
        invincibilityTimer = 300;
        poweredUp = true;
    }
    // POWER UP 2 - EXTRA LIFE
    private void activateExtraLife() {
        Game.triggerGreenFlash(20);
        Game.gainLife();
    }
    // POWER UP 3 - BULLET BURST
    private void activateBurst() {
        invincibilityTimer = 10;
        int bullets = 16;

        for (int i = 0; i < bullets; i++) {
            double angle = 2 * Math.PI * i / bullets;
            Vector2D dir = Vector2D.polar(angle, 1);
            Vector2D bulletPos = new Vector2D(position);
            Vector2D bulletVel = new Vector2D(dir).mult(MUZZLE_VELOCITY * 2);
            Bullet b = new Bullet(bulletPos, bulletVel, true);

            b.position.addScaled(dir, radius + b.radius);

            Game.currentGame.objects.add(b);
        }
    }

    public String toString() {
        return "Ship: " + super.toString();
    }
}