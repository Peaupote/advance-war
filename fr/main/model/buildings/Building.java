package fr.main.model.buildings;

import java.io.Serializable;

public class Building implements Serializable, AbstractBuilding{
	public final String name;
	public final int defense;

	public Building(String name, int defense) {
		this.name = name;
		this.defense = defense;
	}
	public String toString() {
		return name;
	}

}