package fr.main.model.terrains.land;

import fr.main.model.terrains.Terrain;
import fr.main.model.units.Unit;

public class River extends Terrain implements LandTerrain {

  private static River instance;

  protected River() {
    super("Rivière",0,0,0);
  }

  public static River get () {
    if (instance == null) instance = new River();
    return instance;
  }

}
