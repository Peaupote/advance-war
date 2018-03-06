package fr.main.view.render;

import java.awt.Graphics;
import java.awt.Color;

import fr.main.model.TerrainEnum;
import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.generator.MapGenerator;
import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.TerrainLocation;
import fr.main.model.terrains.land.Beach;
import fr.main.model.terrains.land.Lowland;
import fr.main.model.terrains.land.Mountain;
import fr.main.model.units.AbstractUnit;
import fr.main.view.MainFrame;
import fr.main.view.Controller;
import fr.main.view.render.terrains.land.*;
import fr.main.view.render.terrains.naval.ReefRenderer;
import fr.main.view.render.terrains.naval.RiverRenderer;
import fr.main.view.render.terrains.naval.SeaRenderer;

public class UniverseRenderer extends Universe {

  private final Controller controller;
  private final TerrainLocation[][] locations;
  private final Color fogColor    = new Color (0,0,0,100),
                      moveColor   = new Color (0, 255, 0, 50),
                      targetColor = new Color (255, 0, 0, 100);

  public UniverseRenderer (String mapName, Controller controller) {
    super (mapName);

    locations = new MapGenerator().getLocations(map.board);

//    for(Terrain[] line : map.board)
//      for(Terrain t : line)
//        ((Renderer) t).update();

	  for(int i = 0; i < map.board.length; i ++)
	  	for(int j = 0; j < map.board.length; j ++)
	  		updateTerrainRenderer(i, j);

    for (Player p: map.players)
      for (AbstractUnit u: p)
        ((Renderer)u).update();

    this.controller = controller;
  }

//    private TerrainLocation[][] setLocations () {
//		if (map == null) return null;
//
//		//TODO set all TerrainLocation.
//
//		TerrainLocation[][] out = new TerrainLocation[map.board.length][map.board[0].length];
//
//		for (int i = 0; i < map.board.length; i++)
//			for (int j = 0; j < map.board[0].length; j++) {
//
//			}
//
//		return null;
//	}
//
//	private TerrainLocation getTerrainLocationFrom(Terrain[][] map, int i, int j) {
//  		if ()
//	}
//
//	private boolean isInMap(Terrain[][] map, int x, int y) {
//		return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
//	}

	private void updateTerrainRenderer(int x, int y) {
  		switch (TerrainEnum.getTerrainEnum(map.board[x][y])) {
			case lowland: map.board[x][y] = LowlandRenderer.get((LowlandRenderer.LowlandLocation) locations[x][y]);
				LowlandRenderer.get((LowlandRenderer.LowlandLocation) locations[x][y]).update(); break;
			case sea: map.board[x][y] = SeaRenderer.get((SeaRenderer.SeaLocation) locations[x][y]);
				SeaRenderer.get((SeaRenderer.SeaLocation) locations[x][y]).update(); break;
			case river: map.board[x][y] = RiverRenderer.get((RiverRenderer.RiverLocation) locations[x][y]);
				RiverRenderer.get((RiverRenderer.RiverLocation) locations[x][y]).update(); break;
			case hill: map.board[x][y] = HillRenderer.get();
				HillRenderer.get((HillRenderer.HillLocation) locations[x][y]).update(); break;
			case mountain: map.board[x][y] = MountainRenderer.get();
				MountainRenderer.get((Mountain.MountainLocation) locations[x][y]).update(); break;
			case beach: map.board[x][y] = BeachRenderer.get((BeachRenderer.BeachLocation) locations[x][y]);
				BeachRenderer.get((BeachRenderer.BeachLocation) locations[x][y]).update(); break;
			case reef: map.board[x][y] = ReefRenderer.get();
				ReefRenderer.get().update(); break;
			case wood: map.board[x][y] = WoodRenderer.get();
				WoodRenderer.get().update(); break;
			case road: map.board[x][y] = RoadRenderer.get((RoadRenderer.RoadLocation) locations[x][y]);
				RoadRenderer.get((RoadRenderer.RoadLocation) locations[x][y]).update(); break;
			case bridge: map.board[x][y] = BridgeRenderer.get((BridgeRenderer.BridgeLocation) locations[x][y]);
				BridgeRenderer.get((BridgeRenderer.BridgeLocation) locations[x][y]).update(); break;
		}
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

    AbstractUnit unit = controller.world.getUnit(controller.cursor.getX(), controller.cursor.getY());
    if (controller.getMode() == Controller.Mode.UNIT) {
      unit.reachableLocation(targets);
      tColor = unit.getPlayer() == current ? moveColor : targetColor;
    } else if (controller.getMode() == Controller.Mode.ATTACK) {
      unit.renderTarget(targets);
      tColor = targetColor;
    }

	  for (int i = firstY; i < Math.min(lastY, map.board.length); i++)
		  for (int j = firstX; j < Math.min(lastX, map.board[i].length); j++)
//    for (int i = Math.min(lastY, map.board.length); i >= firstY ; i--)
//      for (int j = Math.min(lastX, map.board[i].length); j >= firstX; j--)
		  {
        int a = (j - x) * MainFrame.UNIT - offsetX,
            b = (i - y) * MainFrame.UNIT - offsetY;
        ((Renderer)map.board[i][j]).draw(g, a, b);

        if (!fogwar[i][j]) {
          g.setColor(fogColor);
          g.fillRect(a, b, MainFrame.UNIT, MainFrame.UNIT);
        }

        if (targets[i][j]) {
          g.setColor(tColor);
          g.fillRect(a, b, MainFrame.UNIT, MainFrame.UNIT);
        }
      }

    for (int i = firstY; i < Math.min(lastY, map.board.length); i++)
      for (int j = firstX; j < Math.min(lastX, map.board[i].length); j++) {
        int a = (j - x) * MainFrame.UNIT - offsetX,
            b = (i - y) * MainFrame.UNIT - offsetY;
        if (map.units[i][j] != null)
          if (map.units[i][j].getPlayer() == current || fogwar[i][j])
            ((Renderer)map.units[i][j]).draw(g, a, b);
      }
  }
}

