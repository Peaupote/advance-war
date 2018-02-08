package fr.main.model.terrains.land;

import fr.main.model.units.Unit;
import fr.main.model.terrains.naval.NavalTerrain;
import fr.main.model.terrains.Terrain;

public class Beach extends Terrain implements LandTerrain,NavalTerrain {
  
  private static Beach instance;

	protected Beach() {
		super("Plage",0,0,0);
	}

  public static Beach get () {
    if (instance == null) instance = new Beach();
    return instance;
  }
}
