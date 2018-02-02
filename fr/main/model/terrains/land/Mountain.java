package fr.main.model.terrains.land;

import fr.main.model.units.Unit;

public class Mountain implements LandTerrain {

  public static final int defense = 4;

  public int getDefense (Unit u) {
    return defense;
  }

  public int getBonusVision (Unit u) {
    return 3;
  }

  public int getBonusRange (Unit u) {
    return 2;
  }

  public boolean isHiding (Unit u) {
    return false;
  }

  public String toString () {
    return "Montagne";
  }
}
