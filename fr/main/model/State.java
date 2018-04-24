package fr.main.model;

public class State implements java.io.Serializable{

	public final int numberOfUnit,
                     numberOfBuilding,
                     numberOfFunds;

    public State (int unit, int building, int funds) {
        numberOfUnit     = unit;
        numberOfBuilding = building;
        numberOfFunds    = funds;
    }
}