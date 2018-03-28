package fr.main.view.render.terrains;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.Direction;
import fr.main.model.Weather;
import fr.main.model.terrains.land.*;
import fr.main.model.terrains.naval.*;
import fr.main.model.Universe;

import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.terrains.land.*;
import fr.main.view.render.terrains.naval.*;
import fr.main.view.render.animations.*;
import fr.main.model.TerrainEnum;

public class TerrainRenderer {

	public static HashMap<TerrainLocation, Render> renderers = new HashMap<>();
	private static TerrainLocation[][] tLocations;

	public static class Render extends Renderer {

		protected Animation anim;

		public Render() {
			anim = new Animation();
		}

		public Render(TerrainLocation location) {
			anim = new Animation();

			LinkedList<ScaleRect> areas = new LinkedList<>();
			areas.add(location.getRect());
			AnimationState normal = new AnimationState(new SpriteList(TerrainLocation.getDir() + "normal/" + location.getPath(), areas), 20);
			AnimationState snow   = new AnimationState(new SpriteList(TerrainLocation.getDir() + "snow/"   + location.getPath(), areas), 20);
			anim.put("normal", normal);
			anim.put("snow", snow);
			anim.setState("normal");
		}

		public void draw(Graphics g, int x, int y) {
			anim.draw(g, x, y);
		}

	}

	public static void updateAll(){
		String s = Universe.get().getWeather() == Weather.SNOWY ? "snow" : "normal";
		for (Render render : renderers.values()) render.anim.setState(s);
	}

	public static Render getRender(AbstractTerrain terrain, TerrainLocation location) {
		if (renderers.containsKey(location))
			return renderers.get(location);

		// TODO: nice stuff with reflection
		if (location instanceof TerrainLocation.HillLocation)
			renderers.put(location, new HillRenderer((TerrainLocation.HillLocation) location));
		else if (location instanceof TerrainLocation.LowlandLocation)
			renderers.put(location, new LowlandRenderer((TerrainLocation.LowlandLocation) location));
		else if (location instanceof TerrainLocation.MountainLocation)
			renderers.put(location, new MountainRenderer((TerrainLocation.MountainLocation) location));
		else if (location instanceof TerrainLocation.BeachLocation)
			renderers.put(location, new BeachRenderer((TerrainLocation.BeachLocation) location));
		else if (location instanceof TerrainLocation.WoodLocation)
			renderers.put(location, new WoodRenderer((TerrainLocation.WoodLocation) location));
		else if (location instanceof TerrainLocation.RiverLocation)
			renderers.put(location, new RiverRenderer((TerrainLocation.RiverLocation) location));
		else if (location instanceof TerrainLocation.RoadLocation)
			renderers.put(location, new RoadRenderer((TerrainLocation.RoadLocation) location));
		else if (location instanceof TerrainLocation.BridgeLocation)
			renderers.put(location, new BridgeRenderer((TerrainLocation.BridgeLocation) location));
		else if (location instanceof TerrainLocation.ReefLocation)
			renderers.put(location, new ReefRenderer((TerrainLocation.ReefLocation) location));
		else if (location instanceof TerrainLocation.SeaLocation)
			renderers.put(location, new SeaRenderer((TerrainLocation.SeaLocation) location));
		else {
			System.err.println("ERROR in TerrainRenderer");
			System.exit(10);
		}

		return renderers.get(location);
	}

	private static Dimension size;

	public static void render(Graphics g, Point coords, Point t) {
		AbstractTerrain terrain = Universe.get().getTerrain(t.x, t.y);
		getRender(terrain, tLocations[t.y][t.x]).draw(g, coords.x, coords.y);
	}

	public static void setLocations() {
		if (size == null) size = Universe.get().getDimension();
		TerrainEnum[][] tEnum = new TerrainEnum[size.height][size.width];

		for (int i = 0; i < size.height; i++)
			for (int j = 0; j < size.width; j++)
				tEnum[i][j] = TerrainEnum.getTerrainEnum(Universe.get().getTerrain(j, i));

		tLocations = new TerrainLocation[size.height][size.width];

		for (int i = 0; i < tEnum.length; i++)
			for (int j = 0; j < tEnum[0].length; j++) {
				switch (tEnum[i][j]) {
					case sea: tLocations[i][j] = setSeaLocation(tEnum, i, j) ;break;
					case lowland:
						if (isInMap(i, j - 1) &&
								(tEnum[i][j - 1] == TerrainEnum.mountain
										|| tEnum[i][j - 1] == TerrainEnum.hill
										|| tEnum[i][j - 1] == TerrainEnum.wood))
							tLocations[i][j] = TerrainLocation.LowlandLocation.SHADOW;
						else tLocations[i][j] = TerrainLocation.LowlandLocation.NORMAL;
						break;
					case hill: tLocations[i][j] = TerrainLocation.HillLocation.NORMAL;break;
					case mountain: tLocations[i][j] = TerrainLocation.MountainLocation.NORMAL;break;
					case wood: tLocations[i][j] = TerrainLocation.WoodLocation.NORMAL;break;
					case reef: tLocations[i][j] = TerrainLocation.ReefLocation.NORMAL;break;
					case bridge:
						if (isInMap(i, j - 1) &&
								(tEnum[i][j - 1] == TerrainEnum.sea || tEnum[i][j - 1] == TerrainEnum.river))
							tLocations[i][j] = TerrainLocation.BridgeLocation.VERTICAL;
						else tLocations[i][j] = TerrainLocation.BridgeLocation.HORIZONTAL;
						break;
					case river: tLocations[i][j] = setRiverLocation(tEnum, i, j);break;
					case beach: tLocations[i][j] = setBeachLocation(tEnum, i, j);break; // TODO
					case road: tLocations[i][j] = setRoadLocation(tEnum, i, j);break;
					default: {
					}

				}
			}
	}

	private static boolean isInMap(int x, int y) {
		if (size == null) size = Universe.get().getDimension();
		return x >= 0 && x < size.height && y >= 0 && y < size.height;
	}

	private static TerrainLocation.BeachLocation setBeachLocation(TerrainEnum[][] tEnum, int x, int y) {
		TerrainEnum[] cross = terrainCross(tEnum, x, y);
		int count = 0;

		for (TerrainEnum t : cross)
			if (t == TerrainEnum.beach
					|| t == TerrainEnum.lowland
					|| t == TerrainEnum.mountain
					|| t == TerrainEnum.hill
					|| t == TerrainEnum.road
					|| t == TerrainEnum.wood) count++;

		switch (count) {
			case 3:
				if (cross[0] == TerrainEnum.sea) return TerrainLocation.BeachLocation.FILLED_BOTTOM;
				if (cross[1] == TerrainEnum.sea) return TerrainLocation.BeachLocation.FILLED_LEFT;
				if (cross[2] == TerrainEnum.sea) return TerrainLocation.BeachLocation.FILLED_TOP;
				if (cross[3] == TerrainEnum.sea) return TerrainLocation.BeachLocation.FILLED_RIGHT;
			case 2:
				for (int i = 0; i < 4; i++)
					if (cross[i] == TerrainEnum.sea)
						for (int j = i + 1; j < 4; j++)
							if (cross[j] == TerrainEnum.sea)
								switch (i + j) {
									case 1: return TerrainLocation.BeachLocation.OUTER_BOTTOM_LEFT;
									case 2: return TerrainLocation.BeachLocation.TOP; // error
									case 3:
										if (i == 0) return TerrainLocation.BeachLocation.OUTER_BOTTOM_RIGHT;
										else return TerrainLocation.BeachLocation.OUTER_UPPER_RIGHT;
									case 4: return TerrainLocation.BeachLocation.TOP; // error
									case 5: return TerrainLocation.BeachLocation.OUTER_UPPER_LEFT;
								}
			case 1:
				if (cross[0] == TerrainEnum.sea) return TerrainLocation.BeachLocation.BOTTOM;
				if (cross[1] == TerrainEnum.sea) return TerrainLocation.BeachLocation.LEFT;
				if (cross[2] == TerrainEnum.sea) return TerrainLocation.BeachLocation.TOP;
				if (cross[3] == TerrainEnum.sea) return TerrainLocation.BeachLocation.RIGHT;
			case 0:

			default:
				return TerrainLocation.BeachLocation.TOP;
		}
	}

	private static TerrainLocation.SeaLocation setSeaLocation(TerrainEnum[][] tEnum, int x, int y) {
		TerrainEnum[] cross = terrainCross(tEnum, x, y);
		int count = 0;

		for (TerrainEnum t : cross)
			if (t == TerrainEnum.lowland
					|| t == TerrainEnum.mountain
					|| t == TerrainEnum.road
					|| t == TerrainEnum.hill
					|| t == TerrainEnum.wood) count++;

		switch (count) {
			case 2:
				for (int i = 0; i < 4; i++)
					if (cross[i] == TerrainEnum.sea)
						for (int j = i + 1; j < 4; j++)
							if (cross[j] == TerrainEnum.sea)
								switch (i + j) {
									case 1: return TerrainLocation.SeaLocation.BOTTOM_LEFT;
									case 2: return TerrainLocation.SeaLocation.NORMAL;
									case 3:
										if (i == 0) return TerrainLocation.SeaLocation.BOTTOM_RIGHT;
										else return TerrainLocation.SeaLocation.TOP_LEFT;
									case 4: return TerrainLocation.SeaLocation.NORMAL;
									case 5: return TerrainLocation.SeaLocation.TOP_RIGHT;
								}
			case 1:
				if (cross[0] != TerrainEnum.sea
						&& cross[0] != TerrainEnum.beach
						&& cross[0] != TerrainEnum.bridge
						&& cross[0] != TerrainEnum.reef) return TerrainLocation.SeaLocation.TOP;
				if (cross[1] != TerrainEnum.sea
						&& cross[1] != TerrainEnum.beach
						&& cross[1] != TerrainEnum.bridge
						&& cross[1] != TerrainEnum.reef) return TerrainLocation.SeaLocation.RIGHT;
				if (cross[2] != TerrainEnum.sea
						&& cross[2] != TerrainEnum.beach
						&& cross[2] != TerrainEnum.bridge
						&& cross[2] != TerrainEnum.reef) return TerrainLocation.SeaLocation.BOTTOM;
				if (cross[3] != TerrainEnum.sea
						&& cross[3] != TerrainEnum.beach
						&& cross[3] != TerrainEnum.bridge
						&& cross[3] != TerrainEnum.reef) return TerrainLocation.SeaLocation.LEFT;
			case 0:

			default:
				return TerrainLocation.SeaLocation.NORMAL;
		}
	}

	private static TerrainLocation.RiverLocation setRiverLocation(TerrainEnum[][] tEnum, int x, int y) {
		TerrainEnum[] cross = terrainCross(tEnum, x, y);
		int count = 0;

		for (TerrainEnum t : cross)
			if (t == TerrainEnum.river) count++;

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
									case 1: return TerrainLocation.RiverLocation.TURN_TOP_RIGHT;
									case 2: return TerrainLocation.RiverLocation.VERTICAL;
									case 3:
										if (i == 0) return TerrainLocation.RiverLocation.TURN_TOP_LEFT;
										else return TerrainLocation.RiverLocation.TURN_BOTTOM_RIGHT;
									case 4: return TerrainLocation.RiverLocation.HORIZONTAL;
									case 5: return TerrainLocation.RiverLocation.TURN_BOTTOM_LEFT;
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

	private static TerrainLocation.RoadLocation setRoadLocation(TerrainEnum[][] tEnum, int x, int y) {
		TerrainEnum[] cross = terrainCross(tEnum, x, y);
		int count = 0;

		for (TerrainEnum t : cross)
			if (t == TerrainEnum.road) count++;

		switch (count) {
			case 4:
				return TerrainLocation.RoadLocation.CENTER;
			case 3:
				if (cross[0] != TerrainEnum.road) return TerrainLocation.RoadLocation.T_BOTTOM;
				if (cross[1] != TerrainEnum.road) return TerrainLocation.RoadLocation.T_LEFT;
				if (cross[2] != TerrainEnum.road) return TerrainLocation.RoadLocation.T_TOP;
				if (cross[3] != TerrainEnum.road) return TerrainLocation.RoadLocation.T_RIGHT;
			case 2:
				for (int i = 0; i < 4; i++)
					if (cross[i] == TerrainEnum.road)
						for (int j = i + 1; j < 4; j++)
							if (cross[j] == TerrainEnum.road)
								switch (i + j) {
									case 1: return TerrainLocation.RoadLocation.TURN_TOP_RIGHT;
									case 2: return TerrainLocation.RoadLocation.VERTICAL;
									case 3:
										if (i == 0) return TerrainLocation.RoadLocation.TURN_TOP_LEFT;
										else return TerrainLocation.RoadLocation.TURN_BOTTOM_RIGHT;
									case 4: return TerrainLocation.RoadLocation.HORIZONTAL;
									case 5: return TerrainLocation.RoadLocation.TURN_BOTTOM_LEFT;
								}
			case 1:
				if (cross[0] == TerrainEnum.road || cross[2] == TerrainEnum.road)
					return TerrainLocation.RoadLocation.VERTICAL;
				else return TerrainLocation.RoadLocation.HORIZONTAL;
			default:
				return TerrainLocation.RoadLocation.CENTER;

		}
	}

	private static TerrainEnum[] terrainCross(TerrainEnum[][] tEnum, int x, int y) {
		TerrainEnum[] cross = new TerrainEnum[4];
		int count = 0;

		cross[0] = isInMap(x - 1, y) ? tEnum[x - 1][y] : TerrainEnum.none;
		cross[1] = isInMap(x, y + 1) ? tEnum[x][y + 1] : TerrainEnum.none;
		cross[2] = isInMap(x + 1, y) ? tEnum[x + 1][y] : TerrainEnum.none;
		cross[3] = isInMap(x, y - 1) ? tEnum[x][y - 1] : TerrainEnum.none;

		return cross;
	}
}

