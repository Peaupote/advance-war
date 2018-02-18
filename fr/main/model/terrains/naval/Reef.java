package fr.main.model.terrains.naval;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.naval.NavalUnit;
import fr.main.model.units.air.AirUnit;
import fr.main.model.terrains.Terrain;

public class Reef extends Terrain implements NavalTerrain {

    private static Reef instance;
        protected static final Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();

    static{
        sunnyWeatherMovementCosts.put(MoveType.NAVAL,2);
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.LANDER,2);
    }

    protected Reef () {
        super("Récif", 2, 0, 0, sunnyWeatherMovementCosts);
    }

    @Override
    public boolean hideFrom(AbstractUnit from){
        return AirUnit.class.isInstance(from);
    }

    @Override
    public boolean blockVision(AbstractUnit unit){
        return !AirUnit.class.isInstance(unit);
    }

    public static Reef get () {
        if (instance == null) instance = new Reef();
        return instance;
    }

}
