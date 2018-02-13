package fr.main.model.terrains;

import java.util.Map;

import fr.main.model.buildings.Building;
import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;

/*
* This class is a basic terrain on which can be built any building.
*/
public abstract class Buildable extends Terrain implements AbstractBuildable<Building> {
    protected Building building;

    public Buildable (String name, int defense, int bonusVision, int bonusRange, Map<MoveType,Integer> sunny) {
        super(name, defense, bonusVision, bonusRange,sunny);
        this.building = null;
    }

    public void setBuilding(Building building){
        this.building = building;
    }

    public Building getBuilding(){
        return building;
    }

    @Override
    public int getDefense (Unit u) {
        return building==null ? defense : building.getDefense() + defense/2;
    }
}
