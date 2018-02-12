package fr.main.model.units;

import fr.main.model.buildings.OwnableBuilding;

public interface CaptureBuilding<T extends OwnableBuilding> extends AbstractUnit{

	/*
	* @return true if and only if the building was captured
	*/
	public boolean capture(T b);
}