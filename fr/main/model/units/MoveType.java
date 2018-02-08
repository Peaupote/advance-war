package fr.main.model.units;

public enum MoveType{
	Airy("Aérien"),
	Naval("Naval"),
	Lander("Barge"),
	Wheel("Roues"),
	Track("Chenilles"),
	Infantry("Infanterie"),
	Mech("Bazooka");
	
	MoveType(String s){
		name=s;
	}

	private final String name;

	public String toString(){
		return name;
	}
}