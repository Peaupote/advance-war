package fr.main.model.terrains.land;

import fr.main.model.units.Unit;
import fr.main.model.terrains.naval.NavalTerrain;
import fr.main.model.terrains.Terrain;

public class Beach extends Terrain implements LandTerrain,NavalTerrain {
	public Beach() {
		super("Plage",0,0,0);
	}
}
