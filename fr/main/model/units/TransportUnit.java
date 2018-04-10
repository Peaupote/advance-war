package fr.main.model.units;

import java.util.HashSet;

/**
 * Interface used to represent the units that can transport other units
 */
@SuppressWarnings("serial")
public interface TransportUnit extends AbstractUnit {

    /**
     * @return the number of units that can be transported
     */
    public int getCapacity();
    /**
     * @return the number of units that can still go in the transport
     */
    public default int getOccupation(){
    	return getUnits().size();
    }
    /**
     * @return true if and only if the transport is full
     */
    public boolean isFull();
    /**
     * @return the set of units in the transport
     */
    public HashSet<AbstractUnit> getUnits();
    /**
     * @param u is the unit we want to transport
     * @return true if and only if the unit is now in the transport
     */
    public boolean charge(AbstractUnit u);
    /**
     * @param u is the unit we want to remove from the transport
     * @param x is the horizontal coordinate of the tile we want to drop the unit on
     * @param y is the vertical coordinate of the tile we want to drop the unit on
     * @return true if and only if the unit can be removed from the transport
     */
    public boolean canRemove(AbstractUnit u, int x, int y);
    /**
     * @param u the unit we want to remove from the transport
     * @param x is the horizontal coordinate of the tile we want to drop the unit on
     * @param y is the vertical coordinate of the tile we want to drop the unit on
     * @return true if and only if the unit was removed from the transport
     */
    public boolean remove(AbstractUnit u, int x, int y);
    /**
     * @param u is the unit we want to charge in the transport
     * @return true if and only if the unit can be charged into the transport
     */
    public boolean canCharge(AbstractUnit u);
}