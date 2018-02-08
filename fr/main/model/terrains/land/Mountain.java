package fr.main.model.terrains.land;

import fr.main.model.units.Unit;
import fr.main.model.terrains.Terrain;

public class Mountain extends Terrain implements LandTerrain {

  private static Mountain instance;

  protected Mountain () {
    super("Montagne", 4, 3, 2);
  }

  public static Mountain get () {
    if (instance == null) instance = new Mountain();
    return instance;
  }

}
