package fr.main.view.controllers;

import java.awt.event.*;
import javax.swing.JOptionPane;
import fr.main.view.views.*;
import fr.main.view.MainFrame;
import fr.main.network.*;

/**
 * Main Menu
 */
public class MenuController extends Controller {
	
  public final ActionListener play,
                              exit,
                              host,
                              join;

  public MenuController () {
    super();

    play = (ActionEvent e) -> MainFrame.setScene(new CreateController());

    exit = (ActionEvent e) -> System.exit(0);

    host = e -> {
      try {
        Server server = new Server(8080);
        new Thread(server::listen).start();

        Client client = new Client("localhost", 8080);
        new Thread(client::listen).start();

        MainFrame.setScene(new HubController.Host(client, server));
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        System.err.println(ex);
        ex.printStackTrace();
      }
    };
    join = e -> MainFrame.setScene(new ConnectionController());
  }

  public View makeView () {
    return new MenuView(this);
  }

}
