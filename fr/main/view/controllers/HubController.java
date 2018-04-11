package fr.main.view.controllers;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import fr.main.network.Client;
import fr.main.network.Datagram;
import fr.main.network.Server;
import fr.main.network.Slot;
import fr.main.view.views.HubView;

public class HubController extends Controller {

  private Client client;
  private HubView view;


  public Slot[] slots;

  public final ActionListener send, readyAction;
  private boolean ready;

  public static class Host extends HubController {

    private Server server;

    public Host (Client client, Server server) {
      super(client);
      this.server = server;
    }

    public HubView makeView() {
      super.view = new HubView.Host(this);
      return super.view;
    }

    public String getAddress () {
      return server.getInetAddress().getHostAddress();
    }

  }

  public HubController (Client client) {
    this.client = client;
    slots = new Slot[4];

    send = e -> send(slot());
    readyAction = e -> {
      JButton source = (JButton)e.getSource();
      if (ready) source.setText("Ready to play");
      else source.setText("Not Ready to play");
      ready = !ready;
    };

    new Thread(() -> {
      while (view == null) System.out.println("wait for view");
      try {
        Object data;
        while ((data = client.read()) != null) {
          if (data instanceof Datagram &&
              ((Datagram)data).data instanceof Slot) {
                Datagram d = (Datagram)data;
            System.out.println(d.data);
                slots[d.id] = (Slot)d.data;
                view.update ();
              }
        }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog (null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
      }
    }).start();
  }

  private void send (Object data) {
    try {
      client.send(data);
    } catch (IOException ex) {
      JOptionPane.showMessageDialog (null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      System.err.println(ex);
    }
  }

  private Slot slot () {
    return new Slot (client.id,
                     view.getName(),
                     ready);
  }

  public HubView makeView () {
    view = new HubView(this);
    return view;
  }

  public String getAddress () {
    return client.getInetAddress().getHostAddress();
  }

  public int getID () {
    return client.id;
  }

}
