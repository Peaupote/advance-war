package fr.main.model.units;

import java.util.LinkedList;

public interface TransportUnit<T extends AbstractUnit> extends AbstractUnit {

	int getCapacity();
	boolean isFull();
	LinkedList<T> getUnits();
	void charge(T u);

}

