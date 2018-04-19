package fr.main.model;

public class State implements java.io.Serializable{
    /**
     * Add StatController UID
     */
    private static final long serialVersionUID = 1900732638524129192L;

    public final int numberOfUnit,
                     numberOfBuilding,
                     numberOfFunds;

    public State (int unit, int building, int funds) {
        numberOfUnit     = unit;
        numberOfBuilding = building;
        numberOfFunds    = funds;
    }
}