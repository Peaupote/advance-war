package fr.main.model;

public class State implements java.io.Serializable{
    /**
     * Add Unit UID
     */
    private static final long serialVersionUID = 1597346551032795032L;

	public final int numberOfUnit,
                     numberOfBuilding,
                     numberOfFunds;

    public State (int unit, int building, int funds) {
        numberOfUnit     = unit;
        numberOfBuilding = building;
        numberOfFunds    = funds;
    }
}