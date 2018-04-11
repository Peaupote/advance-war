package fr.main.model.commanders;

import fr.main.model.Universe;
import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;

/**
 * A commander whose power is to remove life of all opponent units
 */
public class DestroyCommander extends Commander{
	/**
	 * Add DestroyCommander
	 */
	private static final long serialVersionUID = 3536665169061833228L;

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

    public String toString () {
      return "destroy";
    }
}
