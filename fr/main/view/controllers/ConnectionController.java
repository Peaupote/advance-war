package fr.main.view.controllers;

import java.awt.event.*;
import javax.swing.*;

import fr.main.view.views.ConnectionView;
import fr.main.view.MainFrame;
import fr.main.network.Client;

public class ConnectionController extends Controller {

  public static class Submit implements ActionListener {
  
    private final JTextField addr, port;

    public Submit (JTextField addr, JTextField port) {
      this.addr = addr;
      this.port = port;
    }

    public void actionPerformed (ActionEvent e) {
      try {
        Client client = new Client (addr.getText(),
                                    Integer.parseInt(port.getText()));
        MainFrame.setScene(new HubController(client));
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        System.err.println(ex);
      }
    }

  }

  public ConnectionView makeView () {
    return new ConnectionView(this);
  }

}
