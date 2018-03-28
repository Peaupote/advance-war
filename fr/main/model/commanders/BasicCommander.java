package fr.main.model.commanders;

import fr.main.model.Player;
import fr.main.model.units.AbstractUnit;

public class BasicCommander extends Commander{
	public BasicCommander(Player player){
		super(player, new Power(600), new Power(1200));
	}

    public int getAttackValue(AbstractUnit u){
        return activated(true) ? 130 : (activated(false) ? 115 : 100);
    }

    public int getDefenseValue(AbstractUnit u){
        return activated(true) ? 130 : (activated(false) ? 115 : 100);
    }

    public String toString() {
      return "basic";
    }
}
