package fr.main.model.terrains.land;

import fr.main.model.units.Unit;
import fr.main.model.units.land.LandUnit;
import fr.main.model.terrains.Terrain;

public class Wood extends Terrain implements LandTerrain {

	public Wood() {
		super("Forêt",2, 0, 0);
	}

	@Override
	public boolean isHiding(Unit u){
		return u instanceof LandUnit;
	}

}
