package fr.main.view.controllers;

import java.awt.event.ActionListener;
import java.io.IOException;

import fr.main.model.players.Player;
import fr.main.view.MainFrame;
import fr.main.view.views.CreateView;

/**
 * Controller to choose each commanders for each players.
 */
public class CreateController extends Controller {

  private CreateView view;
  public final ActionListener select,
                              play;

  public CreateController () {
    select = e -> view.players.updateCommander();
    play = e -> {
      Player[] ps = view.players.getPlayers();
      MainFrame.setScene(new MapController(ps));
    };
  }

  public CreateView makeView() {
    try {
      view = new CreateView(this);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return view;
  }

}
