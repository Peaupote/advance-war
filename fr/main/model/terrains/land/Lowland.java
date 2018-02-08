package fr.main.model.terrains.land;

import fr.main.model.units.Unit;
import fr.main.model.terrains.Buildable;
import fr.main.model.buildings.Building;
import fr.main.model.terrains.Terrain;

public class Lowland extends Buildable implements LandTerrain {
  
  private static Lowland instance;

  protected Lowland() {
    super("Plaine", 1, 0, 0);
  }

  public static Lowland get () {
    if (instance == null) instance = new Lowland();
    return instance;
  }
}
