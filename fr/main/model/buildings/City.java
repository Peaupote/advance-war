package fr.main.model.buildings;

import fr.main.model.Player;

public class City extends OwnableBuilding {

	public static final int defense=3;
	public static final int income=1000;
	public static final String name="Ville";
	public static final int maximumLife=200;

	public City(Player p){
		super(p,maximumLife,defense,income,name);
	}

	public City(){
		this(null);
	}
}