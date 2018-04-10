package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.Weather;
import fr.main.model.units.MoveType;
import fr.main.model.terrains.Terrain;

/**
 * represents a river
 */
public class River extends Terrain implements LandTerrain {

    /**
	 * Add River UID
	 */
	private static final long serialVersionUID = -5995606505466883666L;
	private static River instance;
    protected static final Map<Weather,Map<MoveType,Integer>> weatherMovementCosts=new HashMap<Weather,Map<MoveType,Integer>>();
    public static final String name="Rivière";

    static{
        Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.INFANTRY,2);
        sunnyWeatherMovementCosts.put(MoveType.MECH,1);
        sunnyWeatherMovementCosts.put(MoveType.LANDER,1);
        sunnyWeatherMovementCosts.put(MoveType.NAVAL,1);

        Map<MoveType,Integer> snowyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        snowyWeatherMovementCosts.put(MoveType.AIRY,2);
        snowyWeatherMovementCosts.put(MoveType.INFANTRY,2);
        snowyWeatherMovementCosts.put(MoveType.MECH,1);
        sunnyWeatherMovementCosts.put(MoveType.LANDER,2);
        sunnyWeatherMovementCosts.put(MoveType.NAVAL,2);

        weatherMovementCosts.put(Weather.SUNNY,sunnyWeatherMovementCosts);
        weatherMovementCosts.put(Weather.RAINY,sunnyWeatherMovementCosts);
        // same movement costs if sun or rain
        weatherMovementCosts.put(Weather.SNOWY,snowyWeatherMovementCosts);
    }

    protected River() {
        super(name, 0, 0, weatherMovementCosts);
    }

    public static River get () {
        if (instance == null) instance = new River();
        return instance;
    }
}
