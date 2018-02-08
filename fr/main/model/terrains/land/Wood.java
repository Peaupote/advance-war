package fr.main.model.terrains.land;

import fr.main.model.units.Unit;
import fr.main.model.units.land.LandUnit;
import fr.main.model.terrains.Terrain;

public class Wood extends Terrain implements LandTerrain {

  private static Wood instance;

	protected Wood() {
		super("Forêt",2, 0, 0);
	}

	@Override
	public boolean isHiding(Unit u){
		return u instanceof LandUnit;
	}

  public static Wood get () {
    if (instance == null) instance = new Wood();
    return instance;
  }

}
