package fr.main.model.terrains.land;

import fr.main.model.units.Unit;

public class Wood implements LandTerrain {

	private int defense = 2;

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
		return true;
	}

	public String toString () {
		return "Forêt";
	}
}
