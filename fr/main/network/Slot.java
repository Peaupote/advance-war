package fr.main.network;

public class Slot implements java.io.Serializable {

  /**
	 * Add Slot UID
	 */
	private static final long serialVersionUID = -5066920581822549100L;
public final String name;
  public final int id;
  public final int commander;
  public final boolean ready;

  public Slot (int id, String name, boolean ready) {
    this.name  = name;
    this.id    = id;
    this.ready = ready;
    commander  = 0;
  }

  public String toString () {
    return "Slot " + id + ": " + name + ", ready: " + ready;
  }

}
