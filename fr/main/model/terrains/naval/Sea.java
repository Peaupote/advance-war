package fr.main.model.terrains.naval;

import fr.main.model.terrains.Terrain;
import fr.main.model.units.Unit;

public class Sea extends Terrain implements NavalTerrain {
  public Sea () {
    super("Mer",1, 0, 0, false);
  }
}
