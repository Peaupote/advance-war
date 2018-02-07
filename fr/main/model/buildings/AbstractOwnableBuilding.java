package fr.main.model.buildings;

import fr.main.model.Player;
import fr.main.model.units.Unit;

public interface AbstractOwnableBuilding extends Building{

	public Player getOwner();
	public void setOwner(Player p);
	public boolean isNeutral();
	public int getIncome();
	public int getMaximumLife();
	public int getLife();

	/*
	* @return true if the building was taken by the player (ie if its life fell to 0)
	*/
	public boolean removeLife(Player player, int life);

	/*
	* @return true if the building was taken by the unit (ie if its life fell to 0)
	*/
	public boolean removeLife(Unit u);
	public void goBackToMaximumLife();

}