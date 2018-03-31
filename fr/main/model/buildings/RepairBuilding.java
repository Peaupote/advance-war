package fr.main.model.buildings;

import fr.main.model.units.AbstractUnit;
import fr.main.model.players.Player;

/**
  * Represents buildings that can repair and replenish specific units
  */
public interface RepairBuilding extends AbstractBuilding{
    /**
     * @param u is the unit we want to repair
     * @return true if and only if the building can repair the unit given in parameter
     */
    public boolean canRepair(AbstractUnit u);

    /**
     * @param u is the unit to repair
     * Repair the unit given in parameter
     */
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
