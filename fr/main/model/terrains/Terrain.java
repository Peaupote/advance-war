package fr.main.model.terrains;

import java.util.Map;

import fr.main.model.Weather;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.buildings.Building;

public abstract class Terrain implements AbstractTerrain {

    protected final Map<Weather,Map<MoveType,Integer>> moveCost;

    protected int defense, bonusVision, bonusRange;
    protected String name;

    protected Terrain(String name, int defense, int bonusVision, int bonusRange, Map<Weather,Map<MoveType,Integer>> moveCost) {
        this.defense     = defense;
        this.bonusRange  = bonusRange;
        this.bonusVision = bonusVision;
        this.name        = name;
        this.moveCost = moveCost;
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

    public boolean canMoveIn(Weather w, MoveType moveType){
        if (w==Weather.FOGGY)
            w=Weather.SUNNY;
        for (MoveType mt : moveCost.get(w).keySet())
            if (moveType==mt)
                return true;
        return false;
    }

    public boolean canStop(Weather w, MoveType mt){
        if (w==Weather.FOGGY)
            w=Weather.SUNNY;
        return canMoveIn(w,mt);
    }

    public Integer moveCost(Weather w, MoveType moveType){
        if (w==Weather.FOGGY)
            w=Weather.SUNNY;
        for (Map.Entry<MoveType, Integer> entry : moveCost.get(w).entrySet())
            if (entry.getKey()==moveType)
                return entry.getValue();
        return null;
    }
}