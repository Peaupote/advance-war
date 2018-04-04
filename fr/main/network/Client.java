package fr.main.network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Represent client playing.
 */
public class Client implements Runnable {

  /**
   * Socket connecting to the server.
   */
  private Socket socket;

  /**
   * Send messages to the server with this stream.
   */
  private ObjectOutputStream os;

  /**
   * Receive messages from the server with this stream.
   */
  private ObjectInputStream is;

  /**
   * User input stream.
   */
  private BufferedReader inputLine;

  /**
   * Advance war protocol telling client
   * what he must do.
   */
  private AdwProtocol.Client protocol;

  /**
   * Is listening to the server.
   */
  private static boolean closed;

  public Client (String host, int port)
      throws UnknownHostException, IOException {
    closed = false;
    socket = new Socket(host, port);
    inputLine = new BufferedReader(new InputStreamReader(System.in));
    os = new ObjectOutputStream(socket.getOutputStream());
    is = new ObjectInputStream(socket.getInputStream());
    protocol = new AdwProtocol.Client();

    new Thread(this).start();

    /**
     * Send messages to the server.
     */
    Object data;
    while ((data = protocol.processing()) != null)
      os.writeObject(data);

    os.close();
    is.close();
    socket.close();
  }

  /**
   * Listening from the server.
   */
  public void run () {
    Object response;

    try {
      while ((response = is.readObject()) != null)
        protocol.processInput(response);
    } catch (Exception e) {
      System.err.println(e);
    }
  }

}
