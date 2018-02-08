package fr.main.model.terrains.naval;

import fr.main.model.terrains.Terrain;
import fr.main.model.units.Unit;

public class Sea extends Terrain implements NavalTerrain {

  private static Sea instance;

  protected Sea () {
    super("Mer",1, 0, 0);
  }

  public static Sea get () {
    if (instance == null) instance = new Sea();
    return instance;
  }

}
