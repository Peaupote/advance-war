package fr.main.model.buildings;

import fr.main.model.Player;
import fr.main.model.terrains.Terrain;

public class Barrack extends OwnableBuilding implements FactoryBuilding {

	public static final int defense     = 3;
	public static final int income      = 1000;
	public static final String name     = "Caserne";
	public static final int maximumLife = 200;

	public Barrack(Terrain t, Player p){
		super(t, defense, p, maximumLife, income, name);
	}

}
