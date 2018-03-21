package fr.main.view.render;

import java.awt.*;
import java.util.LinkedList;

import fr.main.model.TerrainEnum;
import fr.main.model.Node;
import fr.main.model.MoveZone;
import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.land.*;
import fr.main.model.terrains.naval.Reef;
import fr.main.model.terrains.naval.Sea;
import fr.main.model.units.AbstractUnit;
import fr.main.view.MainFrame;
import fr.main.view.Controller;
import fr.main.view.render.terrains.TerrainLocation;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.render.terrains.TerrainRenderer;
import fr.main.view.render.buildings.BuildingRenderer;

public class UniverseRenderer extends Universe {

	private final Controller controller;
	private final Color fogColor    = new Color (0,0,0,100),
			moveColor   = new Color (0, 255, 0, 50),
			targetColor = new Color (255, 0, 0, 100);

	private final Point[][] coords;
	private final boolean[][] targets;
  	private Point upperLeft = new Point(0,0), lowerRight;
	private Color tColor;
	private TerrainLocation[][] tLocations;

	public static class FlashMessage {

		public enum Type {
			ALERT(Color.red),
			SUCCESS(Color.green);

			public final Color color;

			private Type (Color color) {
				this.color = color;
			}
		}

		private final int x, y;
		private int time;
		private final String message;
		private final Type type;

		public FlashMessage (String message, int x, int y, int time, Type type) {
			this.time    = time;
			this.message = message;
			this.x       = x;
			this.y       = y;
			this.type    = type;
		}

	}

	private final LinkedList<FlashMessage> flashs;

	public UniverseRenderer (String mapName, Controller controller) {
		super (mapName);

		this.controller = controller;
		coords = new Point[map.board.length][map.board[0].length];
		for (int i = 0; i < map.board.length; i++)
			for (int j = 0; j < map.board[i].length; j++)
				coords[i][j] = new Point(0, 0);

		targets    = new boolean[map.board.length][map.board[0].length];
 		lowerRight = new Point(map.board.length, map.board[0].length);
 		flashs     = new LinkedList<>();
		setLocations();
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

				TerrainRenderer.render(g, coords[i][j], map.board[i][j], tLocations[i][j]);
				if (map.buildings[i][j] != null) BuildingRenderer.render(g, coords[i][j], map.buildings[i][j]);

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

		for (FlashMessage message: flashs) {
			g.setColor(message.type.color);
			g.drawString (message.message, message.x, message.y);
			message.time -= 10;
			if (message.time <= 0) flashs.remove(message);
		}
	}

	public void updateTarget (AbstractUnit unit) {
		clearTarget();
		if (controller.getMode() == Controller.Mode.UNIT) {
			MoveZone m = unit.getMoveMap();
			int moveQuantity = unit.getMoveQuantity();
			Node[][] n = m.map;
			upperLeft = m.offset;
			lowerRight = new Point(upperLeft.x + n[0].length, upperLeft.y + n.length);
			for (int j = upperLeft.y; j < lowerRight.y; j++)
				for (int i = upperLeft.x; i < lowerRight.x; i ++)
					targets[j][i] = n[j - upperLeft.y][i - upperLeft.x].lowestCost <= moveQuantity;
			tColor = unit.getPlayer() == current ? moveColor : targetColor;
		} else if (controller.getMode() == Controller.Mode.ATTACK) {
			unit.renderTarget(targets);
			upperLeft = new Point (0,0);
			lowerRight = new Point (targets.length, targets[0].length);
			tColor = targetColor;
		}
	}

	public void clearTarget () {
		for (int i = upperLeft.x; i < lowerRight.x; i++)
			for (int j = upperLeft.y; j < lowerRight.y; j++)
				targets[j][i] = false;
		upperLeft.move(0,0);
		lowerRight.move(coords[0].length, coords.length);
	}

	public void flash (String message, int x, int y, int time) {
		flashs.add(new FlashMessage(message, x, y, time, FlashMessage.Type.SUCCESS));
	}

	public void flash (String message, int x, int y, int time, FlashMessage.Type type) {
		flashs.add(new FlashMessage(message, x, y, time, type));
	}

	private void setLocations () {
		TerrainEnum[][] tEnum = new TerrainEnum[map.board.length][map.board[0].length];

		for(int i = 0; i < map.board.length; i ++)
			for(int j = 0; j < map.board[0].length; j ++)
				tEnum[i][j] = TerrainEnum.getTerrainEnum(map.board[i][j]);

		this.tLocations = new TerrainLocation[map.board.length][map.board[0].length];

		for(int i = 0; i < tEnum.length; i ++)
			for(int j = 0; j < tEnum[0].length; j ++) {
				switch (tEnum[i][j]) {
					case sea: tLocations[i][j] = TerrainLocation.SeaLocation.NORMAL; break;
					case lowland:
						if(isInMap(i, j - 1) &&
								(tEnum[i][j - 1] == TerrainEnum.mountain
										|| tEnum[i][j - 1] == TerrainEnum.hill
										|| tEnum[i][j - 1] == TerrainEnum.wood))
							tLocations[i][j] = TerrainLocation.LowlandLocation.SHADOW;
						else tLocations[i][j] = TerrainLocation.LowlandLocation.NORMAL;
						break;
					case hill: 		tLocations[i][j] = TerrainLocation.HillLocation.NORMAL; break;
					case mountain: 	tLocations[i][j] = TerrainLocation.MountainLocation.NORMAL; break;
					case wood: 		tLocations[i][j] = TerrainLocation.WoodLocation.NORMAL; break;
					case reef: 		tLocations[i][j] = TerrainLocation.ReefLocation.NORMAL; break;
					case bridge:
						if(isInMap(i, j - 1) &&
								(tEnum[i][j - 1] == TerrainEnum.sea || tEnum[i][j - 1] == TerrainEnum.river))
							tLocations[i][j] = TerrainLocation.BridgeLocation.VERTICAL;
						else tLocations[i][j] = TerrainLocation.BridgeLocation.HORIZONTAL;
						break;
					case river: 	tLocations[i][j] = setRiverLocation(tEnum, i, j); break;
					case beach: tLocations[i][j] = TerrainLocation.BeachLocation.TOP; break; // TODO
					case road: 		tLocations[i][j] = setRoadLocation(tEnum, i, j); break;
					default: {}

				}
			}
	}

	private boolean isInMap(int x, int y) {
		return x >= 0 && x < map.board.length && y >= 0 && y < map.board[0].length;
	}

	private TerrainLocation.BeachLocation setBeachLocation(TerrainEnum[][] tEnum, int x, int y) {
		return null;
	}
	private TerrainLocation.SeaLocation setSeaLocation(TerrainEnum[][] tEnum, int x, int y) {
		return null;
	}

	private TerrainLocation.RiverLocation setRiverLocation(TerrainEnum[][] tEnum, int x, int y) {
		TerrainEnum[] cross = terrainCross(tEnum, x, y);
		int count = 0;

		for(TerrainEnum t : cross)
			if(t == TerrainEnum.river) count ++;

		switch (count) {
			case 4:
				return TerrainLocation.RiverLocation.CENTER;
			case 3:
				if (cross[0] != TerrainEnum.river) return TerrainLocation.RiverLocation.T_BOTTOM;
				if (cross[1] != TerrainEnum.river) return TerrainLocation.RiverLocation.T_LEFT;
				if (cross[2] != TerrainEnum.river) return TerrainLocation.RiverLocation.T_TOP;
				if (cross[3] != TerrainEnum.river) return TerrainLocation.RiverLocation.T_RIGHT;
			case 2:
				for (int i = 0; i < 4; i++)
					if (cross[i] == TerrainEnum.river)
						for (int j = i + 1; j < 4; j++)
							if (cross[j] == TerrainEnum.river)
								switch (i + j) {
									case 1:
										return TerrainLocation.RiverLocation.TURN_TOP_RIGHT;
									case 2:
										return TerrainLocation.RiverLocation.VERTICAL;
									case 3:
										if (i == 0) return TerrainLocation.RiverLocation.TURN_TOP_LEFT;
										else return TerrainLocation.RiverLocation.TURN_BOTTOM_RIGHT;
									case 4:
										return TerrainLocation.RiverLocation.HORIZONTAL;
									case 5:
										return TerrainLocation.RiverLocation.TURN_BOTTOM_LEFT;
								}
			case 1:
				if (cross[0] == TerrainEnum.river) return TerrainLocation.RiverLocation.TOP_END;
				if (cross[1] == TerrainEnum.river) return TerrainLocation.RiverLocation.RIGHT_END;
				if (cross[2] == TerrainEnum.river) return TerrainLocation.RiverLocation.BOTTOM_END;
				if (cross[3] == TerrainEnum.river) return TerrainLocation.RiverLocation.LEFT_END;
			case 0:

			default:
				return TerrainLocation.RiverLocation.CENTER;
		}
	}

	private TerrainLocation.RoadLocation setRoadLocation(TerrainEnum[][] tEnum, int x, int y) {
		TerrainEnum[] cross = terrainCross(tEnum, x, y);
		int count = 0;

		for(TerrainEnum t : cross)
			if(t == TerrainEnum.road) count ++;

		switch (count) {
			case 4 : return TerrainLocation.RoadLocation.CENTER;
			case 3 :
				if(cross[0] != TerrainEnum.road) return TerrainLocation.RoadLocation.T_BOTTOM;
				if(cross[1] != TerrainEnum.road) return TerrainLocation.RoadLocation.T_LEFT;
				if(cross[2] != TerrainEnum.road) return TerrainLocation.RoadLocation.T_TOP;
				if(cross[3] != TerrainEnum.road) return TerrainLocation.RoadLocation.T_RIGHT;
			case 2 :
				for(int i = 0; i < 4; i ++)
					if(cross[i] == TerrainEnum.road)
						for(int j = i + 1; j < 4; j ++)
							if(cross[j] == TerrainEnum.road)
								switch (i + j) {
									case 1 : return TerrainLocation.RoadLocation.TURN_TOP_RIGHT;
									case 2 : return TerrainLocation.RoadLocation.VERTICAL;
									case 3 :
										if(i == 0) return TerrainLocation.RoadLocation.TURN_TOP_LEFT;
										else return TerrainLocation.RoadLocation.TURN_BOTTOM_RIGHT;
									case 4 : return TerrainLocation.RoadLocation.HORIZONTAL;
									case 5 : return TerrainLocation.RoadLocation.TURN_BOTTOM_LEFT;
								}
			case 1 :
				if(cross[0] == TerrainEnum.road || cross[2] == TerrainEnum.road)
					return TerrainLocation.RoadLocation.VERTICAL;
				else return TerrainLocation.RoadLocation.HORIZONTAL;
			default: return TerrainLocation.RoadLocation.CENTER;

		}
	}

	private TerrainEnum[] terrainCross(TerrainEnum[][] tEnum, int x, int y) {
		TerrainEnum[] cross = new TerrainEnum[4];
		int count = 0;

		cross[0] = isInMap(x - 1, y) ? tEnum[x - 1][y] : TerrainEnum.none;
		cross[1] = isInMap(x, y + 1) ? tEnum[x][y + 1] : TerrainEnum.none;
		cross[2] = isInMap(x + 1, y) ? tEnum[x + 1][y] : TerrainEnum.none;
		cross[3] = isInMap(x, y - 1) ? tEnum[x][y - 1] : TerrainEnum.none;

		return cross;
	}

}

