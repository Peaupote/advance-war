package fr.main.network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.InetAddress;

/**
 * Represent client playing.
 */
public class Client {

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

  public final int id;

  public Client (String host, int port)
      throws UnknownHostException, IOException,
             ClassNotFoundException {
    socket = new Socket(host, port);
    os = new ObjectOutputStream(socket.getOutputStream());
    is = new ObjectInputStream(socket.getInputStream());

    this.id = ((Integer)read()).intValue();
  }

  public void close () throws Exception {
    os.close();
    is.close();
    socket.close();
  }

  /**
   * Listening from the server.
   */
  public Object read ()
      throws IOException, ClassNotFoundException {
    return is.readObject();
  }

  public InetAddress getInetAddress () {
    return socket.getInetAddress();
  }

  public final void send(Object data) throws IOException {
    os.writeObject(new Datagram(id, data));
  }

}
