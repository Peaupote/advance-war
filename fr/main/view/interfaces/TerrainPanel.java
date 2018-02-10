package fr.main.view.interfaces;

import java.awt.Graphics;
import java.awt.Color;

import fr.main.view.MainFrame;
import fr.main.model.Universe;
import fr.main.view.Position;

public class TerrainPanel extends InterfaceUI {

  private static final Color BACKGROUNDCOLOR = new Color(0,0,0,230);
  private static final Color FOREGROUNDCOLOR = Color.white;
  private static final int WIDTH = 100, HEIGHT = 200, MARGIN = 10,
          HALFW = MainFrame.WIDTH / (2 * MainFrame.UNIT),
          HALFH = MainFrame.HEIGHT / (2 * MainFrame.UNIT);

  protected final Position.Cursor cursor;
  protected final Position.Camera camera;
  protected final Universe world;

  public TerrainPanel (Position.Cursor cursor, Position.Camera camera) {
    this.cursor = cursor;
    this.camera = camera;
    this.world = Universe.get();
  }

  @Override
  protected void draw (Graphics g) {
    int x = cursor.getX() - camera.getX() >= HALFW && cursor.getY() - camera.getY() >= HALFH ? MARGIN : MainFrame.WIDTH - WIDTH - MARGIN,
        y = MainFrame.HEIGHT - HEIGHT - MARGIN;
    g.setColor (BACKGROUNDCOLOR);
    g.fillRect (x, y, WIDTH, HEIGHT);

    g.setColor (FOREGROUNDCOLOR);
    g.drawString (world.get(cursor.getX(), cursor.getY()).toString(), x + 20, y + 20);

    // Units info :
    if(world.isVisible(cursor.getX(), cursor.getY()) && world.getUnit (cursor.getX(), cursor.getY()) != null) {
      String[] unitInfo = world.getUnit(cursor.getX(), cursor.getY()).toString().split("\n");
      for(int i = 0; i < unitInfo.length; i ++)
        g.drawString(unitInfo[i], x + 20, y + 40 + i * 10);
    } else
      g.drawString ("No Unit", x + 20, y + 40);
  }

}

