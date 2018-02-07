package fr.main.model.buildings;

import fr.main.model.Player;

public class Headquarter extends OwnableBuilding {

	public static final int defense=4;
	public static final int income=1000;
	public static final String name="QG";
	public static final int maximumLife=200;

	public Headquarter(Player p){
		super(p,maximumLife,defense,income,name);
	}

	public Headquarter(){
		this(null);
	}
}