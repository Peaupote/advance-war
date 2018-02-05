package fr.main.model.terrains;

import fr.main.model.buildings.Building;
import fr.main.model.units.Unit;

public abstract class Buildable extends Terrain {
    protected Building building;

    public Buildable (String name, int defense, int bonusVision, int bonusRange, boolean hideable) {
        super(name, defense, bonusVision, bonusRange, hideable);
        this.building = null;
    }

    public void setBuilding(Building building){
        this.building=building;
    }

    public Building getBuilding(){
        return this.building;
    }

    @Override
    public int getDefense (Unit u) {
        if (building==null)
            return this.defense;
        else
            return building.getDefense()+this.defense/2;
        // ainsi un batiment construit sur une plaine ne verra pas sa défense modifiée mais un batiment construit sur une colline aura +1 de défense
    }
}
