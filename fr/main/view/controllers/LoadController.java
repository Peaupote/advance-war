package fr.main.view.controllers;

import java.util.Random;

import fr.main.view.views.LoadView;
import fr.main.model.players.Player;
import fr.main.view.MainFrame;

/**
 * Loading screen
 * TODO: load stuffs
 */
public class LoadController extends Controller {

  private int load;
  private Random rand;
  private final Player[] ps;

  public LoadController (Player[] ps) {
    this.ps = ps;
    load    = 0;
    rand    = new Random();
  }

  public void update() {
    load++;

    if (load == 100) MainFrame.setScene(new GameController(ps));
  }

  public int getLoad () {
    return load;
  }

  public LoadView makeView () {
    return new LoadView(this);
  }

}
