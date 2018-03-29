package fr.main.view.controllers;

import java.awt.event.*;
import javax.swing.*;

import fr.main.view.views.CreateView;
import fr.main.view.MainFrame;
import fr.main.model.Player;

/**
 * Controller to choose each commanders for each players
 *
 */
public class CreateController extends Controller {

  private CreateView view;
  public final ActionListener select,
                              play;

  public CreateController () {
    select = e -> view.players.updateCommander();
    play = e -> {
      Player[] ps = view.players.getPlayers();
      MainFrame.setScene(new LoadController(ps));
    };
  }

  public CreateView makeView() {
    view = new CreateView(this);
    return view;
  }

}
