package fr.main.model.buildings;

import fr.main.model.Player;

public interface OwnableBuilding extends Building{

	public Player getOwner();
	public void setOwner(Player p);
	public boolean isNeutral();
	public int getIncome();
	public int getMaximumLife();
	public int getLife();
	public boolean removeLife(Player player, int life); // renvoie si le batiment a été pris (donc si sa vie est tombée sous 0)
	public void goBackToMaximumLife();

}