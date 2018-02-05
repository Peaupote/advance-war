package fr.main.model.terrains.land;

import fr.main.model.units.Unit;
import fr.main.model.terrains.naval.NavalTerrain;

public class Beach implements LandTerrain,NavalTerrain {

	private int defense = 0;

	public int getDefense (Unit u) {
		return defense;
	}

	public int getBonusVision (Unit u) {
		return 0;
	}

	public int getBonusRange (Unit u) {
		return 0;
	}

	public boolean isHiding (Unit u) {
		return false;
	}

	public String toString () {
		return "Plage";
	}

	public boolean isLandTerrain(){
		return true;
	}

	public boolean isNavalTerrain(){
		return true;
	}
}
