package fr.main.model.units;

import java.util.LinkedList;

import fr.main.model.units.AbstractUnit;

public interface TransportUnit<T extends AbstractUnit> extends AbstractUnit {

	public int getCapacity();
	public boolean isFull();
	public LinkedList<T> getUnits();
	public void charge(T u);

}

