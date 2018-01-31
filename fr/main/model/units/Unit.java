package fr.main.model.units;

import java.awt.Point;

import fr.main.model.Player;
import fr.main.view.MainFrame;

/**
 * Represent a unit on the board
 */
public class Unit implements java.io.Serializable {

  /**
   * Life in percentage
   */
  private int life;
  private final Point location;
  private Player player;

  public Unit (Point location) {
    this (null, location);
  }

  public Unit (Player player, Point location) {
    life          = 100;
    this.location = location;
    this.player   = player;
  }

  public final void setLife (int life) {
    this.life = Math.max(0, Math.min(100, life));
  }

  public final int getLife () {
    return life;
  }

  public final void setPlayer (Player p) {
    if (player == null)
      this.player = p;
  }

  public final int getX () {
    return location.x;
  }

  public final int getY () {
    return location.y;
  }

  public void renderVision (boolean[][] fog) {
    for (int i = Math.max(0, location.y - 1); i < Math.min(location.y + 2, fog.length); i++)
      for (int j = Math.max(0, location.x - 1); j < Math.min(location.x + 2, fog[i].length); j++)
        fog[i][j] = true;
  }

}
