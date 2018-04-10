package fr.main.model.commanders;

import fr.main.model.players.Player;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.units.AbstractUnit;

/**
 * A commander whose power is to earn money
 */
public class MoneyCommander extends Commander{
	/**
	 * Add MoneyCommander
	 */
	private static final long serialVersionUID = 2721532289936600221L;

	public MoneyCommander(Player player){
		super(player, new Power(1000, p -> {
			for (OwnableBuilding o : p.buildingList())
				p.addFunds(o.getIncome() * 2 / 3);
		}), new Power(2000, p -> {
			for (OwnableBuilding o : p.buildingList()){
				p.addFunds(o.getIncome() * 3 / 2);
			}
		}));
	}

    public int getAttackValue(AbstractUnit u){
        return activated(true) ? 110 : (activated(false) ? 105 : 100);
    }

    public int getDefenseValue(AbstractUnit u){
        return activated(true) ? 110 : (activated(false) ? 105 : 100);
    }

    public String toString () {
      return "money";
    }
}
