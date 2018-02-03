package fr.main.view.interfaces;

import java.awt.Graphics;
import java.awt.Color;

import fr.main.view.MainFrame;
import fr.main.model.Universe;
import fr.main.view.Position;

public class TerrainPanel implements InterfaceUI {

  private static final Color BACKGROUNDCOLOR = new Color(0,0,0,230);
  private static final Color FOREGROUNDCOLOR = Color.white;
  private static final int WIDTH = 100, HEIGHT = 200, MARGIN = 10;

  protected final Position.Cursor cursor;
  protected final Universe world;

  public TerrainPanel (Position.Cursor cursor, Universe world) {
    this.cursor = cursor;
    this.world = world;
  }

  public void draw (Graphics g) {
    int x = cursor.getX() < MainFrame.WIDTH / (2 * MainFrame.UNIT) ? MainFrame.WIDTH - WIDTH - MARGIN : MARGIN,
        y = MainFrame.HEIGHT - HEIGHT - MARGIN;
    g.setColor (BACKGROUNDCOLOR);
    g.fillRect (x, y, WIDTH, HEIGHT);

    g.setColor (FOREGROUNDCOLOR);
    g.drawString (world.get(cursor.getX(), cursor.getY()).toString(), x + 20, y + 20);
  }

}

