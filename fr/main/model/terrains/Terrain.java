package fr.main.model.terrains;

import fr.main.model.units.Unit;
import fr.main.model.buildings.Building;

public class Terrain implements java.io.Serializable {
    private Unit unit;
    private int defense, bonusVision, bonusRange;
    boolean hideable;

    public Terrain(int defense, int bonusVision, int bonusRange, boolean hideable) {
        this.defense = defense;
        this.bonusRange = bonusRange;
        this.bonusVision = bonusVision;
        this.hideable = hideable;
        this.unit = null;
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
}
