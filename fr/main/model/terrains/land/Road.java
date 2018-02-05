package fr.main.model.terrains.land;

import fr.main.model.units.Unit;

public class Road implements LandTerrain {

  public static final int defense = 0;

  public int getDefense (Unit u) {
    return defense;
  }

  public int getBonusVision (Unit u) {
    return 0;
  }

  public int getBonusRange (Unit u) {
    return 0;
  }

  public boolean isHiding (Unit u) {
    return false;
  }

  public String toString () {
    return "Route";
  }
}
