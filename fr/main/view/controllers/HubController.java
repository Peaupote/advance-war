package fr.main.view.controllers;

import fr.main.network.*;
import fr.main.view.views.HubView;

public class HubController extends Controller {

  private Client client;

  public static class Slot {

    String name;
    int commander;

  }

  private Slot[] slots;

  public static class Host extends HubController {

    private Server server;

    public Host (Client client, Server server) {
      super(client);
      this.server = server;
    }

    public HubView.Host makeView() {
      return new HubView.Host(this);
    }

    public String getAddress () {
      return server.getInetAddress().getHostAddress();
    }

  }

  public HubController (Client client) {
    this.client = client;
    slots = new Slot[4];
  }

  public HubView makeView () {
    return new HubView(this);
  }

  public String getAddress () {
    return client.getInetAddress().getHostAddress();
  }

}
