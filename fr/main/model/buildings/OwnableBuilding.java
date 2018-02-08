package fr.main.model.buildings;

import fr.main.model.Player;

public interface OwnableBuilding extends AbstractBuilding{

	Player getOwner();
	void setOwner(Player p);
	boolean isNeutral();
	int getIncome();
	int getMaximumLife();
	int getLife();
	boolean removeLife(Player player, int life); // renvoie si le batiment a été pris (donc si sa vie est tombée sous 0)
	void goBackToMaximumLife();
}