package fr.main.model.commanders;

import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;

/**
 * A basic commander with no real power but to increase attack and defense of its units
 */
public class BasicCommander extends Commander{
	/**
	 * add BasicCommander
	 */
	private static final long serialVersionUID = 8930666798497469048L;

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
