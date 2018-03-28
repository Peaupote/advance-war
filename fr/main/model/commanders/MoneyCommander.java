package fr.main.model.commanders;

import fr.main.model.Player;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.units.AbstractUnit;

public class MoneyCommander extends Commander{
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
