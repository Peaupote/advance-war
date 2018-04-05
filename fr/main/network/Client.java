package fr.main.network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

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
   * Advance war protocol telling client
   * what he must do.
   */
  private AdwProtocol.Client protocol;

  public Client (String host, int port)
      throws UnknownHostException, IOException {
    socket = new Socket(host, port);
    os = new ObjectOutputStream(socket.getOutputStream());
    is = new ObjectInputStream(socket.getInputStream());
    protocol = new AdwProtocol.Client();

    new Thread(this).start();
  }

  public void listen () {
    try {
      Object data;
      while ((data = protocol.processing()) != null)
        os.writeObject(data);
  
      os.close();
      is.close();
      socket.close();
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  /**
   * Listening from the server.
   */
  public void run () {
    Object data;

    try {
      while ((data = is.readObject()) != null)
        protocol.processInput(data);
    } catch (Exception e) {
      System.err.println(e);
    }
  }

  public InetAddress getInetAddress () {
    return socket.getInetAddress();
  }

}
