package fr.main.model.terrains.naval;

import java.util.HashMap;
import java.util.Map;

import fr.main.model.Weather;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.air.AirUnit;

/**
 * represents a reef
 */
public class Reef extends Terrain implements NavalTerrain {

    /**
	 * Add Reef UID
	 */
	private static final long serialVersionUID = -544222409543153408L;
	private static Reef instance;
    protected static final Map<Weather,Map<MoveType,Integer>> weatherMovementCosts=new HashMap<Weather,Map<MoveType,Integer>>();

    static{
        Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        sunnyWeatherMovementCosts.put(MoveType.NAVAL,2);
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.LANDER,2);

        Map<MoveType,Integer> snowyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        sunnyWeatherMovementCosts.put(MoveType.NAVAL,2);
        sunnyWeatherMovementCosts.put(MoveType.AIRY,2);
        sunnyWeatherMovementCosts.put(MoveType.LANDER,2);

        weatherMovementCosts.put(Weather.SUNNY,sunnyWeatherMovementCosts);
        weatherMovementCosts.put(Weather.RAINY,sunnyWeatherMovementCosts);
        // same movement costs if sun or rain
        weatherMovementCosts.put(Weather.SNOWY,snowyWeatherMovementCosts);
    }

    protected Reef () {
        super("Récif", 2, 0, weatherMovementCosts);
    }

    @Override
    public boolean hideFrom(AbstractUnit from){
        return !AirUnit.class.isInstance(from);
    }

    public static Reef get () {
        if (instance == null) instance = new Reef();
        return instance;
    }

}
