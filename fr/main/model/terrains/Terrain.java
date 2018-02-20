package fr.main.model.terrains;

import java.util.Map;

import fr.main.model.units.AbstractUnit;
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

    public int getDefense(AbstractUnit u) {
        return defense;
    }

    public int getBonusVision(AbstractUnit u) {
        return bonusVision;
    }
    
    public int getBonusRange(AbstractUnit u) {
        return bonusRange;
    }
    
    public boolean hideFrom(AbstractUnit from) {
        return false;
    }

    public boolean blockVision(AbstractUnit u){
        return false;
    }
    
    @Override
    public String toString() {
        return name;
    }

    public boolean canMoveIn(AbstractUnit u){
        return canMoveIn(u.getMoveType());
    }

    public boolean canMoveIn(MoveType moveType){
        for (MoveType mt : sunnyWeatherMovementCosts.keySet())
            if (moveType==mt)
                return true;
        return false;
    }

    public boolean canStop(AbstractUnit u){
        return canStop(u.getMoveType());
    }

    public boolean canStop(MoveType mt){
        return canMoveIn(mt);
    }

    public Integer moveCost(AbstractUnit u){
        return moveCost(u.getMoveType());
    }

    public Integer moveCost(MoveType moveType){
        for (Map.Entry<MoveType, Integer> entry : sunnyWeatherMovementCosts.entrySet())
            if (entry.getKey()==moveType)
                return entry.getValue();
        return null;
    }
}