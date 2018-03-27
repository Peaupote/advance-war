package fr.main.model.commanders;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.units.AbstractUnit;

public class DestroyCommander extends Commander{
	public DestroyCommander(Player player){
		super(player, new Power(1000, p -> {
			for (Player pl : Universe.get().playerList())
				if (pl != p)
					for (AbstractUnit o : pl.unitList())
						o.removeLife(10);
		}), new Power(2000, p -> {
			for (Player pl : Universe.get().playerList())
				if (pl != p)
					for (AbstractUnit o : pl.unitList())
						o.removeLife(25);
		}));
	}

    public int getAttackValue(AbstractUnit u){
        return activated(true) ? 110 : (activated(false) ? 105 : 100);
    }

    public int getDefenseValue(AbstractUnit u){
        return activated(true) ? 110 : (activated(false) ? 105 : 100);
    }
}