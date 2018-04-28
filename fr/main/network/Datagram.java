package fr.main.network;

public class Datagram implements java.io.Serializable {

    /**
	 * Add Datagram UID
	 */
	private static final long serialVersionUID = -3791265471569436926L;

    public final int id;

    public final Object data;

    public Datagram (int id, Object data) {
        this.id   = id;
        this.data = data;
    }

}
