package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.land.LandUnit;
import fr.main.model.units.air.AirUnit;
import fr.main.model.terrains.Terrain;

public class Wood extends Terrain implements LandTerrain {

    private static Wood instance;
    public static final String name="Forêt";
    protected static final Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();

    static{
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.TREAD,2);
        sunnyWeatherMovementCosts.put(MoveType.WHEEL,3);
        sunnyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        sunnyWeatherMovementCosts.put(MoveType.MECH,1);
    }

    protected Wood() {
        super(name,2, 0, 0, sunnyWeatherMovementCosts);
    }

    @Override
    public boolean hideFrom(AbstractUnit from){
        return !AirUnit.class.isInstance(from);
    }

    @Override
    public boolean blockVision(AbstractUnit unit){
        return !AirUnit.class.isInstance(unit);
    }
    
    public static Wood get () {
        if (instance == null) instance = new Wood();
        return instance;
    }

}
