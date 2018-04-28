package fr.main.view.render;

import java.awt.*;
import java.util.*;
import java.util.function.BiConsumer;

import fr.main.model.Universe;
import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.view.MainFrame;
import fr.main.view.render.buildings.BuildingRenderer;
import fr.main.view.render.sprites.Sprite;
import fr.main.view.render.terrains.TerrainRenderer;

public class MapRenderer extends Universe {

    private static final Font font = new Font("Helvetica", Font.PLAIN, 14);
    protected final Point[][] coords;

    protected class Frame {
      final int firstX, firstY, lastX, lastY;

      public Frame (int w, int h, int x, int y, int offsetX, int offsetY) {
        firstX = x - (offsetX < 0 ? 1 : 0);
        firstY = y - (offsetY < 0 ? 1 : 0);
        lastX  = x + w + (offsetX > 0 ? 1 : 0);
        lastY  = y + h + (offsetY > 0 ? 1 : 0);
      }

      public void forEach (BiConsumer<Integer, Integer> func) {
        for (int i = firstY; i < Math.min(lastY, map.board.length); i++)
            for (int j = firstX; j < Math.min(lastX, map.board[i].length); j++)
              func.accept(i, j);
      }
    }

    public MapRenderer (Universe.Board b) {
      super(b);
      coords = new Point[map.board.length][map.board[0].length];
      for (int i = 0; i < map.board.length; i++)
          for (int j = 0; j < map.board[i].length; j++)
              coords[i][j] = new Point(0, 0);

      TerrainRenderer.setLocations();
    }

    public MapRenderer (AbstractUnit[][] units, AbstractTerrain[][] map, Player[] ps, AbstractBuilding[][] buildings){
        this (new Universe.Board(units, ps, map, buildings));
    }

    public MapRenderer (String mapName) {
        this (Universe.restaure(mapName));
    }

    public MapRenderer (String mapName, Player[] ps) {
        this (Universe.restaure(mapName).setPlayers(ps));
    }

    public Frame draw (Graphics g, int x, int y, int offsetX, int offsetY) {
        g.setFont(font);
        int w = map.board.length,
            h = map.board[0].length;
        Frame f = new Frame(w, h, x, y, offsetX, offsetY);

        // TODO: i thinks java create a new consumer each frame
        f.forEach((i,j) -> {
            coords[i][j].x = (j - x) * MainFrame.UNIT - offsetX;
            coords[i][j].y = (i - y) * MainFrame.UNIT - offsetY;

            TerrainRenderer.render(g, coords[i][j], new Point(j, i));
            if (map.buildings[i][j] != null)
              	BuildingRenderer.render(g, coords[i][j], map.buildings[i][j]);

        });

        return f;
    }


}
