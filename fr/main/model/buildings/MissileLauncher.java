package fr.main.model.buildings;

public class MissileLauncher implements ActionBuilding {

	public static final int defense=2;
	public static final String name="Silo à missile";

	private boolean fired;

	{
		this.fired=false;
	}

	public String toString(){
		return name;
	}

	public int getDefense(){
		return this.defense;
	}
}