package fr.main.model.buildings;

import fr.main.model.terrains.Terrain;

public class MissileLauncher extends Building implements ActionBuilding, IndestructibleBuilding {

	private boolean fired;

	public MissileLauncher(Terrain t) {
		super("Silo à missile", 2, 0, t);
		this.fired = false;
	}

	public boolean isFired() {
		return fired;
	}
	public void fire() {
		fired = true;
		// WIP -> implémenter le missile.
	}
}