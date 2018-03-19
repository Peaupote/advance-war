package fr.main.model.commanders;

import fr.main.model.Player;

@FunctionalInterface
public interface PowerAction extends java.io.Serializable {
	public void activate(Player p);
}