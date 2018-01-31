package fr.main.model.units;

import fr.main.model.Drawable;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;

import fr.main.model.Player;
import fr.main.view.MainFrame;

/**
 * Represent a unit on the board
 */
public class Unit implements Drawable, java.io.Serializable {

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

  public void draw (Graphics g, int x, int y) {
    g.setColor(Color.red);
    g.fillRect(x, y, MainFrame.UNIT, MainFrame.UNIT);
  }

}
