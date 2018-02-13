package fr.main.model.terrains;

import java.util.Map;

import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.buildings.Building;

public abstract class Terrain implements AbstractTerrain {

    protected final Map<MoveType,Integer> sunnyWeatherMovementCosts;

    protected int defense, bonusVision, bonusRange;
    protected String name;

    protected Terrain(String name, int defense, int bonusVision, int bonusRange, Map<MoveType,Integer> sunny) {
        this.defense     = defense;
        this.bonusRange  = bonusRange;
        this.bonusVision = bonusVision;
        this.name        = name;
        this.sunnyWeatherMovementCosts = sunny;
    }

    public int getDefense(Unit u) {
        return defense;
    }

    public int getBonusVision(Unit u) {
        return bonusVision;
    }
    
    public int getBonusRange(Unit u) {
        return bonusRange;
    }
    
    public boolean isHiding(Unit u) {
        return false;
    }
    
    @Override
    public String toString() {
        return name;
    }

    public boolean canMoveIn(Unit u){
        return canMoveIn(u.moveType);
    }

    public boolean canMoveIn(MoveType moveType){
        for (MoveType mt : sunnyWeatherMovementCosts.keySet())
            if (moveType==mt)
                return true;
        return false;
    }

    public int moveCost(Unit u){
        return moveCost(u.moveType);
    }

    public int moveCost(MoveType moveType){
        for (Map.Entry<MoveType, Integer> entry : sunnyWeatherMovementCosts.entrySet())
            if (entry.getKey()==moveType)
                return entry.getValue();
        throw new RuntimeException("Cannot move on this terrain");
    }
}