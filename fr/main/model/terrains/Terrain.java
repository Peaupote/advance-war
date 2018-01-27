package fr.main.model.terrains;

import java.awt.Graphics;

import fr.main.model.units.Unit;
import fr.main.model.Drawable;

public abstract class Terrain
  implements java.io.Serializable, Drawable {

  public abstract int getDefense(Unit u);
  public abstract int getBonusVision(Unit u);
  public abstract int getBonusRange(Unit u);
  public abstract boolean isHiding(Unit u);

}
