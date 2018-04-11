package fr.main.network;

import java.io.IOException;

class AdwProtocol {

  enum State {
    HUB,

    GAME
  };

  private State state;
  private Server.ClientThread client;

  public AdwProtocol (Server.ClientThread client) {
    state = State.HUB;
    this.client = client;
  }

  public void proccessInput (Object data) throws IOException {
    assert data instanceof Datagram;
    Datagram d = (Datagram)data;
    if (state == State.HUB) proccessHubInput(d);
  }

  private void proccessHubInput (Datagram data) throws IOException {
    if (data.data instanceof Slot) {
      client.slot = (Slot)data.data;
      client.sendAll (data);
    }
  }

}
