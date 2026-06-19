package game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

import static game.Constants.FRAME_WIDTH;
import static game.Constants.FRAME_HEIGHT;

public class View extends JComponent {
    public static final Color BG_COLOR = Color.BLACK;
    private Game game;
    Image im = Constants.MILKYWAY1;
    AffineTransform bgTransf;

    public View(Game game){
        this.game = game;
        double imWidth = im.getWidth(null);
        double imHeight = im.getHeight(null);
        double stretchx = imWidth > FRAME_WIDTH ? FRAME_WIDTH / imWidth : 1;
        double stretchy = imHeight > FRAME_HEIGHT ? FRAME_HEIGHT / imHeight : 1;
        bgTransf = new AffineTransform();
        bgTransf.scale(stretchx, stretchy);
    }

    public void paintComponent(Graphics g0) {
        int shakeX = 0;
        int shakeY = 0;

        if (Game.scrShaketime > 0) {
            shakeX = (int)((Math.random() - 0.5) * 2 * Game.scrShakeStrength);
            shakeY = (int)((Math.random() - 0.5) * 2 * Game.scrShakeStrength);
            Game.scrShaketime--;
        }

        Graphics2D g = (Graphics2D) g0;
        AffineTransform originalView = g.getTransform();
        g.translate(shakeX, shakeY);
        g.drawImage(im, bgTransf, null);
        synchronized (Game.class) {
            for (GameObject object : game.objects)
                object.draw(g);
            for (Particle p : game.particles)
                p.draw(g);
        }
        g.setColor(Color.YELLOW);g.setFont(new Font("dialog", Font.BOLD, 20));g.drawString("Level: "+Game.getLevel(), 20, FRAME_HEIGHT-20);
        g.drawString("High Score: " + Game.getHighScore(), FRAME_WIDTH/2-80, 30);
        g.drawString("Score: "+Game.getScore(), FRAME_WIDTH/3+20, FRAME_HEIGHT-20);
        g.drawString("Lives: "+Game.getLives(), 2*FRAME_WIDTH/3+20, FRAME_HEIGHT-20);
        if (Game.getLives()==0)
            g.drawString("GAME OVER Score "+Game.getScore(), FRAME_WIDTH/2-100, FRAME_HEIGHT/2-20);
        g.setTransform(originalView);

        // vignette/ui/hud aspects
        PlayerShip player = game.playerShip;

        // green flash for life gain
        if (Game.greenFlash > 0) {
            int alpha = Game.greenFlash * 5;
            g.setColor(new Color(0, 255, 0, Math.min(alpha, 120)));
            g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
            Game.greenFlash--;
        }

        // red flash for life lost
        if (Game.redFlash > 0) {
            int alpha = Game.redFlash * 6;
            g.setColor(new Color(255, 0, 0, Math.min(alpha, 150)));
            g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
            Game.redFlash--;
        }

        // blue overlay for invinciblity/shield indication
        if (player != null && (player.invincibilityTimer > 0 || player.shieldTimer > 0)) {
            Graphics2D g2 = (Graphics2D) g;
            int transparency = 60;
            g2.setColor(new Color(0, 100, 255, transparency));
            int sideThickness = 40;

            g2.fillRect(0, 0, FRAME_WIDTH, sideThickness); // up
            g2.fillRect(0, FRAME_HEIGHT - sideThickness, FRAME_WIDTH, sideThickness); // down
            g2.fillRect(0, 0, sideThickness, FRAME_HEIGHT); // left
            g2.fillRect(FRAME_WIDTH - sideThickness, 0, sideThickness, FRAME_HEIGHT); // right
        }
    }

    public Dimension getPreferredSize(){
        return Constants.FRAME_SIZE;
    }
}