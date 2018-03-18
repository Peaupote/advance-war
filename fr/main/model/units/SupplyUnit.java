package fr.main.model.units;

import fr.main.model.Universe;
import fr.main.model.Direction;

public interface SupplyUnit extends AbstractUnit {

    public boolean canSupply(AbstractUnit u);
    
    public default boolean canSupply(){
        Universe universe=Universe.get();
        for (Direction d : Direction.cardinalDirections())
            if (canSupply(universe.getUnit(getX()+d.x,getY()+d.y)))
                return true;
        return false;
    }

    public default void supply(){
        Universe universe=Universe.get();
        for (Direction d : Direction.cardinalDirections()){
            AbstractUnit u=universe.getUnit(getX()+d.x,getY()+d.y);
            if (canSupply(u))
                u.getFuel().replenish();
        }
        this.setMoveQuantity(0);
    }

}