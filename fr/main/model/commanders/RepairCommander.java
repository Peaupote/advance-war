package fr.main.model.commanders;

import fr.main.model.Player;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.units.AbstractUnit;

/**
 * A commander whose power is to repair its units
 */
public class RepairCommander extends Commander{
	public RepairCommander(Player player){
		super(player, new Power(1000, p -> {
			for (AbstractUnit o : p.unitList())
				o.addLife(15);
		}), new Power(2000, p -> {
			for (AbstractUnit o : p.unitList())
				o.addLife(40);
		}));
	}

    public int getAttackValue(AbstractUnit u) {
        return activated(true) ? 110 : (activated(false) ? 105 : 100);
    }

    public int getDefenseValue(AbstractUnit u) {
        return activated(true) ? 110 : (activated(false) ? 105 : 100);
    }

    public String toString () {
      return "repair";
    }
}
