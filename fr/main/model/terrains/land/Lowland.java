package fr.main.model.terrains.land;

import java.awt.Graphics;
import java.awt.Color;

import fr.main.view.MainFrame;
import fr.main.model.units.Unit;

public class Lowland extends LandTerrain {

  private int defense = 1;

  public int getDefense (Unit u) {
    return defense;
  }

  public int getBonusVision (Unit u) {
    return 0;
  }

  public int getBonusRange (Unit u) {
    return 0;
  }

  public boolean isHiding (Unit u) {
    return false;
  }

  public void draw (Graphics g, int x, int y) {
    g.setColor(Color.green);
    g.fillRect(x, y, MainFrame.UNIT, MainFrame.UNIT);
  }

  public String toString () {
    return "L";
  }
}
