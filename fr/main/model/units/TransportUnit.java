package fr.main.model.units;

import java.util.ArrayList;

public interface TransportUnit<T extends AbstractUnit> extends AbstractUnit {

    public int getCapacity();
    public boolean isFull();
    public ArrayList<T> getUnits();
    public boolean charge(T u);
    public boolean remove(T u);
    public boolean canCharge(AbstractUnit u);
}