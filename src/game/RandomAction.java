package game;

public class RandomAction implements Controller{
    Action action = new Action();

    int rotationDirection = 1;

    @Override
    public Action action() {
        double shoot = Math.random();
        action.shoot = shoot > 0.8;
        double thrust = Math.random();
        action.thrust = thrust>0.5 ? 1 : 0;
        double turn = Math.random();
        action.turn = turn < 0.2 ? rotationDirection : 0;
        if (Math.random() < 0.01) rotationDirection *= -1;
        return action;
    }
}
