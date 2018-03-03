package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.Weather;
import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.terrains.naval.NavalTerrain;
import fr.main.model.terrains.Terrain;

public class Beach extends Terrain implements LandTerrain,NavalTerrain {
  
    private static Beach instance;
    public static final String name="Plage";
    protected static final Map<Weather,Map<MoveType,Integer>> weatherMovementCosts=new HashMap<Weather,Map<MoveType,Integer>>();

    static{
        Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.LANDER,1);
        sunnyWeatherMovementCosts.put(MoveType.WHEEL,1);
        sunnyWeatherMovementCosts.put(MoveType.TREAD,1);
        sunnyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        sunnyWeatherMovementCosts.put(MoveType.MECH,1);

        Map<MoveType,Integer> snowyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        snowyWeatherMovementCosts.put(MoveType.AIRY,2);
        snowyWeatherMovementCosts.put(MoveType.LANDER,1);
        snowyWeatherMovementCosts.put(MoveType.WHEEL,1);
        snowyWeatherMovementCosts.put(MoveType.TREAD,1);
        snowyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        snowyWeatherMovementCosts.put(MoveType.MECH,1);

        weatherMovementCosts.put(Weather.SUNNY,sunnyWeatherMovementCosts);
        weatherMovementCosts.put(Weather.RAINY,sunnyWeatherMovementCosts);
        // same movement costs if sun or rain
        weatherMovementCosts.put(Weather.SNOWY,snowyWeatherMovementCosts);
    }

    protected Beach() {
        super(name,0,0,0,weatherMovementCosts);
    }

    public static Beach get () {
        if (instance == null)
            instance = new Beach();
        return instance;
    }
}
