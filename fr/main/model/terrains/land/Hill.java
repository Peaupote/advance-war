package fr.main.model.terrains.land;

import fr.main.model.units.Unit;
import fr.main.model.terrains.Buildable;
import fr.main.model.buildings.Building;
import fr.main.model.terrains.Terrain;

public class Hill extends Buildable implements LandTerrain {

  public Hill() {
    super("Colline", 2, 0, 0, false);
  }

}
