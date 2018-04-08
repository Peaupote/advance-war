package fr.main.network;

public class Datagram implements java.io.Serializable {

  public final int id;

  public final Object data;

  public Datagram (int id, Object data) {
    this.id   = id;
    this.data = data;
  }

}
