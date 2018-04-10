package fr.main.model.buildings;

import fr.main.model.units.AbstractUnit;
import fr.main.model.players.Player;

/**
  * Represents buildings that can repair and replenish specific units
  */
@SuppressWarnings("serial")
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
                // if the building is owned, the owner pays
                Player p = ((OwnableBuilding)this).getOwner();

                // the building repairs between 0 and 20 life points
                // the building cannot add life such as the unit has more than 100 life points
                // one life point costs 1/80 of the unit's cost
                // so the final formula is the one below :
                int pv   = Math.max(0, Math.min(Math.min(100 - u.getLife(), 20), 80 * p.getFunds() / u.getCost()));
                u.addLife(pv);
                p.spent(pv * u.getCost() / 80);
            } else u.addLife(20);
            u.getFuel().replenish();
            if (u.getPrimaryWeapon() != null) u.getPrimaryWeapon().replenish();
        }
    }
}
