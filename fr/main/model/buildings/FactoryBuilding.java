package fr.main.model.buildings;

import fr.main.model.terrains.Terrain;
import fr.main.model.units.AbstractUnit;

import java.util.ArrayList;

public class FactoryBuilding<T extends AbstractUnit> extends Building implements ActionBuilding {
    public final ArrayList<T> unitList;

    public FactoryBuilding (String name, int defense, ArrayList<T> unitList) {
        super(name, defense);
        this.unitList = new ArrayList<>(unitList);
    }
    ArrayList<T> listUnits() {
        return unitList;
    }
    boolean produce(T u) {
        return false; // WIP -> Check if cell is occupied and generate an unit.
    }
}