package fr.main.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

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
     * Tell the server what to do with the date he received.
     */
    private AdwProtocol protocol;

    /**
     * Client's Player informations.
     */
    Slot slot;

    final int id;

    public ClientThread(int id, Socket socket) {
      this.socket = socket;
      this.id     = id;
    }

    public void run () {
      try {
        is = new ObjectInputStream(socket.getInputStream());
        os = new ObjectOutputStream(socket.getOutputStream());
        protocol = new AdwProtocol(this);
        System.out.println("New Client " + id + ": " + socket);

        // send to the client his id and already connected clients id and player
        os.writeObject(id);
        for (int i = 0; i < id; i++)
          os.writeObject(new Datagram(i, clients[i].slot));

        synchronized (this) {
          Object data;
          while ((data = is.readObject()) != null) {
            System.out.println("Client " + id + ": " + data);
            protocol.proccessInput(data);
          }
        }

        clients[id] = null;

        is.close();
        os.close();
        socket.close();
      } catch (Exception e) {
        System.err.println(e);
        //e.printStackTrace();
      }
    }

    public synchronized void sendAll (Datagram data) throws IOException {
      for (int i = 0; i < clients.length; i++)
        if (clients[i] != null && i != id)
          clients[i].os.writeObject(data);
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
        clients[i] = new ClientThread(i, socket);
        clients[i].start();
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
