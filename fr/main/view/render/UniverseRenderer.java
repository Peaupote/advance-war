package fr.main.view.render;

import java.awt.*;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.view.MainFrame;
import fr.main.view.Controller;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.render.terrains.TerrainRenderer;

public class UniverseRenderer extends Universe {

  private final Controller controller;
  private final Color fogColor    = new Color (0,0,0,100),
                      moveColor   = new Color (0, 255, 0, 50),
                      targetColor = new Color (255, 0, 0, 100);

  private final Point[][] coords;
  private final boolean[][] targets;
  private Color tColor;

  public UniverseRenderer (String mapName, Controller controller) {
    super (mapName);

    this.controller = controller;
    coords = new Point[map.board.length][map.board[0].length];
    for (int i = 0; i < map.board.length; i++)
      for (int j = 0; j < map.board[i].length; j++)
        coords[i][j] = new Point(0, 0);

    targets = new boolean[map.board.length][map.board[0].length];
  }

  public void draw (Graphics g, int x, int y, int offsetX, int offsetY) {
    int w = map.board.length,
        h = map.board[0].length,
        firstX = x - (offsetX < 0 ? 1 : 0),
        firstY = y - (offsetY < 0 ? 1 : 0),
        lastX  = x + w + (offsetX > 0 ? 1 : 0),
        lastY  = y + h + (offsetY > 0 ? 1 : 0);

	  for (int i = firstY; i < Math.min(lastY, map.board.length); i++)
		  for (int j = firstX; j < Math.min(lastX, map.board[i].length); j++) {
        coords[i][j].x = (j - x) * MainFrame.UNIT - offsetX;
        coords[i][j].y = (i - y) * MainFrame.UNIT - offsetY;

        if (map.buildings[i][j] != null) {
          g.setColor(Color.red);
          g.fillRect(coords[i][j].x, coords[i][j].y, MainFrame.UNIT, MainFrame.UNIT);
        } else TerrainRenderer.render(g, coords[i][j], map.board[i][j]);

        if (targets[i][j]) {
          g.setColor(tColor);
          g.fillRect(coords[i][j].x, coords[i][j].y, MainFrame.UNIT, MainFrame.UNIT);
        }
      }

    for (int i = firstY; i < Math.min(lastY, map.board.length); i++)
      for (int j = firstX; j < Math.min(lastX, map.board[i].length); j++) {
        if (!fogwar[i][j]) {
          g.setColor(fogColor);
          g.fillRect(coords[i][j].x, coords[i][j].y, MainFrame.UNIT, MainFrame.UNIT);
        }

        if (map.units[i][j] != null)
          if (map.units[i][j].getPlayer() == current || fogwar[i][j])
            UnitRenderer.render(g, coords[i][j], map.units[i][j]);
      }
  }

  public void updateTarget (AbstractUnit unit) {
      clearTarget();
      if (controller.getMode() == Controller.Mode.UNIT) {
        unit.reachableLocation(targets);
        tColor = unit.getPlayer() == current ? moveColor : targetColor;
      } else if (controller.getMode() == Controller.Mode.ATTACK) {
        unit.renderTarget(targets);
        tColor = targetColor;
      }
  }

  public void clearTarget () {
      for (int i = 0; i < targets.length; i++)
        for (int j = 0; j < targets[i].length; j++)
          targets[i][j] = false;
  }

}

