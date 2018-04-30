package fr.main.view.render.terrains;

import static fr.main.model.TerrainEnum.beach;
import static fr.main.model.TerrainEnum.bridge;
import static fr.main.model.TerrainEnum.hill;
import static fr.main.model.TerrainEnum.lowland;
import static fr.main.model.TerrainEnum.mountain;
import static fr.main.model.TerrainEnum.none;
import static fr.main.model.TerrainEnum.reef;
import static fr.main.model.TerrainEnum.river;
import static fr.main.model.TerrainEnum.road;
import static fr.main.model.TerrainEnum.sea;
import static fr.main.model.TerrainEnum.wood;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

import fr.main.model.TerrainEnum;
import fr.main.model.Universe;
import fr.main.model.Weather;
import fr.main.model.generator.MapGenerator;
import fr.main.view.render.Renderer;
import fr.main.view.render.animations.Animation;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.Sprite;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.terrains.land.BeachRenderer;
import fr.main.view.render.terrains.land.BridgeRenderer;
import fr.main.view.render.terrains.land.HillRenderer;
import fr.main.view.render.terrains.land.LowlandRenderer;
import fr.main.view.render.terrains.land.MountainRenderer;
import fr.main.view.render.terrains.land.RiverRenderer;
import fr.main.view.render.terrains.land.RoadRenderer;
import fr.main.view.render.terrains.land.WoodRenderer;
import fr.main.view.render.terrains.naval.ReefRenderer;
import fr.main.view.render.terrains.naval.SeaRenderer;

/**
 *  Sets all Terrain locations and Renderer.
 */
public class TerrainRenderer {

	private static HashMap<TerrainLocation, Render> renderers = new HashMap<>();
	private static TerrainLocation[][] tLocations;
	private static Dimension size;


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

		public Render(TerrainLocation.GenericTerrainLocation location) {
			anim = new Animation();

			TerrainLocation loc = location.getBase();
			LinkedList<TerrainLocation.Sticker> ss = location.getStickers();

			final String normDir = TerrainLocation.getDir() + "normal/",
					snowDir = TerrainLocation.getDir() + "snow/";
			String nameNorm = "normal/" + loc.getPath(),
					nameSnow = "snow/" + loc.getPath();

			// TODO ToolkitImage casting to BufferedImage is not working, need to adapt solution without using sun.com func.
			BufferedImage baseNormal = castToBufferedImage(Sprite.get(normDir + loc.getPath()).getImage(loc.getRect())),
					baseSnow = castToBufferedImage(Sprite.get(snowDir + loc.getPath()).getImage(loc.getRect()));

			if(ss != null && ss.size() > 0)
			for(TerrainLocation.Sticker s : ss) {
				try {
					Image normImg = Sprite.get(normDir + s.loc.getPath()).getImage(s.loc.getRect()),
							snowImg = Sprite.get(snowDir + s.loc.getPath()).getImage(s.loc.getRect());
					baseNormal = joinBufferedImage(
							baseNormal,
							castToBufferedImage(normImg),
							s.x,
							s.y);
					baseSnow = joinBufferedImage(
							baseSnow,
							castToBufferedImage(snowImg),
							s.x,
							s.y);
					nameNorm += s.loc.getPath() + s.x + s.y;
					nameSnow += s.loc.getPath() + s.x + s.y;

				} catch (NullPointerException e) {
					System.out.println(e.toString());
					break;
				}
			}

			Sprite normSprite = new Sprite(nameNorm, baseNormal);
			Sprite snowSprite = new Sprite(nameSnow, baseSnow);
			AnimationState normal = new AnimationState(new SpriteList(Sprite.getSprite(nameNorm)), 20);
			AnimationState snow = new AnimationState(new SpriteList(Sprite.getSprite(nameSnow)), 20);

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

	private static Render getRender(TerrainLocation location) {
		if (renderers.containsKey(location))
			return renderers.get(location);

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
		else if (location instanceof TerrainLocation.GenericTerrainLocation)
			renderers.put(location, new SeaRenderer((TerrainLocation.GenericTerrainLocation) location));
		else {
//			System.err.println("ERROR in TerrainRenderer");
			return null;
//			System.exit(10);
		}

		return renderers.get(location);
	}

	public static void render(Graphics g, Point coords, Point t) {
		Render r = getRender(tLocations[t.y][t.x]);
		if (r != null)
				r.draw(g, coords.x, coords.y);
	}

	private static BufferedImage joinBufferedImage(BufferedImage base, BufferedImage sticker, int x, int y) {
		int width = base.getWidth();
		int height = base.getHeight();

		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = newImage.createGraphics();

		Color oldColor = g2.getColor();
		g2.setPaint(Color.BLACK);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		g2.drawImage(base, null, 0, 0);
		g2.drawImage(sticker, null, x, y);
		g2.dispose();

		return newImage;
	}

	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	private static BufferedImage castToBufferedImage(Image img) {
		if (img instanceof BufferedImage)
		{
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bImage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bImage;
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
					case sea: tLocations[i][j] = setSeaLocation(tEnum, i, j); break;
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
								(tEnum[i][j - 1] == sea || tEnum[i][j - 1] == TerrainEnum.river))
							tLocations[i][j] = TerrainLocation.BridgeLocation.VERTICAL;
						else tLocations[i][j] = TerrainLocation.BridgeLocation.HORIZONTAL;
						break;
					case river: tLocations[i][j] = setRiverLocation(tEnum, i, j);break;
					case beach: tLocations[i][j] = setBeachLocation(tEnum, i, j);break;
					case road: tLocations[i][j] = setRoadLocation(tEnum, i, j);break;
					default: {
					}

				}
			}

		// Set the cliff corners.
		TerrainEnum[] surroundings;
		TerrainLocation.GenericTerrainLocation genLoc;
		for (int i = 0; i < tEnum.length; i++)
			for (int j = 0; j < tEnum[0].length; j++) {
				surroundings = null;
				genLoc = null;

				if (tEnum[i][j] == sea || tEnum[i][j] == reef) {
					surroundings = MapGenerator.getSurroundingTerrain(tEnum, i, j);

					// TODO : add reef sticker
					genLoc = addSeaSticker(tLocations[i][j], surroundings);
					if(genLoc.listLength() > 0) tLocations[i][j] = genLoc;
				}
			}
	}

	private static TerrainLocation.GenericTerrainLocation addSeaSticker(TerrainLocation base, TerrainEnum[] surroundings) {
		final TerrainEnum[] not = {sea, beach, reef, none};
		boolean used[] = {false, false, false, false};
		TerrainLocation.GenericTerrainLocation loc = new TerrainLocation.GenericTerrainLocation(base);
		for(int i = 0; i < 8; i += 2)
			if(surroundings[i] == sea || surroundings [i] == reef) {
				if (isAny(surroundings[posMod(i - 2, 8)], not) && !isAny(surroundings[posMod(i - 1, 8)], not) && !used[(posMod(i - 1, 8) - 1) / 2])
					loc.addSticker(getSeaStickerLocation(posMod(i - 1, 8)));
				if (isAny(surroundings[posMod(i + 2, 8)], not) && !isAny(surroundings[posMod(i + 1, 8)], not) && !used[(posMod(i + 1, 8) - 1) / 2])
					loc.addSticker(getSeaStickerLocation(posMod(i + 1, 8)));
			}
		return loc;
	}

	private static int posMod(int x, int p) {
		x = x % p;
		return x >= 0 ? x : -x;
	}

	private static TerrainLocation.Sticker getSeaStickerLocation(int i) {
		TerrainLocation.Sticker sticker = null;

		// TODO replace *2 by a const program-wide

		switch (i) {
			case 1: sticker = new TerrainLocation.Sticker(TerrainLocation.SeaLocation.CORNER_TOP_RIGHT, 8 * 2, 0); 		break;
			case 3: sticker = new TerrainLocation.Sticker(TerrainLocation.SeaLocation.CORNER_BOTTOM_RIGHT, 8 * 2, 8 * 2); 	break;
			case 5: sticker = new TerrainLocation.Sticker(TerrainLocation.SeaLocation.CORNER_BOTTOM_LEFT, 0, 8 * 2);	break;
			case 7: sticker = new TerrainLocation.Sticker(TerrainLocation.SeaLocation.CORNER_TOP_LEFT, 0, 0);		break;
			default: return null;
		}
		return sticker;
	}

	private static boolean isInMap(int x, int y) {
		if (size == null) size = Universe.get().getDimension();
		return x >= 0 && x < size.height && y >= 0 && y < size.height;
	}

	private static TerrainLocation.BeachLocation setBeachLocation(TerrainEnum[][] tEnum, int x, int y) {
		TerrainEnum[] cross = terrainCross(tEnum, x, y);
		final TerrainEnum[] ground = {hill, mountain, lowland, road, wood};
		int count = 0;

		for (TerrainEnum t : cross)
			if (isAny(t, ground)) count++;

		switch (count) {
			case 3:
				if (cross[0] == sea) return TerrainLocation.BeachLocation.FILLED_BOTTOM;
				if (cross[1] == sea) return TerrainLocation.BeachLocation.FILLED_LEFT;
				if (cross[2] == sea) return TerrainLocation.BeachLocation.FILLED_TOP;
				if (cross[3] == sea) return TerrainLocation.BeachLocation.FILLED_RIGHT;
				break;
			case 2:
				for (int i = 0; i < 4; i++)
					if (cross[i] == sea)
						for (int j = i + 1; j < 4; j++)
							if (cross[j] == sea)
								switch (i + j) {
									case 1: return TerrainLocation.BeachLocation.OUTER_BOTTOM_LEFT;
									case 2: return TerrainLocation.BeachLocation.TOP; // error
									case 3:
										if (i == 0) return TerrainLocation.BeachLocation.OUTER_BOTTOM_RIGHT;
										else return TerrainLocation.BeachLocation.OUTER_UPPER_LEFT;
									case 4: return TerrainLocation.BeachLocation.TOP; // error
									case 5: return TerrainLocation.BeachLocation.OUTER_UPPER_RIGHT;
								}
			    break;
			case 1:
				if (isAny(cross[0], ground)) return TerrainLocation.BeachLocation.BOTTOM;
				if (isAny(cross[1], ground)) return TerrainLocation.BeachLocation.LEFT;
				if (isAny(cross[2], ground)) return TerrainLocation.BeachLocation.TOP;
				if (isAny(cross[3], ground)) return TerrainLocation.BeachLocation.RIGHT;
			    break;
			case 0:
				if (cross[0] == beach && cross[1] == beach) return TerrainLocation.BeachLocation.INNER_BOTTOM_LEFT;
				if (cross[0] == beach && cross[3] == beach) return TerrainLocation.BeachLocation.INNER_BOTTOM_RIGHT;
				if (cross[2] == beach && cross[1] == beach) return TerrainLocation.BeachLocation.INNER_UPPER_LEFT;
				if (cross[2] == beach && cross[3] == beach) return TerrainLocation.BeachLocation.INNER_UPPER_RIGHT;

		    break;
		}
		return TerrainLocation.BeachLocation.TOP;
	}

	private static TerrainLocation.SeaLocation setSeaLocation(TerrainEnum[][] tEnum, int x, int y) {
		TerrainEnum[] cross = terrainCross(tEnum, x, y);
		final TerrainEnum[] naval = {sea, bridge, reef, beach, river}, ground = {lowland, mountain, road, hill, wood};
		int count = 0;

		for (TerrainEnum t : cross)
			if (isAny(t, ground)) count++;

		switch (count) {
			case 2:
				for (int i = 0; i < 4; i++)
					if (isAny(cross[i], naval))
						for (int j = i + 1; j < 4; j++)
							if (isAny(cross[j], naval))
								switch (i + j) {
									case 1: return TerrainLocation.SeaLocation.BOTTOM_LEFT;
									case 2: return TerrainLocation.SeaLocation.NORMAL;
									case 3:
										if (i == 0) return TerrainLocation.SeaLocation.BOTTOM_RIGHT;
										else return TerrainLocation.SeaLocation.TOP_LEFT;
									case 4: return TerrainLocation.SeaLocation.NORMAL;
									case 5: return TerrainLocation.SeaLocation.TOP_RIGHT;
								}
		    break;
			case 1:
				if (!isAny(cross[0], naval)) return TerrainLocation.SeaLocation.TOP;
				if (!isAny(cross[1], naval)) return TerrainLocation.SeaLocation.RIGHT;
				if (!isAny(cross[2], naval)) return TerrainLocation.SeaLocation.BOTTOM;
				if (!isAny(cross[3], naval)) return TerrainLocation.SeaLocation.LEFT;
		    break;
		}
		return TerrainLocation.SeaLocation.NORMAL;
	}

	private static TerrainLocation.RiverLocation setRiverLocation(TerrainEnum[][] tEnum, int x, int y) {
		TerrainEnum[] cross = terrainCross(tEnum, x, y);
		final TerrainEnum[] ground = {lowland, mountain, wood, hill, road};
		final TerrainEnum[] naval = {river, bridge, sea};
		int count = 0;

		for (TerrainEnum t : cross)
			if (isAny(t, naval)) count++;

		switch (count) {
			case 4:
				return TerrainLocation.RiverLocation.CENTER;
			case 3:
				if (cross[0] != TerrainEnum.river && cross[0] != TerrainEnum.bridge
						&& cross[0] != TerrainEnum.sea && cross[0] != TerrainEnum.reef)
					return TerrainLocation.RiverLocation.T_BOTTOM;
				if (cross[1] != TerrainEnum.river && cross[1] != TerrainEnum.bridge
						&& cross[1] != TerrainEnum.sea && cross[0] != TerrainEnum.reef)
					return TerrainLocation.RiverLocation.T_LEFT;
				if (cross[2] != TerrainEnum.river && cross[2] != TerrainEnum.bridge
						&& cross[2] != TerrainEnum.sea && cross[0] != TerrainEnum.reef)
					return TerrainLocation.RiverLocation.T_TOP;
				if (cross[3] != TerrainEnum.river && cross[3] != TerrainEnum.bridge
						&& cross[3] != TerrainEnum.sea && cross[0] != TerrainEnum.reef)
					return TerrainLocation.RiverLocation.T_RIGHT;
		    break;
			case 2:
				for (int i = 0; i < 4; i++)
					if (isAny(cross[i], naval))
						for (int j = i + 1; j < 4; j++)
							if (isAny(cross[j], naval))
								switch (i + j) {
									case 1: return TerrainLocation.RiverLocation.TURN_TOP_RIGHT;
									case 2: return TerrainLocation.RiverLocation.VERTICAL;
									case 3:
										if (i == 0) return TerrainLocation.RiverLocation.TURN_TOP_LEFT;
										else return TerrainLocation.RiverLocation.TURN_BOTTOM_RIGHT;
									case 4: return TerrainLocation.RiverLocation.HORIZONTAL;
									case 5: return TerrainLocation.RiverLocation.TURN_BOTTOM_LEFT;
								}
		    break;
			case 1:
				if (isAny(cross[0], naval))
					if(cross[2] == TerrainEnum.sea) return TerrainLocation.RiverLocation.VERTICAL;
					else return TerrainLocation.RiverLocation.BOTTOM_END;
				if (isAny(cross[1], naval))
					if(cross[3] == TerrainEnum.sea) return TerrainLocation.RiverLocation.HORIZONTAL;
					else return TerrainLocation.RiverLocation.LEFT_END;
				if (isAny(cross[2], naval))
					if(cross[0] == TerrainEnum.sea) return TerrainLocation.RiverLocation.VERTICAL;
					else return TerrainLocation.RiverLocation.TOP_END;
				if (isAny(cross[3], naval))
					if(cross[1] == TerrainEnum.sea) return TerrainLocation.RiverLocation.HORIZONTAL;
					else return TerrainLocation.RiverLocation.RIGHT_END;
		    break;
			case 0:
				if(MapGenerator.isSandwiched(tEnum, x, y, ground)) return TerrainLocation.RiverLocation.HORIZONTAL;
				return TerrainLocation.RiverLocation.VERTICAL;
		}
		return TerrainLocation.RiverLocation.CENTER;
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
				if (cross[0] != TerrainEnum.road && cross[0] != TerrainEnum.bridge) return TerrainLocation.RoadLocation.T_BOTTOM;
				if (cross[1] != TerrainEnum.road && cross[1] != TerrainEnum.bridge) return TerrainLocation.RoadLocation.T_LEFT;
				if (cross[2] != TerrainEnum.road && cross[2] != TerrainEnum.bridge) return TerrainLocation.RoadLocation.T_TOP;
				if (cross[3] != TerrainEnum.road && cross[3] != TerrainEnum.bridge) return TerrainLocation.RoadLocation.T_RIGHT;
		    break;
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
		    break;
			case 1:
				if (cross[0] == TerrainEnum.road || cross[2] == TerrainEnum.road)
					return TerrainLocation.RoadLocation.VERTICAL;
				else return TerrainLocation.RoadLocation.HORIZONTAL;
		}
		return TerrainLocation.RoadLocation.CENTER;
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

	public static boolean isAny(TerrainEnum a, TerrainEnum[] ts) {
		for(TerrainEnum t : ts)
			if(a == t) return true;
		return false;
	}
}

