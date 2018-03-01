package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.terrains.Terrain;

import fr.main.model.units.MoveType;
import fr.main.model.units.AbstractUnit;

public class Bridge extends Terrain implements LandTerrain{

    private static Bridge instance;
    public static final String name="Pont";
    protected static final Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();

    static{
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        sunnyWeatherMovementCosts.put(MoveType.MECH,1);
        sunnyWeatherMovementCosts.put(MoveType.WHEEL,1);
        sunnyWeatherMovementCosts.put(MoveType.TREAD,1);
        sunnyWeatherMovementCosts.put(MoveType.NAVAL,1);
        sunnyWeatherMovementCosts.put(MoveType.LANDER,1);        
    }

    protected Bridge() {
        super(name,0,0,0, sunnyWeatherMovementCosts);
    }

    public static Bridge get () {
        if (instance == null) instance = new Bridge();
        return instance;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean canStop(MoveType mt){
        return mt!=MoveType.NAVAL && mt!=MoveType.LANDER && super.canStop(mt);
    }
}