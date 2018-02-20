package fr.main.model.commanders;

import fr.main.model.Player;

@FunctionalInterface
public interface PowerAction{
	public void activate(Player p);
}
