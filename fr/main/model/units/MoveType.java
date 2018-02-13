package fr.main.model.units;

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