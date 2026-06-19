package game;

import utilities.JEasyFrame;
import utilities.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static game.Constants.*;

public class Game {
    public static int redFlash = 0;
    public static int greenFlash = 0;
    public static Game currentGame;
    public static final int N_INITIAL_ASTEROIDS = 7;
    public static int highScore = 0;
    public List<GameObject> objects;
    public List<Ship> ships;
    public List<Particle> particles;
    PlayerShip playerShip;
    Keys ctrl;
    Controller controller;
    public int respawnTimer = 0;
    // screen shake length/intensity
    public static int scrShaketime = 0;
    public static double scrShakeStrength = 0;


    private static int score = 0;
    private static int lives = 3;
    private static int level = 1;
    public static boolean gameOver = false;

    public Game() {
        currentGame = this;
        objects = new ArrayList<GameObject>();
        ships = new ArrayList<Ship>();
        particles = new ArrayList<Particle>();
        for (int i = 0; i < N_INITIAL_ASTEROIDS; i++) {
            objects.add(new Asteroid());
        }

        ctrl = new Keys();
        controller = ctrl;
        playerShip = new PlayerShip(controller);
        objects.add(playerShip);
        ships.add(playerShip);

        addSaucers();
    }

    public void newLevel() {
        level++;
        try {
        } catch (Exception e) {
        }
        synchronized (Game.class) {
            objects.clear();
            ships.clear();
            particles.clear();
            for (int i = 0; i < N_INITIAL_ASTEROIDS + 2 * (level - 1); i++) {
                objects.add(new Asteroid());
            }
            playerShip = new PlayerShip(controller);
            objects.add(playerShip);
            ships.add(playerShip);
            addSaucers();
        }
    }

    public void newLife() {
        synchronized (Game.class) {
            objects.clear();
            ships.clear();
            particles.clear();
            for (int i = 0; i < N_INITIAL_ASTEROIDS + 2 * (level - 1); i++) {
                objects.add(new Asteroid());
            }
            playerShip = new PlayerShip(controller);
            objects.add(playerShip);
            ships.add(playerShip);
            addSaucers();
        }
    }

    private void addSaucers() {
        for (int i = 0; i < 3; i++) {
            Controller ctrl = (i % 3 != 0 ? new RandomAction() : new AimNShoot(this));
            Color colorBody = (i % 3 != 0 ? Color.PINK : Color.GREEN);
            Random r = new Random();
            Vector2D s = new Vector2D(
                    r.nextInt(Constants.FRAME_WIDTH),
                    r.nextInt(Constants.FRAME_HEIGHT));
            Ship saucer = new Saucer(ctrl, colorBody, Color.white);
            if (i % 3 == 0) {
                ((AimNShoot) ctrl).setShip(saucer);
                Vector2D posDiff = new Vector2D(saucer.position).subtract(playerShip.position);
                if (posDiff.mag() < AimNShoot.SHOOTING_DISTANCE) {
                    saucer.position = new Vector2D(playerShip.position).addScaled(posDiff.normalise(), AimNShoot.SHOOTING_DISTANCE * 0.75);
                }
            }
            objects.add(saucer);
            ships.add(saucer);
        }
    }

    public static void main(String[] args) {
        loadHighScore();
        Game game = new Game();
        View view = new View(game);
        new JEasyFrame(view, "Game with AI").addKeyListener(game.ctrl);
        while (!gameOver) {
            game.update();
            view.repaint();
            try {
                Thread.sleep(DELAY);
            } catch (Exception e) {
            }
        }
    }

    public void update() {
        // powerup spawn
        if (Math.random() < 0.005) {
            objects.add(new PowerUp(
                    new Vector2D(Math.random()*FRAME_WIDTH, Math.random()*FRAME_HEIGHT)
            ));
        }

        // randomly spawn kamikaze enemy
        if (Math.random() < 0.01) {
            spawnKamikazeEnemy();
        }

        for (int i = 0; i < objects.size(); i++) {
            GameObject o1 = objects.get(i);
            for (int j = i + 1; j < objects.size(); j++) {
                GameObject o2 = objects.get(j);
                o1.collisionHandling(o2);
            }
        }

        List<GameObject> alive = new ArrayList<>();
        boolean noAsteroids = true;
        boolean noShip = true;

        for (GameObject o : objects) {
            o.update();

            if (o.dead) {
                if (o instanceof Asteroid || o instanceof Ship) {
                    explosion(o);
                }
            }

            if (o instanceof Asteroid) {
                noAsteroids = false;
                Asteroid a = (Asteroid) o;
                if (!a.spawnedAsteroids.isEmpty()) {
                    alive.addAll(a.spawnedAsteroids);
                    a.spawnedAsteroids.clear();
                }
            } else if (o instanceof PlayerShip) noShip = false;

            if (!o.dead) alive.add(o);

            for (Ship s : ships)
                if (s.bullet != null) {
                    alive.add(s.bullet);
                    s.bullet = null;
                }
        }

        synchronized (Game.class) {
            objects.clear();
            objects.addAll(alive);
        }

        updateParticles();

        if (noAsteroids) {
            newLevel();
        } else if (noShip) {
            if (respawnTimer == 0) {
                respawnTimer = 60;
            }
        }

        if (respawnTimer > 0) {
            respawnTimer--;
            if (respawnTimer == 0) {
                newLife();
            }
        }
    }

    public void explosion(GameObject object) {
        int count = 80;
        for (int i = 0; i < count; i++) {
            particles.add(new Particle(object.position, object.velocity));
        }
    }

    public void updateParticles() {
        List<Particle> alive = new ArrayList<>();
        for (Particle p : particles) {
            p.update();
            if (!p.dead) alive.add(p);
        }
        synchronized (Game.class) {
            particles.clear();
            particles.addAll(alive);
        }
    }

    public static void incScore(int inc) {
        int oldScore = score;
        score += inc;
        if (score > highScore)
            highScore = score;
        if (score / 5000 > oldScore / 5000) {
            lives++;
        }
    }

    public static void loseLife() {
        lives--;
        if (lives == 0) {
            gameOver = true;
            saveHighScore();
        }
    }

    public static void gainLife() {
        lives++;
    }

    public static int getScore() {
        return score;
    }

    public static int getLevel() {
        return level;
    }

    public static int getLives() {
        return lives;
    }

    public static void loadHighScore() {
        // attempts to read "highscore.txt" and parse the value. if unable, resume without
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.FileReader("highscore.txt"));
            highScore = Integer.parseInt(reader.readLine());
            reader.close();
        } catch (Exception e) {
            highScore = 0;
        }
    }

    public static void saveHighScore() {
        // write current high score to highscore.txt
        try {
            java.io.PrintWriter writer = new java.io.PrintWriter("highscore.txt");
            writer.println(highScore);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getHighScore() {
        return highScore;
    }

    private void spawnKamikazeEnemy() {
        KamikazeEnemy enemy = new KamikazeEnemy(playerShip);
        objects.add(enemy);
    }

    public static void screenShakeTrigger(int duration, double strength) {
        scrShaketime = duration;
        scrShakeStrength = strength;
    }

    public static void triggerRedFlash(int duration) {
        redFlash = duration;
    }

    public static void triggerGreenFlash(int duration) {
        greenFlash = duration;
    }
}