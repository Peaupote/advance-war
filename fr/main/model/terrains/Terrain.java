package fr.main.model.terrains;

import java.util.Map;

import fr.main.model.Weather;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.air.AirUnit;
import fr.main.model.buildings.Building;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.view.render.terrains.TerrainLocation;

public abstract class Terrain implements AbstractTerrain {

    protected final Map<Weather,Map<MoveType,Integer>> moveCost;
    protected TerrainLocation location;

    public final int height, defense;
    public final String name;

    protected Terrain(String name, int defense, int height, Map<Weather,Map<MoveType,Integer>> moveCost) {
        this.defense     = defense;
        this.height      = height;
        this.name        = name;
        this.moveCost    = moveCost;
        this.location    = null;
    }

    @Override
    public int getHeight(){
        return height;
    }

    @Override
    public int getDefense(AbstractUnit u) {
        return (u instanceof AirUnit)?0:defense;
    }

    @Override
    public int getBonusVision(AbstractUnit u) {
        return 0;
    }

    @Override
    public int getBonusRange(AbstractUnit u, PrimaryWeapon p) {
        return 0;
    }

    @Override
    public boolean hideFrom(AbstractUnit from) {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean canMoveIn(Weather w, MoveType moveType){
        if (w==Weather.FOGGY)
            w=Weather.SUNNY;
        for (MoveType mt : moveCost.get(w).keySet())
            if (moveType==mt)
                return true;
        return false;
    }

    @Override
    public boolean canStop(Weather w, MoveType mt){
        if (w==Weather.FOGGY)
            w=Weather.SUNNY;
        return canMoveIn(w,mt);
    }

    @Override
    public Integer moveCost(Weather w, MoveType moveType){
        if (w==Weather.FOGGY)
            w=Weather.SUNNY;
        for (Map.Entry<MoveType, Integer> entry : moveCost.get(w).entrySet())
            if (entry.getKey()==moveType)
                return entry.getValue();
        return null;
    }
}
