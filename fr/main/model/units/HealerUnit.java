package fr.main.model.units;

import fr.main.model.Universe;
import fr.main.model.Direction;

/**
 * Interface used to represent the units that can heal other units
 */
public interface HealerUnit extends AbstractUnit {

    /**
     * @param AbstractUnit the unit to heal
     */
    default void heal(AbstractUnit u){
        if (canHeal(u))
            if (u.getPlayer().spent(u.getCost()/5)){
                u.addLife(10);
                setMoveQuantity(0);
            }
    }

    /**
     * @param AbstractUnit the unit we want to heal
     * @return true if and only if the unit can be healed
     */
    default boolean canHeal(AbstractUnit u){
        return u != null && u.getPlayer() == getPlayer() && isEnabled();
    }

    /**
     * @return true if there is an unit around the position that can be healed
     */
    default boolean canHeal(){
        Universe u = Universe.get();
        if (this.getMoveQuantity() != 0){
            for (Direction d : Direction.cardinalDirections()){
                int xx = getX() + d.x, yy = getY() + d.y;
                if (u.isValidPosition(xx, yy) && canHeal(u.getUnit(xx, yy)))
                    return true;
            }
        }
        return false;
    }
}