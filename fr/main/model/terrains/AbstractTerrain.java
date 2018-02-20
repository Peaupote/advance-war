package fr.main.model.terrains;

import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;

public interface AbstractTerrain extends java.io.Serializable {

    public int getDefense(AbstractUnit u);
    public int getBonusVision(AbstractUnit u);
    public int getBonusRange(AbstractUnit u);
    public boolean hideFrom(AbstractUnit u);
	public boolean blockVision(AbstractUnit u);
	public boolean canMoveIn(AbstractUnit u);
	public boolean canMoveIn(MoveType mt);
	public boolean canStop(AbstractUnit u);
	public boolean canStop(MoveType mt);
	public Integer moveCost(AbstractUnit u);
	public Integer moveCost(MoveType mt);
}