package fr.main.model.terrains;

import fr.main.model.units.Unit;
import fr.main.model.buildings.Building;

public abstract class Terrain implements AbstractTerrain {
    protected Unit unit;
    protected int defense, bonusVision, bonusRange;
    protected String name;

    public Terrain(String name, int defense, int bonusVision, int bonusRange) {
        this.defense = defense;
        this.bonusRange = bonusRange;
        this.bonusVision = bonusVision;
        this.unit = null;
        this.name = name;
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
    public Unit getUnit() {
        return unit;
    }
    public void setUnit(Unit u) {
        this.unit = u;
    }
    public void removeUnit() {
        this.unit = null;
    }
    public boolean hasUnit() {
        return this.unit != null;
    }

    @Override
    public String toString() {
        return name;
    }
}
