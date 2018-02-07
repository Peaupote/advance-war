package fr.main.model.buildings;

import fr.main.model.terrains.Terrain;

public class MissileLauncher extends Building implements ActionBuilding, IndestructibleBuilding {

	private boolean fired;

	public MissileLauncher(Terrain t) {
		super(t, 2);
		this.fired = false;
	}

	public boolean isFired() {
		return fired;
	}
	public void fire() {
		fired = true;
		// TODO: implémenter le missile.
	}
}
