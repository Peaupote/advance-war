package fr.main.model.buildings;

import fr.main.model.terrains.Terrain;

import java.io.Serializable;

public class Building implements Serializable, AbstractBuilding{
	public final String name;
	public final int defense;
	protected Terrain terrain;
	public final int maximumLife;
	protected int life;

	public Building(String name, int defense, int maximumLife, Terrain terrain) {
		this.name = name;
		this.defense = defense;
		this.maximumLife = maximumLife;
		this.life = maximumLife;
		this.terrain = terrain;
	}
	public Building(String name, int defense, int maximumLife) {
		this(name, defense, maximumLife, null);
	}
	public Building(String name, int defense) {
		this(name, defense, 100);
	}

	public String getName() {
		return name;
	}
	public int getX() {
		return 0; // WIP -> coordinates of the associated Terrain
	}
	public int getY() {
		return 0; // WIP -> coordinates of the associated Terrain
	}
	public Terrain getTerrain() {
		return terrain;
	}
	public int getDefense() {
		return defense;
	}
	public void setTerrain(Terrain t, boolean force) {
		if(this.terrain == null || force)
			this.terrain = t;
		return;
	}
	public void setTerrain(Terrain t) {
		setTerrain(t, false);
	}

}