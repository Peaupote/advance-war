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
	public boolean canFire(int x, int y) {
		return false; // WIP
	}
	public void fire(int x, int y) {
		if(!canFire(x, y)) return;
		fired = true;
		// TODO: implémenter le missile.
	}
}
