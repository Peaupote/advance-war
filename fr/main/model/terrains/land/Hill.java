package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.terrains.Buildable;
import fr.main.model.buildings.Building;
import fr.main.model.buildings.Dock;
import fr.main.model.terrains.Terrain;

public class Hill extends Buildable implements LandTerrain {

    private static Hill instance;
    protected static final Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();
    public static final String name="Colline";

    static{
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.WHEEL,3);
        sunnyWeatherMovementCosts.put(MoveType.TREAD,2);
        sunnyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        sunnyWeatherMovementCosts.put(MoveType.MECH,1);
    }

    protected Hill() {
        super(name, 2, 0, 0, sunnyWeatherMovementCosts);
    }

    public static Hill get () {
        if (instance == null)
            instance = new Hill();
        return instance;
    }

    @Override
    public boolean canStop(MoveType mt){
        if (getBuilding() instanceof Dock && (mt==MoveType.NAVAL || mt==MoveType.LANDER))
            return true;
        else
            return super.canStop(mt);
    }

    @Override
    public boolean canMoveIn(MoveType mt){
        if (getBuilding() instanceof Dock && (mt==MoveType.NAVAL || mt==MoveType.LANDER))
            return true;
        else
            return super.canMoveIn(mt);        
    }

    @Override
    public Integer moveCost(MoveType mt){
        if (getBuilding() instanceof Dock && (mt==MoveType.NAVAL || mt==MoveType.LANDER))
            return 2;
        else
            return super.moveCost(mt);
    }
}
