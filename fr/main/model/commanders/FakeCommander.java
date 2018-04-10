package fr.main.model.commanders;

import fr.main.model.players.Player;

/**
 * A commander with no power and no bonus of any sort
 */
public class FakeCommander extends Commander {

  /**
	 * Add FakeCommander
	 */
	private static final long serialVersionUID = 6149992353986990767L;

public FakeCommander (Player player) {
    super(player, new Power(1000), new Power(2000));
  }

}
