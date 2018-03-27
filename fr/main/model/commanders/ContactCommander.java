package fr.main.model.commanders;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.weapons.PrimaryWeapon;

public class ContactCommander extends Commander{
    public ContactCommander(Player player){
        super(player, new Power(800), new Power(1600));
    }

    public int getAttackValue(AbstractUnit u){
        if (u.getPrimaryWeapon() == null || u.getPrimaryWeapon().isContactWeapon())
            return activated(true) ? 150 : (activated(false) ? 120 : 100);
        else
            return 80;
    }

    public int getDefenseValue(AbstractUnit u){
        if (u.getPrimaryWeapon() == null || u.getPrimaryWeapon().isContactWeapon())
            return activated(true) ? 150 : (activated(false) ? 120 : 100);
        else
            return 80;
    }

    public int getVision(AbstractUnit u){
        return u.getBaseVision() + Universe.get().getTerrain(u.getX(), u.getY()).getBonusVision(u) + 1;
    }

    public int getMaximumRange(AbstractUnit u, PrimaryWeapon p){
        return p.isContactWeapon() ? 1 : p.getBaseMaximumRange() + Universe.get().getTerrain(u.getX(), u.getY()).getBonusRange(u, p) - 1;
    }
}