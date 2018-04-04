package fr.main.model.units;

/**
 * Enum to represent the move type of an unit
 */
public enum MoveType {

	AIRY("Aérien"),
	NAVAL("Naval"),
	LANDER("Barge"),
	WHEEL("Roues"),
	TREAD("Chenilles"),
	INFANTRY("Infanterie"),
	MECH("Bazooka");
	
	MoveType(String s){
		name=s;
	}

	private final String name;

	public String toString(){
		return name;
	}
}