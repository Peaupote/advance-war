package fr.main.model.buildings;

import fr.main.model.Player;

public class Airport extends OwnableBuilding implements FactoryBuilding {

	public static final int defense=3;
	public static final int income=1000;
	public static final String name="Aéroport";
	public static final int maximumLife=200;

	public Airport(Player p){
		super(p,maximumLife,defense,income,name);
	}

	public Airport(){
		this(null);
	}
}