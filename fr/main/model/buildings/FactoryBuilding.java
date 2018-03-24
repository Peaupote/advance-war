package fr.main.model.buildings;

import java.util.Set;

import fr.main.model.units.AbstractUnit;

public interface FactoryBuilding{
    public Set<Class<? extends AbstractUnit>> getUnitList();
    public boolean create(Class<? extends AbstractUnit> c);
}

