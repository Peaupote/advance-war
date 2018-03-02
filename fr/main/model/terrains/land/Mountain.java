package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.Weather;
import fr.main.model.units.MoveType;
import fr.main.model.units.AbstractUnit;
import fr.main.model.terrains.Terrain;

public class Mountain extends Terrain implements LandTerrain {

    private static Mountain instance;
    protected static final Map<Weather,Map<MoveType,Integer>> weatherMovementCosts=new HashMap<Weather,Map<MoveType,Integer>>();
    public static final String name="Montagne";

    static{
        Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.INFANTRY,2);
        sunnyWeatherMovementCosts.put(MoveType.MECH,1);

        Map<MoveType,Integer> snowyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        snowyWeatherMovementCosts.put(MoveType.AIRY,2);
        snowyWeatherMovementCosts.put(MoveType.INFANTRY,4);
        snowyWeatherMovementCosts.put(MoveType.MECH,2);

        weatherMovementCosts.put(Weather.SUNNY,sunnyWeatherMovementCosts);
        weatherMovementCosts.put(Weather.RAINY,sunnyWeatherMovementCosts);
        // same movement costs if sun or rain
        weatherMovementCosts.put(Weather.SNOWY,snowyWeatherMovementCosts);
    }

    protected Mountain () {
        super(name, 4, 3, 2, weatherMovementCosts);
    }

    public static Mountain get () {
        if (instance == null)
            instance = new Mountain();
        return instance;
    }

    public boolean blockVision(AbstractUnit u){
        return true;
    }

}
