package fr.main.view.render;

import java.awt.Graphics;
import java.awt.Color;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.units.Unit;
import fr.main.view.MainFrame;

public class UniverseRenderer extends Universe {

  private final Color fogColor = new Color (0,0,0,100);

  public UniverseRenderer (String path, Player[] players) {
    super (path, players);
  }

  public void draw (Graphics g, int x, int y, int offsetX, int offsetY) {
    int w = MainFrame.WIDTH / MainFrame.UNIT,
        h = MainFrame.HEIGHT / MainFrame.UNIT,
        firstX = x - (offsetX < 0 ? 1 : 0),
        firstY = y - (offsetY < 0 ? 1 : 0),
        lastX  = x + w + (offsetX > 0 ? 1 : 0),
        lastY  = y + h + (offsetY > 0 ? 1 : 0);

    boolean[][] fogwar = new boolean[map.board.length][map.board[0].length];

    for (int i = firstY; i < Math.min(lastY, map.board.length); i++)
      for (int j = firstX; j < Math.min(lastX, map.board[i].length); j++) {
        int a = (j - x) * MainFrame.UNIT - offsetX,
            b = (i - y) * MainFrame.UNIT - offsetY;
        ((Renderer)map.board[i][j]).draw(g, a, b);
      }

    for (int j = 0; j < map.units[current.id - 1].length; j++) {
      Unit unit = map.units[current.id - 1][j];
      if (unit.getX() >= firstX && unit.getX() < lastX &&
          unit.getY() >= firstY && unit.getY() < lastY) {
            ((Renderer)unit).draw(g,
              (unit.getX() - x) * MainFrame.UNIT - offsetX,
              (unit.getY() - y) * MainFrame.UNIT - offsetY);
          }
      unit.renderVision(fogwar);
    }

    for (int i = 0; i < map.units.length; i++) {
      if (i + 1 == current.id) continue;
      for (int j = 0; j < map.units[i].length; j++) {
        Unit unit = map.units[i][j];
        if (fogwar[unit.getX()][unit.getY()]) {
              ((Renderer)unit).draw(g,
                (unit.getX() - x) * MainFrame.UNIT - offsetX,
                (unit.getY() - y) * MainFrame.UNIT - offsetY);
            }
      }
    }

    for (int i = firstY; i < lastY; i++)
      for (int j = firstX; j < lastX; j++) {
        if (!fogwar[i][j]) {
          int a = (j - x) * MainFrame.UNIT - offsetX,
              b = (i - y) * MainFrame.UNIT - offsetY;
            g.setColor(fogColor);
            g.fillRect(a, b, MainFrame.UNIT, MainFrame.UNIT);
        }
      }

  }
}

