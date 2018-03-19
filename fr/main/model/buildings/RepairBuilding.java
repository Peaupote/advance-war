package fr.main.model.buildings;

import fr.main.model.units.AbstractUnit;
import fr.main.model.Player;

/*
 * Represents buildings that can repair and replenish specific units
 */
public interface RepairBuilding<T extends AbstractUnit> {
    // example : the airport is the only building to repair air units ; it doesn't repair land unit
    public boolean canRepair(AbstractUnit u);

    public default void repair (AbstractUnit u){
        if (canRepair(u)){
            if (this instanceof OwnableBuilding){
                Player p = ((OwnableBuilding)this).getOwner();
                int pv   = Math.max(0, Math.min(Math.min(100 - u.getLife(), 20), 50 * p.getFunds() / u.getCost()));
                u.addLife(pv);
                p.spent(pv * u.getCost() / 50);
            } else u.addLife(20);
            u.getFuel().replenish();
        }
    }
}
