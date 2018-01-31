package fr.main.view.render;

import java.awt.Graphics;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.units.Unit;
import fr.main.view.MainFrame;

public class UniverseRenderer extends Universe {


  public UniverseRenderer (String path, Player[] players) {
    super (path, players);
  }

  public void draw (Graphics g, int x, int y, int offsetX, int offsetY) {
    int firstX = x - (offsetX < 0 ? 1 : 0),
        firstY = y - (offsetY < 0 ? 1 : 0),
        lastX  = x + MainFrame.WIDTH / MainFrame.UNIT + (offsetX > 0 ? 1 : 0),
        lastY  = y + MainFrame.HEIGHT / MainFrame.UNIT + (offsetY > 0 ? 1 : 0);

    for (int i = firstY; i < lastY; i++)
      for (int j = firstX; j < lastX; j++)
        ((Renderer)map.board[i][j]).draw(g, (j - x) * MainFrame.UNIT - offsetX, (i - y) * MainFrame.UNIT - offsetY);

    for (int i = 0; i < map.units.length; i++)
      for (int j = 0; j < map.units[i].length; j++) {
        Unit unit = map.units[i][j];
        if (unit.getX() >= firstX && unit.getX() < lastX &&
            unit.getY() >= firstY && unit.getY() < lastY)
          ((Renderer)unit).draw(g, (unit.getX() - x) * MainFrame.UNIT - offsetX, (unit.getY() - y) * MainFrame.UNIT - offsetY);
      }
  }
}

