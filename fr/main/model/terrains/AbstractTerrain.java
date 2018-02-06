package fr.main.model.terrains;

import fr.main.model.units.Unit;

public interface AbstractTerrain extends java.io.Serializable {

    public int getDefense(Unit u);
    public int getBonusVision(Unit u);
    public int getBonusRange(Unit u);
    public boolean isHiding(Unit u);
    public Unit getUnit();
    public String toString();

}