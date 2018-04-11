package fr.main.model.terrains.naval;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.Weather;
import fr.main.model.units.MoveType;
import fr.main.model.terrains.Terrain;

/**
 * represents a sea
 */
public class Sea extends Terrain implements NavalTerrain {

    /**
	 * Add Sea UID
	 */
	private static final long serialVersionUID = -3963530451255190200L;
	private static Sea instance;
    protected static final Map<Weather,Map<MoveType,Integer>> weatherMovementCosts=new HashMap<Weather,Map<MoveType,Integer>>();
    public static final String name="Mer";

    static{
        Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.LANDER,1);
        sunnyWeatherMovementCosts.put(MoveType.NAVAL,1);

        Map<MoveType,Integer> snowyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        snowyWeatherMovementCosts.put(MoveType.AIRY,2);
        snowyWeatherMovementCosts.put(MoveType.LANDER,2);
        snowyWeatherMovementCosts.put(MoveType.NAVAL,2);

        weatherMovementCosts.put(Weather.SUNNY,sunnyWeatherMovementCosts);
        weatherMovementCosts.put(Weather.RAINY,sunnyWeatherMovementCosts);
        // same movement costs if sun or rain
        weatherMovementCosts.put(Weather.SNOWY,snowyWeatherMovementCosts);
    }

    protected Sea () {
        super(name, 1, 0, weatherMovementCosts);
    }

    public static Sea get () {
        if (instance == null) instance = new Sea();
        return instance;
    }

}