package fr.main.model.commanders;

import fr.main.model.Player;

/**
 * The action done when a commander's power is activated
 */
@FunctionalInterface
public interface PowerAction extends java.io.Serializable {
	/**
	 * @param Player the player who activates the power
	 */
	public void activate(Player p);
}