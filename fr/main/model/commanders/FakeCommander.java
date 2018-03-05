package fr.main.model.commanders;

import fr.main.model.Player;

public class FakeCommander extends Commander {

  public FakeCommander (Player player) {
    super(player, new Power(1000), new Power(2000));
  }

}
