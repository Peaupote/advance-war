package fr.main.network;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

import fr.main.model.players.Player;

/**
 * Represent the server side.
 */
public class Server extends ServerSocket {

  /**
   * Can't be more than 4 players.
   */
  private static final int MAX_CLIENT_COUNT = 4;

  /**
   * Thread for each player.
   */
  private final ClientThread[] clients;

  /**
   * Number of connected clients
   * number of non-null ClientThread in clients.
   */
  private int connectedClients;

  /**
   * Connection with the last socket.
   */
  private Socket socket;

  class ClientThread extends Thread {

    /**
     * Receive messages from client.
     */
    private ObjectInputStream is;

    /**
     * Send messages to the client.
     */
    private ObjectOutputStream os;

    /**
     * Socket connection with the client.
     */
    private Socket socket;

    /**
     * Server side protocol.
     */
    private AdwProtocol.Server protocol;

    /**
     * Client's Player.
     */
    private Player player;

    public ClientThread(Socket socket) {
      this.socket = socket;
    }

    public void run () {
      try {
        is = new ObjectInputStream(socket.getInputStream());
        os = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("New Client: " + socket);
        protocol = new AdwProtocol.Server(this);

        synchronized (this) {
          Object line;
          while ((line = is.readObject()) != null) {
            try {
              protocol.processInput(line);
            } catch (Exception e) {
              os.writeObject(e);
            }
            System.out.println(line);
          }
        }

        for (ClientThread client: clients)
          if (client == this) {
            client = null;
            break;
          }

        is.close();
        os.close();
        socket.close();
      } catch (Exception e) {
        System.err.println(e);
      }
    }

    void setPlayer(Player player) {
      this.player = player;
    }

  }

  public Server (int port) throws IOException {
    super (port);
    clients = new ClientThread[MAX_CLIENT_COUNT];
    System.out.println("Start server");
  }

  public void listen () {
    System.out.println("Start to listen");

    try {
      while (true) {
        socket = accept();
        if (connectedClients == MAX_CLIENT_COUNT) 
          exitConnectionWithMessage("Server is to busy. Try later");

        int i;
        for (i = 0; i < MAX_CLIENT_COUNT && clients[i] != null; i++);
        ClientThread clientThread = new ClientThread(socket);
        clients[i] = clientThread;
        clientThread.start();
        connectedClients++;
      }
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  private void exitConnectionWithMessage (String message) throws IOException {
    PrintStream os = new PrintStream(socket.getOutputStream());

    connectedClients--;
    os.println(message);
    os.close();
    socket.close();
  }

}
