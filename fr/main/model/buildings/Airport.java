package fr.main.model.buildings;

import fr.main.model.Player;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.air.AirUnit;

public class Airport extends OwnableBuilding implements FactoryBuilding {

	public static final int defense     = 3;
	public static final int income      = 1000;
	public static final String name     = "Aéroport";
	public static final int maximumLife = 200;

	public Airport(Terrain t, Player p){
		super(t, defense, p, maximumLife, income, name);
	}

}
