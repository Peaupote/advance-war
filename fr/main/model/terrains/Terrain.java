package fr.main.model.terrains;

import fr.main.model.units.Unit;
import fr.main.model.buildings.Building;

public abstract class Terrain implements java.io.Serializable {
    protected Unit unit;
    protected int defense, bonusVision, bonusRange;
    protected boolean hideable;
    protected String name;

    public Terrain(String name, int defense, int bonusVision, int bonusRange, boolean hideable) {
        this.defense = defense;
        this.bonusRange = bonusRange;
        this.bonusVision = bonusVision;
        this.hideable = hideable;
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

    @Override
    public String toString() {
        return name;
    }
}
