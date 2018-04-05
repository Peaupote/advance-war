package fr.main.network;

import fr.main.model.players.Player;
import fr.main.model.commanders.BasicCommander;

/**
 * Representing each mode of the connection
 * between clients and the server.
 */
public class AdwProtocol {

  public static class Client {

    enum State {
      /**
       * Client waits the server to respond.
       */
      PROCESSING(0),

      /**
       * Client waiting while the other player
       * are doing stuffs.
       */
      WAITING(1),

      /**
       * The server is waiting for the player
       * to send his action.
       */
      PLAY(2),
      
      
      /**
       * Client must send the player his is.
       */
      PLAYER(3);
      
      public final int code;

      private State (int code) {
        this.code = code;
      }
    }
    
    private State state;

    public Client () {
      this.state = State.PLAYER;
    }

    public Object processing () {
      if (state == State.PLAYER) { 
        System.out.println("Player name:");
        Player p = new Player(System.console().readLine());
        new BasicCommander (p); 
        state = State.PROCESSING;
        return p; 
      }
      return System.console().readLine();
    }

    public void processInput (Object input) {
      if (input instanceof Exception) {
        System.err.println(((Exception)input).getMessage());
        state = State.PLAYER;
      }
    }

    public State getState () {
      return state;
    }

    public int getStatusCode () {
      return state.code;
    }
  }

  public static class Server {

    enum State {
      
      /**
       * Server waiting the client to send the player 
       * he is.
       */
      WAITING_PLAYER(0),

      /**
       * Server waiting the client to send his play.
       */
      WAITING_ACTION(1);

      public final int code;

      private State (int code) {
        this.code = code;
      }

    }

    private State state;
    private fr.main.network.Server.ClientThread server;

    public Server (fr.main.network.Server.ClientThread server) {
      state = State.WAITING_PLAYER;
      this.server = server;
    }

    public void processInput (Object input) throws Exception {
      if (state == State.WAITING_PLAYER) {
        if (input instanceof Player)
          server.setPlayer((Player) input);
        else throw new Exception ("Not a player");
      } else {
        throw new Exception ("Unknow Exception");
      }
    }

    public State getState () {
      return state;
    }

    public int getStatusCode () {
      return state.code;
    }

  }

}
