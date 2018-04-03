package fr.main.network;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable {

  private Socket socket;

  private PrintStream os;
  private DataInputStream is;

  private BufferedReader inputLine;
  private static boolean closed;

  public Client (String host, int port)
      throws UnknownHostException, IOException {
    socket = new Socket(host, port);
    inputLine = new BufferedReader(new InputStreamReader(System.in));
    os = new PrintStream(socket.getOutputStream());
    is = new DataInputStream(socket.getInputStream());

    new Thread(this).start();
    while (!closed) os.println(inputLine.readLine());

    os.close();
    is.close();
    socket.close();
  }

  public void run () {
    String response;

    try {
      while ((response = is.readLine()) != null) {
        System.out.println(response);
        if (response.equals("quit")) closed = true;
      }
    } catch (IOException e) {
      System.err.println(e);
    }
  }

}
