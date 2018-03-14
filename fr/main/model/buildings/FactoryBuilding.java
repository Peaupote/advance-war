package fr.main.model.buildings;

import java.util.Set;

import fr.main.model.units.AbstractUnit;

public interface FactoryBuilding<T extends AbstractUnit> extends ActionBuilding{
    public Set<Class<? extends T>> getUnitList();
    public boolean create(Class<? extends T> c);
}

