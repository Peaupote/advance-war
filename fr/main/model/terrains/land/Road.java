package fr.main.model.terrains.land;

import fr.main.model.units.Unit;
import fr.main.model.terrains.Terrain;

public class Road extends Terrain implements LandTerrain {

  private static Road instance;

  protected Road() {
    super("Route",0,0,0);
  }

  public static Road get () {
    if (instance == null) instance = new Road();
    return instance;
  }

}
