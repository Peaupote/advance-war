package fr.main.model.commanders;

import fr.main.model.Universe;
import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.weapons.PrimaryWeapon;

/**
 * A commander with good ranged units
 */
public class RangedCommander extends Commander{
    /**
	 * Add RangedCommander UID
	 */
	private static final long serialVersionUID = 1028124949092657216L;

	public RangedCommander(Player player){
        super(player, new Power(800), new Power(1600));
    }

    public int getAttackValue(AbstractUnit u){
        if (u.getPrimaryWeapon() == null || u.getPrimaryWeapon().isContactWeapon())
            return 80;
        else
            return activated(true) ? 180 : (activated(false) ? 130 : 100);
    }

    public int getDefenseValue(AbstractUnit u){
        if (u.getPrimaryWeapon() == null || u.getPrimaryWeapon().isContactWeapon())
            return 80;
        else
            return activated(true) ? 180 : (activated(false) ? 130 : 100);
    }

    public int getVision(AbstractUnit u){
        return u.getBaseVision() + Universe.get().getTerrain(u.getX(), u.getY()).getBonusVision(u) + 
                (u.getPrimaryWeapon() != null &&  !u.getPrimaryWeapon().isContactWeapon() ? 1 : 0) * (activated(true) ? 2 : (activated(false) ? 1 : 0));
    }

    public int getMaximumRange(AbstractUnit u, PrimaryWeapon p){
        return p.isContactWeapon() ? 1 : p.getBaseMaximumRange() + Universe.get().getTerrain(u.getX(), u.getY()).getBonusRange(u, p) + 1;
    }

    public String toString () {
      return "ranged";
    }
}
