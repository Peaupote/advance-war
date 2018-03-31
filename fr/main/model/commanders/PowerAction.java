package fr.main.model.commanders;

import fr.main.model.players.Player;

/**
 * The action done when a commander's power is activated
 */
@FunctionalInterface
public interface PowerAction extends java.io.Serializable {
	/**
	 * @param p is the player who activates the power
	 */
	public void activate(Player p);
}