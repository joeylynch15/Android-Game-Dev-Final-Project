package edu.angelo.finalprojectlynch;

import java.util.Random;

public class Asteroid {
    private static Random random = new Random();
    public static final int minX = 0;                      // farthest left the center of a target can go
    public static final int maxX = World.WORLD_WIDTH;  // farthest right the center of a target can go
    public static final int minY = 0;                      // leaving room at the top of the screen for pause and score
    public static final int maxY = World.WORLD_HEIGHT - 80; // leaving room for the radius of the target

    public int locationX, locationY, velocityX, velocityY; //Keep track of current positions and speeds

    /**
     * This updates a target's position and speed depending on where it is at and where it will be once the update is applied
     */
    public void advance() {
        if (locationX + velocityX < minX) //If the new location will be out of bounds
        {
            locationX += maxX;
        }
        else if (locationX + velocityX > maxX) //If the new location will be out of bounds
        {
            locationX -= maxX;
        }
        else
            locationX += velocityX; //If it is in bounds, just add the velocity to the targets location

        if (locationY + velocityY < minY) //If the new location will be out of bounds
        {
            locationY += maxY;
        }
        else if (locationY + velocityY > maxY) //If the new location will be out of bounds
        {
            locationY -= maxY;
        }
        else
            locationY += velocityY; //If it is in bounds, just add the velocity to the targets location
    }

    public void hit() {

    }

    public void randomize() {
        locationX = random.nextInt(maxX);    //Generate a random X and Y location that is in bounds
        locationY = random.nextInt(maxY);  //
        velocityX = random.nextInt(11) - 10;             //Generate a random velocity between -10 and 10 for X and Y
        velocityY = random.nextInt(11) - 10;             //
    }

    //Default constructor for a Target, generates its new location and velocity
    public Asteroid() {
        randomize();
    }
}
