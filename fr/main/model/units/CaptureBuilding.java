package fr.main.model.units;

import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.OwnableBuilding;

public interface CaptureBuilding extends AbstractUnit{

    /*
    * @return true if and only if the building was captured
    */
    public default boolean capture(AbstractBuilding b){
        if (canCapture(b)){
        	setMoveQuantity(0);
            return ((OwnableBuilding)b).removeLife(getPlayer(),getLife());
        }
        return false;
    }

    public boolean canCapture(AbstractBuilding b);
}