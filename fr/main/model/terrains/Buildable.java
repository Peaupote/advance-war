package fr.main.model.terrains;

import fr.main.model.buildings.Building;

public interface Buildable extends Terrain{
	
	public Building getBuilding();
	public void setBuilding(Building b);

}