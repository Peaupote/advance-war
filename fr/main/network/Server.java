package fr.main.network;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class Server extends ServerSocket {

  private static final int MAX_CLIENT_COUNT = 4;

  private final ClientThread[] clients;
  private int connectedClients;
  private Socket socket;

  private class ClientThread extends Thread {

    DataInputStream is;
    PrintStream os;
    Socket socket;

    public ClientThread(Socket socket) {
      this.socket = socket;
    }

    public void run () {
      try {
        is = new DataInputStream(socket.getInputStream());
        os = new PrintStream(socket.getOutputStream());
        os.println("Coucou");
        sendAll("Hi !");

        synchronized (this) {
          String line = is.readLine();
          while ((line != null)) {
            if (line.equals("quit")) {
              sendAll(line);
              break;
            }
          }
        }
        sendAll("Bye.");
        os.println("Bye.");

        for (ClientThread client: clients)
          if (client == this)
            client = null;

        is.close();
        os.close();
        socket.close();
      } catch (IOException e) {
        System.err.println(e);
      }
    }

    public synchronized void sendAll (String message) {
      for (ClientThread client: clients)
        if (client != this && client != null)
          client.os.println(message);
    }
  
  }

  public Server (int port) throws IOException {
    super (port);
    clients = new ClientThread[MAX_CLIENT_COUNT];

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
  }

  private void exitConnectionWithMessage (String message) throws IOException {
    PrintStream os = new PrintStream(socket.getOutputStream());

    connectedClients--;
    os.println(message);
    os.close();
    socket.close();
  }

}
