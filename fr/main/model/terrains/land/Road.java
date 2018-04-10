package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.Weather;
import fr.main.model.units.MoveType;
import fr.main.model.terrains.Terrain;

/**
 * Represents a road
 */
public class Road extends Terrain implements LandTerrain {

    /**
	 * Add Road UID
	 */
	private static final long serialVersionUID = 4359565654338863950L;
	private static Road instance;
    protected static final Map<Weather,Map<MoveType,Integer>> weatherMovementCosts=new HashMap<Weather,Map<MoveType,Integer>>();
    public static final String name="Route";

    static{
        Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        sunnyWeatherMovementCosts.put(MoveType.MECH,1);
        sunnyWeatherMovementCosts.put(MoveType.WHEEL,1);
        sunnyWeatherMovementCosts.put(MoveType.TREAD,1);

        Map<MoveType,Integer> snowyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        snowyWeatherMovementCosts.put(MoveType.AIRY,2);
        snowyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        snowyWeatherMovementCosts.put(MoveType.MECH,1);
        snowyWeatherMovementCosts.put(MoveType.WHEEL,1);
        snowyWeatherMovementCosts.put(MoveType.TREAD,1);

        weatherMovementCosts.put(Weather.SUNNY,sunnyWeatherMovementCosts);
        weatherMovementCosts.put(Weather.RAINY,sunnyWeatherMovementCosts);
        // same movement costs if sun or rain
        weatherMovementCosts.put(Weather.SNOWY,snowyWeatherMovementCosts);
    }

    protected Road() {
        super(name, 0, 0, weatherMovementCosts);
    }

    public static Road get () {
        if (instance == null)
            instance = new Road();
        return instance;
    }
}
