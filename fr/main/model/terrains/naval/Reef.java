package fr.main.model.terrains.naval;

import fr.main.view.MainFrame;
import fr.main.model.units.Unit;

public class Reef extends NavalTerrain {

  private int defense = 2;

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
    return true;
  }

  public String toString () {
    return "R";
  }

}
