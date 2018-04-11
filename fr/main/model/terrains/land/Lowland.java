package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.Weather;
import fr.main.model.units.MoveType;
import fr.main.model.terrains.Terrain;

/**
 * represents a lowland
 */
public class Lowland extends Terrain implements LandTerrain {

    /**
	 * Add Lowland UID
	 */
	private static final long serialVersionUID = 8130230126646489124L;
	private static Lowland instance;
    public static final String name="Plaine";
    protected static final Map<Weather,Map<MoveType,Integer>> weatherMovementCosts=new HashMap<Weather,Map<MoveType,Integer>>();

    static{
        Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.WHEEL,2);
        sunnyWeatherMovementCosts.put(MoveType.TREAD,1);
        sunnyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        sunnyWeatherMovementCosts.put(MoveType.MECH,1);

        Map<MoveType,Integer> rainyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        rainyWeatherMovementCosts.put(MoveType.AIRY,1);
        rainyWeatherMovementCosts.put(MoveType.WHEEL,3);
        rainyWeatherMovementCosts.put(MoveType.TREAD,2);
        rainyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        rainyWeatherMovementCosts.put(MoveType.MECH,1);

        Map<MoveType,Integer> snowyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        snowyWeatherMovementCosts.put(MoveType.AIRY,2);
        snowyWeatherMovementCosts.put(MoveType.WHEEL,3);
        snowyWeatherMovementCosts.put(MoveType.TREAD,2);
        snowyWeatherMovementCosts.put(MoveType.INFANTRY,2);
        snowyWeatherMovementCosts.put(MoveType.MECH,1);

        weatherMovementCosts.put(Weather.SUNNY,sunnyWeatherMovementCosts);
        weatherMovementCosts.put(Weather.RAINY,rainyWeatherMovementCosts);
        weatherMovementCosts.put(Weather.SNOWY,snowyWeatherMovementCosts);
    }

    protected Lowland() {
        super(name, 1, 0, weatherMovementCosts);
    }

    public static Lowland get () {
        if (instance == null)
            instance = new Lowland();
        return instance;
    }

}
