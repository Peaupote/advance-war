package fr.main.model.terrains;

import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;

public interface AbstractTerrain extends java.io.Serializable {

    public int getDefense(Unit u);
    public int getBonusVision(Unit u);
    public int getBonusRange(Unit u);
    public boolean isHiding(Unit u);
	public boolean canMoveIn(Unit u);
	public boolean canMoveIn(MoveType mt);
	public int moveCost(Unit u);
	public int moveCost(MoveType mt);
}