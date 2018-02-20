package fr.main.model.terrains.land;

import java.util.Map;
import java.util.HashMap;

import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.terrains.Buildable;
import fr.main.model.buildings.Building;
import fr.main.model.buildings.Dock;
import fr.main.model.terrains.Terrain;

public class Lowland extends Buildable implements LandTerrain {
    
    private static Lowland instance;
    public static final String name="Plaine";
    protected static final Map<MoveType,Integer> sunnyWeatherMovementCosts=new HashMap<MoveType,Integer>();

    static{
        sunnyWeatherMovementCosts.put(MoveType.AIRY,1);
        sunnyWeatherMovementCosts.put(MoveType.WHEEL,2);
        sunnyWeatherMovementCosts.put(MoveType.TREAD,1);
        sunnyWeatherMovementCosts.put(MoveType.INFANTRY,1);
        sunnyWeatherMovementCosts.put(MoveType.MECH,1);
    }

    protected Lowland() {
        super(name, 1, 0, 0, sunnyWeatherMovementCosts);
    }

    public static Lowland get () {
        if (instance == null)
            instance = new Lowland();
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
            return 1;
        else
            return super.moveCost(mt);
    }
}
