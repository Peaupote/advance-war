package fr.main.model.terrains;

import java.awt.Graphics;

import fr.main.model.units.Unit;

public abstract class Terrain
  implements java.io.Serializable {

  public abstract int getDefense(Unit u);
  public abstract int getBonusVision(Unit u);
  public abstract int getBonusRange(Unit u);
  public abstract boolean isHiding(Unit u);

}
