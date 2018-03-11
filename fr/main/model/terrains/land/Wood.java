package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.Weather;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.land.LandUnit;
import fr.main.model.units.air.AirUnit;
import fr.main.model.terrains.Terrain;

public class Wood extends Terrain implements LandTerrain {

    private static Wood instance;
    public static final String name="Forêt";
    protected static final Map<Weather,Map<MoveType,Integer>> weatherMovementCosts=new HashMap<Weather,Map<MoveType,Integer>>();

    static{
        Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.TREAD,2);
        sunnyWeatherMovementCosts.put(MoveType.WHEEL,3);
        sunnyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        sunnyWeatherMovementCosts.put(MoveType.MECH,1);

        Map<MoveType,Integer> rainyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        rainyWeatherMovementCosts.put(MoveType.AIRY,1);
        rainyWeatherMovementCosts.put(MoveType.WHEEL,4);
        rainyWeatherMovementCosts.put(MoveType.TREAD,3);
        rainyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        rainyWeatherMovementCosts.put(MoveType.MECH,1);

        Map<MoveType,Integer> snowyWeatherMovementCosts=new HashMap<MoveType,Integer>();
        snowyWeatherMovementCosts.put(MoveType.AIRY,2);
        snowyWeatherMovementCosts.put(MoveType.WHEEL,4);
        snowyWeatherMovementCosts.put(MoveType.TREAD,3);
        snowyWeatherMovementCosts.put(MoveType.INFANTRY,2);
        snowyWeatherMovementCosts.put(MoveType.MECH,1);

        weatherMovementCosts.put(Weather.SUNNY,sunnyWeatherMovementCosts);
        weatherMovementCosts.put(Weather.RAINY,rainyWeatherMovementCosts);
        weatherMovementCosts.put(Weather.SNOWY,snowyWeatherMovementCosts);
    }

    protected Wood() {
        super(name, 2, 0, weatherMovementCosts);
    }

    @Override
    public boolean hideFrom(AbstractUnit from){
        return !AirUnit.class.isInstance(from);
    }

    public static Wood get () {
        if (instance == null) instance = new Wood();
        return instance;
    }

}
