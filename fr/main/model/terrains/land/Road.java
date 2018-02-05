package fr.main.model.terrains.land;

import fr.main.model.units.Unit;
import fr.main.model.terrains.Terrain;

public class Road extends Terrain implements LandTerrain {
  public Road() {
    super(0,0,0,false);
  }

  public String toString () {
    return "Route";
  }
}
