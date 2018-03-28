package fr.main.view.controllers;

import java.awt.event.*;
import fr.main.view.views.*;
import fr.main.model.Player;
import fr.main.view.MainFrame;

public class MenuController extends Controller {
	
  public final ActionListener play, exit, sound;

  public MenuController () {
    super();

    Player[] ps = new Player[]{
      new Player("P1"),
      new Player("P2")
    };
    play = (ActionEvent e) -> MainFrame.setScene(new LoadController(ps));
    exit = (ActionEvent e) -> System.exit(0);
    sound = (ActionEvent e) -> System.out.println("sound");
  }

  public void update () {}
  public View makeView () {
    return new MenuView(this);
  }

}
