package fr.main.model.terrains.land;

import fr.main.model.units.Unit;
import fr.main.model.terrains.Terrain;

public class Mountain extends Terrain implements LandTerrain {

  public Mountain () {
    super("Montagne", 4, 3, 2);
  }
}
