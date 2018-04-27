package fr.main.model.units;

import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.OwnableBuilding;

/**
 * Interface used to represent units that can capture a building
 */
@SuppressWarnings("serial")
public interface CaptureBuilding extends AbstractUnit{

    /**
     * @param b is the building we want to capture
     * @return true if and only if the building was captured
     */
    public default boolean capture(AbstractBuilding b){
        if (canCapture(b) && b.getX() == getX() && b.getY() == getY()){
        	setMoveQuantity(0);
            return ((OwnableBuilding)b).removeLife(getPlayer(), getLife());
        }
        return false;
    }

    /**
     * @param b is the building we want to capture
     * @return true if and only if the building can be captured
     */
    public boolean canCapture(AbstractBuilding b);
}