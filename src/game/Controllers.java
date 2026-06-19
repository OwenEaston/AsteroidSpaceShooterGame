package game;

import utilities.Vector2D;

import static game.Constants.DT;
import static game.Constants.FRAME_WIDTH;


public class Controllers {
    public static GameObject findTarget(Ship ship, Iterable<GameObject> gameObjects, double distanceLimit) {
        // don't find targets further away than distanceLimit
        double minDistance = FRAME_WIDTH;
        GameObject closestTarget = null;
        for (GameObject obj : gameObjects) {
            if (obj==ship || !ship.canHit(obj) || (obj instanceof Bullet))
                continue;
            double dist = ship.distance(obj);
            if (dist < distanceLimit && dist < minDistance) {
                closestTarget = obj;
                minDistance = dist;
            }
        }
        if (closestTarget==null) System.out.println("No target found");
        return closestTarget;
    }

    public static double angleToTarget(Ship ship, GameObject target) {
        Vector2D targetPosition = new Vector2D(target.position).addScaled(target.velocity, Constants.DT * 5);
        return ship.direction.angle(targetPosition.subtract(ship.position));
    }

    public static int aim(Ship ship, GameObject target) {
        double angle = angleToTarget(ship, target);
        System.out.println("angle = " + angle);
        if (Math.abs(angle) < 0.8 * Ship.STEER_RATE * DT)
            return 0;
        else
            return angle > 0 ? 1 : -1;
    }

}
