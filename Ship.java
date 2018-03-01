package edu.angelo.finalprojectlynch;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    public int angleTop;
    public int angleRight;
    public int angleLeft;
    public int angleVelocity;
    public double velocityX;
    public double velocityY;
    public double x;
    public double y;
    public Ship bullet;
    public boolean activeBullet;

    public Ship() {
        angleTop = 0;
        angleRight = 135;
        angleLeft = 225;
        angleVelocity = 0;
        velocityX = 0;
        velocityY = 0;
        x = Asteroid.maxX / 2;
        y = Asteroid.maxY / 2;
        activeBullet = false;
    }

    public void turnLeft() {
        angleTop -= angleVelocity;
        angleRight -= angleVelocity;
        angleLeft -= angleVelocity;

        if (angleTop < 0)
        {
            angleTop = 359;
        }
        if (angleRight < 0)
        {
            angleRight = 359;
        }
        if (angleLeft < 0)
        {
            angleLeft = 359;
        }
    }

    public void turnRight() {
        angleTop += angleVelocity;
        angleRight += angleVelocity;
        angleLeft += angleVelocity;

        if (angleTop > 359)
        {
            angleTop = 0;
        }
        if (angleRight > 359)
        {
            angleRight = 0;
        }
        if (angleLeft < 359)
        {
            angleLeft = 0;
        }
    }

    public void advance() {
        if (x + velocityX < Asteroid.minX)
        {
            x += Asteroid.maxX;
        }
        else if (x + velocityX > Asteroid.maxX)
        {
            x -= Asteroid.maxX;
        }
        else
        {
            x += velocityX;
        }

        if (y + velocityY < Asteroid.minY)
        {
            y += Asteroid.maxY;
        }
        else if (y + velocityY > Asteroid.maxY)
        {
            y -= Asteroid.maxY;
        }
        else
        {
            y += velocityY;
        }
    }

    public void thrust() {
        if (angleTop == 0)
        {
            velocityY -= .7;
        }
        else if (angleTop > 0 && angleTop < 90)
        {
            velocityX += .7 * Math.sin(Math.toRadians(angleTop));
            velocityY -= .7 * Math.cos(Math.toRadians(angleTop));
        }
        else if (angleTop == 90)
        {
            velocityX += .7;
        }
        else if (angleTop > 90 && angleTop < 180)
        {
            velocityX += .7 * Math.cos(Math.toRadians(angleTop - 90));
            velocityY += .7 * Math.sin(Math.toRadians(angleTop - 90));
        }
        else if (angleTop == 180)
        {
            velocityY += .7;
        }
        else if (angleTop > 180 && angleTop < 270)
        {
            velocityX -= .7 * Math.sin(Math.toRadians(angleTop - 180));
            velocityY += .7 *  Math.cos(Math.toRadians(angleTop - 180));
        }
        else if (angleTop == 270)
        {
            velocityX -= .7;
        }
        else if (angleTop > 270 && angleTop < 360)
        {
            velocityX -= .7 * Math.cos(Math.toRadians(angleTop - 270));
            velocityY -= .7 * Math.sin(Math.toRadians(angleTop - 270));
        }
    }

    public void shoot() {
        bullet = new Ship();
        activeBullet = true;
        bullet.x = x;
        bullet.y = y;

        if (angleTop == 0)
        {
            bullet.velocityY = -7;
        }
        else if (angleTop > 0 && angleTop < 90)
        {
            bullet.velocityX = 7 * Math.sin(Math.toRadians(angleTop));
            bullet.velocityY = -7 * Math.cos(Math.toRadians(angleTop));
        }
        else if (angleTop == 90)
        {
            bullet.velocityX = 7;
        }
        else if (angleTop > 90 && angleTop < 180)
        {
            bullet.velocityX = 7 * Math.cos(Math.toRadians(angleTop - 90));
            bullet.velocityY = 7 * Math.sin(Math.toRadians(angleTop - 90));
        }
        else if (angleTop == 180)
        {
            bullet.velocityY = 7;
        }
        else if (angleTop > 180 && angleTop < 270)
        {
            bullet.velocityX = -7 * Math.sin(Math.toRadians(angleTop - 180));
            bullet.velocityY = 7 * Math.cos(Math.toRadians(angleTop - 180));
        }
        else if (angleTop == 270)
        {
            bullet.velocityX = -7;
        }
        else if (angleTop > 270 && angleTop < 360)
        {
            bullet.velocityX = -7 * Math.cos(Math.toRadians(angleTop - 270));
            bullet.velocityY = -7 * Math.sin(Math.toRadians(angleTop - 270));
        }
    }
}
