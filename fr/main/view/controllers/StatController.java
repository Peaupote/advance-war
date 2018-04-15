package fr.main.view.controllers;

import java.awt.event.ActionListener;

import fr.main.view.MainFrame;
import fr.main.model.players.Player;
import fr.main.model.State;
import fr.main.view.views.StatView;

public class StatController extends Controller {

  public final int[][] units, buildings;

  public final ActionListener menu;

  public StatController (int numberOfTurns, Player[] ps) {
    units     = new int[ps.length][numberOfTurns];
    buildings = new int[ps.length][numberOfTurns];

    for (int i = 0; i < ps.length; i++) {
      State[] stats = ps[i].getStats();
      for (int j = 0; j < stats.length; j++) {
        units[i][j]     = stats[j].numberOfUnit;
        buildings[i][j] = stats[j].numberOfBuilding;
      }

      for (int j = stats.length; j < numberOfTurns; j++) {
        units[i][j]     = stats[stats.length - 1].numberOfUnit;
        buildings[i][j] = stats[stats.length - 1].numberOfBuilding;
      }
    }

    menu = e -> MainFrame.setScene(new MenuController());
  }

  public StatView makeView () {
    return new StatView(this);
  }

}
