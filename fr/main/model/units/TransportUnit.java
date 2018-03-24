package fr.main.model.units;

import java.util.HashSet;

public interface TransportUnit extends AbstractUnit {

    public int getCapacity();
    public boolean isFull();
    public HashSet<AbstractUnit> getUnits();
    public boolean charge(AbstractUnit u);
    public boolean canRemove(AbstractUnit u, int x, int y);
    public boolean remove(AbstractUnit u, int x, int y);
    public boolean canCharge(AbstractUnit u);
}