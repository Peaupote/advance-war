package fr.main.view.controllers;

import java.awt.event.*;
import fr.main.view.views.*;
import fr.main.view.MainFrame;

public class MenuController extends Controller {
	
  public final ActionListener play, exit, sound;

  public MenuController () {
    super();

    play = (ActionEvent e) -> MainFrame.setScene(new CreateController());
    exit = (ActionEvent e) -> System.exit(0);
    sound = (ActionEvent e) -> System.out.println("sound");
  }

  public View makeView () {
    return new MenuView(this);
  }

}
