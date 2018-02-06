package fr.main.model.terrains;

import fr.main.model.units.Unit;
import fr.main.model.buildings.Building;

public interface Terrain extends java.io.Serializable {

    public int getDefense(Unit u);
    public int getBonusVision(Unit u);
    public int getBonusRange(Unit u);
    public boolean isHiding(Unit u);
}
