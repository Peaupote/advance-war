package fr.main.model.terrains.land;

import fr.main.model.units.Unit;
import fr.main.model.terrains.Buildable;
import fr.main.model.buildings.Building;
import fr.main.model.terrains.Terrain;

public class Hill extends Buildable implements LandTerrain {

  private static Hill instance;

  protected Hill() {
    super("Colline", 2, 0, 0);
  }

  public static Hill get () {
    if (instance == null) instance = new Hill();
    return instance;
  }
}
