package edu.angelo.finalprojectlynch;

import java.util.Random;

public class World {
    static final int WORLD_WIDTH = 320;
    static final int WORLD_HEIGHT = 480;
    static final int SCORE_INCREMENT = 10;
    static final float TICK_INITIAL = 0.1f;

    public Ship ship;
    public AsteroidSet asteroidSet;
    public boolean gameOver = false;
    public int score = 0;
    Random random = new Random();
    float tickTime = 0;
    float tick = TICK_INITIAL;

    public World() {
        ship = new Ship();
        asteroidSet = new AsteroidSet();
    }

    public void update(float deltaTime) {
        if (gameOver) {
            return;
        }

        tickTime += deltaTime;

        while (tickTime > tick) {
            tickTime -= tick;
            ship.advance();
            if (ship.angleVelocity < 0)
            {
                ship.turnLeft();
            }
            if (ship.angleVelocity > 0)
            {
                ship.turnRight();
            }

            if (ship.activeBullet)
            {
                ship.bullet.advance();
            }
            asteroidSet.advance();

            if (ship.activeBullet)
            {
                for (int i = 0; i < asteroidSet.asteroids.size(); i++)
                {
                    if (Math.sqrt((ship.bullet.x - asteroidSet.asteroids.get(i).locationX) * (ship.bullet.x - asteroidSet.asteroids.get(i).locationX)
                            + (ship.bullet.y - asteroidSet.asteroids.get(i).locationY) * (ship.bullet.y - asteroidSet.asteroids.get(i).locationY))
                            <= 26)
                    {
                        asteroidSet.asteroids.remove(i);
                        ship.bullet = null;
                        ship.activeBullet = false;
                        break;
                    }
                }
            }
        }
    }
}
