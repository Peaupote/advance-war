package fr.main.model.terrains.land;

import fr.main.model.terrains.Terrain;
import fr.main.model.units.Unit;

public class River extends Terrain implements LandTerrain {

  public River() {
    super(0,0,0,false);
  }

  public String toString () {
    return "Rivière";
  }
}
