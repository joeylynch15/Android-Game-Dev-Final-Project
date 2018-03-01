package edu.angelo.finalprojectlynch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jlynch6 on 11/28/2017.
 */
public class AsteroidSet {
    public List<Asteroid> asteroids; //List of targets

    /**
     * Constructor
     * Creates and initializes a target set
     * @param numTargets How many targets will be in the set
     */
    public AsteroidSet(int numTargets) {

        asteroids = new ArrayList<Asteroid>(); //Create an array list of Targets

        //Add one target to the new array list to equal the number of targets
        for (int i = 0; i < numTargets; i++)
        {
            asteroids.add(new Asteroid());
        }
    }

    //Default constructor, makes 10 targets by default
    public AsteroidSet() {
        this(4);
    }

    /**
     * Calls the advance method on each target in the array
     */
    public void advance() {
        for (int i = 0; i < asteroids.size(); i++)
        {
            asteroids.get(i).advance();
        }
    }
}
