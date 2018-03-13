package fr.main.view.render;

import java.awt.Graphics;
import java.awt.Color;

import fr.main.model.TerrainEnum;
import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.generator.MapGenerator;
import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.land.*;
import fr.main.model.units.AbstractUnit;

import fr.main.view.MainFrame;
import fr.main.view.Controller;
import fr.main.view.render.terrains.TerrainLocation;
import fr.main.view.render.terrains.land.*;
import fr.main.view.render.terrains.naval.*;

import static fr.main.model.TerrainEnum.*;

public class UniverseRenderer extends Universe {

  private final Controller controller;
  private final TerrainLocation[][] locations;
  private final Color fogColor    = new Color (0,0,0,100),
                      moveColor   = new Color (0, 255, 0, 50),
                      targetColor = new Color (255, 0, 0, 100);

  private final int[][][] coords;
  private final boolean[][] targets;
  private Color tColor;

  public UniverseRenderer (String mapName, Controller controller) {
    super (mapName);

    locations = getLocations(map.board);

	  for(int i = 0; i < map.board.length; i ++)
	  	for(int j = 0; j < map.board.length; j ++)
	  		updateTerrainRenderer(i, j);

    for (Player p: map.players)
      for (AbstractUnit u: p)
        ((Renderer)u).update();

    this.controller = controller;
    coords = new int[map.board.length][map.board[0].length][2];
    targets = new boolean[map.board.length][map.board[0].length];
  }

	private void updateTerrainRenderer(int x, int y) {
  		switch (TerrainEnum.getTerrainEnum(map.board[x][y])) {
			case lowland: map.board[x][y] = LowlandRenderer.get((TerrainLocation.LowlandLocation) locations[x][y]);
				LowlandRenderer.get((TerrainLocation.LowlandLocation) locations[x][y]).update(); break;
			case sea: map.board[x][y] = SeaRenderer.get((SeaRenderer.SeaLocation) locations[x][y]);
				SeaRenderer.get((SeaRenderer.SeaLocation) locations[x][y]).update(); break;
			case river: map.board[x][y] = RiverRenderer.get((RiverRenderer.RiverLocation) locations[x][y]);
				RiverRenderer.get((RiverRenderer.RiverLocation) locations[x][y]).update(); break;
			case hill: map.board[x][y] = HillRenderer.get();
				HillRenderer.get((TerrainLocation.HillLocation) locations[x][y]).update(); break;
			case mountain: map.board[x][y] = MountainRenderer.get();
				MountainRenderer.get((TerrainLocation.MountainLocation) locations[x][y]).update(); break;
			case beach: map.board[x][y] = BeachRenderer.get((TerrainLocation.BeachLocation) locations[x][y]);
				BeachRenderer.get((TerrainLocation.BeachLocation) locations[x][y]).update(); break;
			case reef: map.board[x][y] = ReefRenderer.get();
				ReefRenderer.get().update(); break;
			case wood: map.board[x][y] = WoodRenderer.get();
				WoodRenderer.get().update(); break;
			case road: map.board[x][y] = RoadRenderer.get((TerrainLocation.RoadLocation) locations[x][y]);
				RoadRenderer.get((TerrainLocation.RoadLocation) locations[x][y]).update(); break;
			case bridge: map.board[x][y] = BridgeRenderer.get((TerrainLocation.BridgeLocation) locations[x][y]);
				BridgeRenderer.get((TerrainLocation.BridgeLocation) locations[x][y]).update(); break;
		}
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
        coords[i][j][0] = (j - x) * MainFrame.UNIT - offsetX;
        coords[i][j][1] = (i - y) * MainFrame.UNIT - offsetY;

        if (map.buildings[i][j] != null) {
          g.setColor(Color.red);
          g.fillRect(coords[i][j][0], coords[i][j][1], MainFrame.UNIT, MainFrame.UNIT);
        }
        else ((Renderer)map.board[i][j]).draw(g, coords[i][j][0], coords[i][j][1]);

        if (targets[i][j]) {
          g.setColor(tColor);
          g.fillRect(coords[i][j][0], coords[i][j][1], MainFrame.UNIT, MainFrame.UNIT);
        }
      }

    for (int i = firstY; i < Math.min(lastY, map.board.length); i++)
      for (int j = firstX; j < Math.min(lastX, map.board[i].length); j++) {
        if (!fogwar[i][j]) {
          g.setColor(fogColor);
          g.fillRect(coords[i][j][0], coords[i][j][1], MainFrame.UNIT, MainFrame.UNIT);
        }

        if (map.units[i][j] != null)
          if (map.units[i][j].getPlayer() == current || fogwar[i][j])
            ((Renderer)map.units[i][j]).draw(g, coords[i][j][0], coords[i][j][1]);
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

	TerrainLocation[][] getLocations(TerrainEnum[][] map) {
		TerrainLocation[][] out = new TerrainLocation[map.length][map[0].length];
		//TODO finish this function.
		for(int i = 0; i < map.length; i ++)
			for(int j = 0; j < map[0].length; j ++)
				switch (map[i][j]) {
					case lowland:
						if(isInMap(map, i, j - 1)
								&& (map[i][j - 1] == mountain || map[i][j - 1] == hill || map[i][j - 1] == wood))
							out[i][j] = TerrainLocation.LowlandLocation.SHADOW;
						else out[i][j] = TerrainLocation.LowlandLocation.NORMAL;
						break;
					case sea: 		out[i][j] = TerrainLocation.SeaLocation.NORMAL; 			break; 	//TODO
					case wood:		out[i][j] = TerrainLocation.WoodLocation.NORMAL; 			break;
					case reef:		out[i][j] = TerrainLocation.ReefLocation.NORMAL; 			break;
					case road:		out[i][j] = TerrainLocation.RoadLocation.CENTER; 			break; 	//TODO
					case mountain: 	out[i][j] = TerrainLocation.MountainLocation.NORMAL; 	break;
					case hill: 		out[i][j] = TerrainLocation.HillLocation.NORMAL; 			break;
					case beach: 	out[i][j] = TerrainLocation.BeachLocation.LEFT; 			break; 	//TODO
					case river: 	out[i][j] = TerrainLocation.RiverLocation.CENTER; 		break; 	//TODO
					case bridge:
						if(isInMap(map, i - 1, j) && map[i - 1][j] == sea)
							out[i][j] = TerrainLocation.BridgeLocation.HORIZONTAL;
						else out[i][j] = TerrainLocation.BridgeLocation.VERTICAL;
						break;
				}
		return out;
	}


	public TerrainLocation[][] getLocations(Terrain[][] map) {
		TerrainEnum[][] mapBis = new TerrainEnum[map.length][map[0].length];
		for(int i = 0; i < map.length; i ++)
			for(int j = 0; j < map[0].length; j ++)
				mapBis[i][j] = TerrainEnum.getTerrainEnum(map[i][j]);
		return getLocations(mapBis);
	}

	boolean isInMap(TerrainEnum[][] map, int x, int y) {
		return x >= 0 && x < map.length && y >= 0 && y < map[0].length;
	}

}

