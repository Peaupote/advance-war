package fr.main.model.units;

import fr.main.model.Universe;
import fr.main.model.Direction;

public interface HealerUnit extends AbstractUnit {

    default void heal(AbstractUnit u){
        if (canHeal(u))
            if (u.getPlayer().spent(u.getCost()/5)){
                u.addLife(10);
                setMoveQuantity(0);
            }
    }

    default boolean canHeal(AbstractUnit u){
        return u != null && u.getPlayer() == getPlayer() && isEnabled();
    }

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