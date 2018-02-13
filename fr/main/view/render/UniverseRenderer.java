package fr.main.view.render;

import java.awt.Graphics;
import java.awt.Color;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.Unit;
import fr.main.view.MainFrame;
import fr.main.view.Controller;

public class UniverseRenderer extends Universe {

  private final Controller controller;
  private final Color fogColor    = new Color (0,0,0,100),
                      moveColor   = new Color (0, 255, 0, 50),
                      targetColor = new Color (255, 0, 0, 100);

  public UniverseRenderer (String path, Controller controller) {
    super (path);
    for(Terrain[] line : map.board)
      for(Terrain t : line)
        ((Renderer) t).update();

    this.controller = controller;
  }

  public void draw (Graphics g, int x, int y, int offsetX, int offsetY) {
    int w = map.board.length,
        h = map.board[0].length,
        firstX = x - (offsetX < 0 ? 1 : 0),
        firstY = y - (offsetY < 0 ? 1 : 0),
        lastX  = x + w + (offsetX > 0 ? 1 : 0),
        lastY  = y + h + (offsetY > 0 ? 1 : 0);

    Color tColor = null;
    boolean targets[][] = new boolean[map.board.length][map.board[0].length];
    if (controller.getMode() == Controller.Mode.UNIT) {
      Unit unit = controller.world.getUnit(controller.cursor.getX(), controller.cursor.getY());
      unit.reachableLocation(targets);
      tColor = unit.getPlayer() == current ? moveColor : targetColor;
    }

    for (int i = firstY; i < Math.min(lastY, map.board.length); i++)
      for (int j = firstX; j < Math.min(lastX, map.board[i].length); j++) {
        int a = (j - x) * MainFrame.UNIT - offsetX,
            b = (i - y) * MainFrame.UNIT - offsetY;
        ((Renderer)map.board[i][j]).draw(g, a, b);
        if (map.units[i][j] != null)
          if (map.units[i][j].getPlayer() == current || fogwar[i][j])
            ((Renderer)map.units[i][j]).draw(g, a, b);

        if (!fogwar[i][j]) {
          g.setColor(fogColor);
          g.fillRect(a, b, MainFrame.UNIT, MainFrame.UNIT);
        }
        
        if (targets[i][j]) {
          g.setColor(tColor);
          g.fillRect(a, b, MainFrame.UNIT, MainFrame.UNIT);
        }
      }
  }
}

