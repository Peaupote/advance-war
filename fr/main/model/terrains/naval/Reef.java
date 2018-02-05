package fr.main.model.terrains.naval;

import fr.main.model.units.Unit;
import fr.main.model.terrains.Terrain;

public class Reef extends Terrain implements NavalTerrain {

  public Reef () {
    super("Récif", 2, 0, 0, true);
  }

}
