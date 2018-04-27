package fr.main.model.units;

import fr.main.model.Universe;
import fr.main.model.Direction;

/**
 * Interface used to represent the units that can replenish other units
 */
@SuppressWarnings("serial")
public interface SupplyUnit extends AbstractUnit {

    /**
     * @param u is the unit we want to replenish
     * @return true if and only if the unit can be replenished
     */
    public boolean canSupply(AbstractUnit u);
    
    /**
     * @return true if there is an unit around the position that can be replenished
     */
    public default boolean canSupply(){
        Universe universe=Universe.get();
        for (Direction d : Direction.cardinalDirections())
            if (canSupply(universe.getUnit(getX()+d.x,getY()+d.y)))
                return true;
        return false;
    }

    /**
     * supply all units around
     */
    public default void supply(){
        Universe universe=Universe.get();
        for (Direction d : Direction.cardinalDirections()){
            AbstractUnit u = universe.getUnit(getX()+d.x,getY()+d.y);
            if (canSupply(u)){
                u.getFuel().replenish();
                if (u.getPrimaryWeapon() != null)
                    u.getPrimaryWeapon().replenish();
            }
        }
        this.setMoveQuantity(0);
    }

}