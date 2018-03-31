package fr.main.model;

import java.util.Random;

/**
 * Represents the weather during the game
 */
public enum Weather implements java.io.Serializable{

    SUNNY("Ensoleillé", false, 1, 0),
    FOGGY("Brumeux",    true,  1, 0),
    RAINY("Pluvieux",   true,  1, 1),
    SNOWY("Neigeux",    true,  2, 2);

    /**
     * malusFuel : the multiplying factor of the quantity of fuel consumed
     * malusVision : the amount removed to the vision of the units when there is this weather
     */
    public final int malusFuel,malusVision;
    public final String name;
    /**
     * true if and only if there is fog with this weather
     */
    public final boolean fog;

    private Weather(String name, boolean fog, int malusFuel, int malusVision){
        this.name        = name;
        this.fog         = fog;
        this.malusFuel   = malusFuel;
        this.malusVision = malusVision;
    }

    /**
     * @param fog is true if and only if the basic weather (SUNNY or FOGGY) returned has fog (so FOGGY if true and SUNNY otherwise) 
     * @return a random Weather with 5% chance of being SNOWY, 10% chance of being RAINY, and 85% chance of being the other one
     */
    public static Weather random(boolean fog){
        if (fog) return random(0,85,10,5);
        else     return random(85,0,10,5);
    }

    /**
     * @param sun is the proportion of SUNNY returned
     * @param fog is the proportion of FOGGY returned
     * @param rain is the proportion of RAINY returned
     * @param snow is the proportion of SNOWY returned
     * @return a random Weather accordingly to the proportions
     */
    static Weather random(int sun, int fog, int rain, int snow){
        int sum     = sun+fog+rain+snow;
        Random rand = new Random();

        int res = rand.nextInt(sum);
        if (res < sun) return SUNNY;
        res -= sun;

        if (res < fog) return FOGGY;
        res -= fog;
        
        if (res < rain) return RAINY;
        
        return SNOWY;
    }

}
