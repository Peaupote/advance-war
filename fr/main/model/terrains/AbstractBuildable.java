package fr.main.model.terrains;

import fr.main.model.buildings.Building;

public interface AbstractBuildable<T extends Building> extends AbstractTerrain{
	
	public T getBuilding();
	public void setBuilding(T b);

}