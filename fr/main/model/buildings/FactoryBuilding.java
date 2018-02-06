package fr.main.model.buildings;

import fr.main.model.units.Unit;

import java.util.ArrayList;

public class FactoryBuilding<T extends AbstractUnit> extends Building implements ActionBuilding {
    public final ArrayList<T> unitList;

    public FactoryBuilding (String name, int defense, ArrayList) {
        super(name, defense);
        this.unitList = new ArrayList<>();
    }
    Unit[] listUnits() {
        return unitList;
    }
    boolean produce(Unit u);
}