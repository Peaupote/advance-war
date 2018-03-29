package fr.main.model.commanders;

import fr.main.model.Player;

/**
 * A commander with no power and no bonus of any sort
 */
public class FakeCommander extends Commander {

  public FakeCommander (Player player) {
    super(player, new Power(1000), new Power(2000));
  }

}
