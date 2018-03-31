package fr.main.model.buildings;

import java.util.Set;

import fr.main.model.units.AbstractUnit;

/**
 * Interface representing a building that can create units
 */
public interface FactoryBuilding extends AbstractBuilding{
    /**
     * @return the set of classes of units that can be created by the building
     */
    public Set<Class<? extends AbstractUnit>> getUnitList();

    /**
     * @param c is the class of the unit to be created
     */
    public boolean create(Class<? extends AbstractUnit> c);
}

