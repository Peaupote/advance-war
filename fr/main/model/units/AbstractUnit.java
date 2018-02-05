package fr.main.model.units;

import java.awt.Point;

import fr.main.model.Player;

/**
 * Represents a unit on the board
 */
public interface AbstractUnit extends java.io.Serializable {

	public boolean removeLife (int life);
	public void addLife (int life);
	public boolean setLife (int life);
	public int getLife ();
	public void setPlayer (Player p);
	public Player getPlayer();
	public int getX ();
	public int getY ();
	public void renderVision (boolean[][] fog);
	public String toString();

}