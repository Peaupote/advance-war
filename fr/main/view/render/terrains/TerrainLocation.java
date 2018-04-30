package fr.main.view.render.terrains;


import fr.main.view.render.sprites.ScaleRect;

import java.io.Serializable;
import java.util.LinkedList;


public interface TerrainLocation extends Serializable{

	public static String getDir () {
		return "./assets/terrains/";
	}

	public String getPath();
	public ScaleRect getRect();
	public ScaleRect getDefaultRect();

	public enum TerrainImageRect {
		LEFT(0, 17), RIGHT(34, 17), TOP(17, 0), BOTTOM(17, 34), CENTER(17, 17),
		TOP_RIGHT(34, 0), TOP_LEFT(0, 0), BOTTOM_RIGHT(34, 34), BOTTOM_LEFT(0, 34),
		CENTER_TOP_LEFT(17, 17, 8, 8), CENTER_TOP_RIGHT(25, 17, 8, 8),
		CENTER_BOTTOM_LEFT(17, 25, 8, 8), CENTER_BOTTOM_RIGHT(25, 25, 8, 8);

		public final int x, y, w, h;

		TerrainImageRect(int x, int y) {
			this(x, y, 16, 16);
		}

		TerrainImageRect(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		ScaleRect getRect() {
			return new ScaleRect(x, y, w, h);
		}
	}

	public enum BeachLocation implements TerrainLocation {
		LEFT(1, TerrainImageRect.LEFT), RIGHT(1, TerrainImageRect.RIGHT),
		TOP(1, TerrainImageRect.TOP), BOTTOM(1, TerrainImageRect.BOTTOM),
		FILLED_LEFT(0, TerrainImageRect.RIGHT), FILLED_RIGHT(0, TerrainImageRect.LEFT),
		FILLED_TOP(0, TerrainImageRect.BOTTOM), FILLED_BOTTOM(0, TerrainImageRect.TOP),
		INNER_BOTTOM_RIGHT(1, TerrainImageRect.BOTTOM_RIGHT), INNER_BOTTOM_LEFT(1, TerrainImageRect.BOTTOM_LEFT),
		INNER_UPPER_RIGHT(1, TerrainImageRect.TOP_RIGHT), INNER_UPPER_LEFT(1, TerrainImageRect.TOP_LEFT),
		OUTER_BOTTOM_RIGHT(0, TerrainImageRect.BOTTOM_RIGHT), OUTER_BOTTOM_LEFT(0, TerrainImageRect.BOTTOM_LEFT),
		OUTER_UPPER_RIGHT(0, TerrainImageRect.TOP_RIGHT), OUTER_UPPER_LEFT(0, TerrainImageRect.TOP_LEFT);

		private static final String[] paths = {"beach1.png", "beach2.png"};
		private final TerrainImageRect location;
		private final int index;

		BeachLocation(int index, TerrainImageRect loc) {
			this.index = index;
			this.location = loc;
		}

		public String getPath() {
			return paths[index];
		}
		public TerrainImageRect location() {
			return location;
		}

		@Override
		public ScaleRect getRect() {
			return location.getRect();
		}

		@Override
		public ScaleRect getDefaultRect() {
			return TOP.getRect();
		}
	}

	public enum BridgeLocation implements TerrainLocation {
		HORIZONTAL(TerrainImageRect.TOP_LEFT), VERTICAL(TerrainImageRect.LEFT);

		private TerrainImageRect location;
		private String path = "bridge.png";

		BridgeLocation(TerrainImageRect loc) {
			this.location = loc;
		}

		@Override
		public String getPath() {
			return path;
		}

		public TerrainImageRect location() {
			return location;
		}

		@Override
		public ScaleRect getRect() {
			return location.getRect();
		}

		@Override
		public ScaleRect getDefaultRect() {
			return HORIZONTAL.getDefaultRect();
		}
	}

	public enum HillLocation implements TerrainLocation {
		NORMAL(TerrainImageRect.TOP_LEFT);

		private TerrainImageRect location;
		private String path = "hill.png";

		HillLocation(TerrainImageRect loc) {
			this.location = loc;
		}

		@Override
		public String getPath() {
			return path;
		}

		public TerrainImageRect location() {
			return location;
		}

		@Override
		public ScaleRect getRect() {
			return location.getRect();
		}

		@Override
		public ScaleRect getDefaultRect() {
			return NORMAL.getRect();
		}
	}

	public enum LowlandLocation implements TerrainLocation {
		NORMAL(0, TerrainImageRect.CENTER), SHADOW(1, TerrainImageRect.TOP_LEFT);

		private TerrainImageRect location;
		private int index;
		private String[] path = { "rivers1.png", "lowland_shadow.png"};

		LowlandLocation(int index, TerrainImageRect loc) {
			this.index = index;
			this.location = loc;
		}

		@Override
		public String getPath() {
			return path[index];
		}

		public TerrainImageRect location() {
			return location;
		}

		@Override
		public ScaleRect getRect() {
			return location.getRect();
		}

		@Override
		public ScaleRect getDefaultRect() {
			return NORMAL.getRect();
		}
	}

	public enum WoodLocation implements TerrainLocation {
		NORMAL(TerrainImageRect.CENTER);

		private static final String path = "beach2.png";
		private final TerrainImageRect location;

		WoodLocation(TerrainImageRect loc) {
			this.location = loc;
		}

		public String getPath() {
			return path;
		}

		public TerrainImageRect location() {
			return location;
		}

		@Override
		public ScaleRect getRect() {
			return location.getRect();
		}

		@Override
		public ScaleRect getDefaultRect() {
			return NORMAL.getRect();
		}
	}

	public enum ReefLocation implements TerrainLocation {
		NORMAL(TerrainImageRect.CENTER);

		private static final String path = "cliffs.png";
		private final TerrainImageRect location;

		ReefLocation(TerrainImageRect loc) {
			this.location = loc;
		}

		public String getPath() {
			return path;
		}

		public TerrainImageRect location() {
			return location;
		}

		@Override
		public ScaleRect getRect() {
			return location.getRect();
		}

		@Override
		public ScaleRect getDefaultRect() {
			return NORMAL.getRect();
		}
	}

	public enum MountainLocation implements TerrainLocation {
		NORMAL(TerrainImageRect.TOP_LEFT);

		private static final String path = "mountain.png";
		private final TerrainImageRect location;

		MountainLocation(TerrainImageRect location) {
			this.location = location;
		}

		public String getPath() {
			return path;
		}

		public TerrainImageRect location() {
			return location;
		}

		@Override
		public ScaleRect getRect() {
			return location.getRect();
		}

		@Override
		public ScaleRect getDefaultRect() {
			return NORMAL.getRect();
		}
	}

	public enum RiverLocation implements TerrainLocation {
		HORIZONTAL(0, TerrainImageRect.TOP), VERTICAL(0, TerrainImageRect.LEFT),
		CENTER(1, TerrainImageRect.CENTER),
		LEFT_END(1, TerrainImageRect.TOP_LEFT), RIGHT_END(1, TerrainImageRect.TOP_RIGHT),
		TOP_END(1, TerrainImageRect.BOTTOM_LEFT), BOTTOM_END(1, TerrainImageRect.BOTTOM_RIGHT),
		T_TOP(1, TerrainImageRect.BOTTOM), T_RIGHT(1, TerrainImageRect.LEFT),
		T_LEFT(1, TerrainImageRect.RIGHT), T_BOTTOM(1, TerrainImageRect.TOP),
		TURN_TOP_RIGHT(0, TerrainImageRect.BOTTOM_LEFT), TURN_TOP_LEFT(0, TerrainImageRect.BOTTOM_RIGHT),
		TURN_BOTTOM_RIGHT(0, TerrainImageRect.TOP_LEFT), TURN_BOTTOM_LEFT(0, TerrainImageRect.TOP_RIGHT);

		private static final String[] paths = { "rivers1.png", "rivers2.png"};
		private final TerrainImageRect location;
		private final int index;

		RiverLocation(int index, TerrainImageRect loc) {
			this.index = index;
			this.location = loc;
		}

		public String getPath() {
			return paths[index];
		}

		public TerrainImageRect location() {
			return location;
		}

		@Override
		public ScaleRect getRect() {
			return location.getRect();
		}

		@Override
		public ScaleRect getDefaultRect() {
			return CENTER.getRect();
		}
	}

	public enum SeaLocation implements TerrainLocation {
		NORMAL(0, TerrainImageRect.CENTER),
		LEFT(1, TerrainImageRect.LEFT), RIGHT(1, TerrainImageRect.RIGHT),
		TOP(1, TerrainImageRect.TOP), BOTTOM(1, TerrainImageRect.BOTTOM),
		TOP_LEFT(1, TerrainImageRect.TOP_LEFT), TOP_RIGHT(1, TerrainImageRect.TOP_RIGHT),
		BOTTOM_LEFT(1, TerrainImageRect.BOTTOM_LEFT), BOTTOM_RIGHT(1, TerrainImageRect.BOTTOM_RIGHT),
		CORNER_TOP_LEFT(2, TerrainImageRect.CENTER_TOP_LEFT), CORNER_TOP_RIGHT(2, TerrainImageRect.CENTER_TOP_RIGHT),
		CORNER_BOTTOM_LEFT(2, TerrainImageRect.CENTER_BOTTOM_LEFT), CORNER_BOTTOM_RIGHT(2, TerrainImageRect.CENTER_BOTTOM_RIGHT);

		private static final String[] paths = {
				"beach1.png", "cliffs.png", "cliffs2.png"
		};
		private final TerrainImageRect location;
		private final int index;

		SeaLocation(int index, TerrainImageRect loc) {
			this.index = index;
			this.location = loc;
		}

		public String getPath() {
			return paths[index];
		}
		public TerrainImageRect location() {
			return location;
		}

		@Override
		public ScaleRect getRect() {
			return location.getRect();
		}

		@Override
		public ScaleRect getDefaultRect() {
			return NORMAL.getDefaultRect();
		}
	}

	public enum RoadLocation implements TerrainLocation {
		HORIZONTAL(0, TerrainImageRect.LEFT), VERTICAL(0, TerrainImageRect.BOTTOM_LEFT),
		CENTER(1, TerrainImageRect.CENTER),
		T_TOP(1, TerrainImageRect.BOTTOM), T_RIGHT(1, TerrainImageRect.LEFT),
		T_LEFT(1, TerrainImageRect.RIGHT), T_BOTTOM(1, TerrainImageRect.TOP),
		TURN_TOP_RIGHT(1, TerrainImageRect.BOTTOM_LEFT), TURN_TOP_LEFT(1, TerrainImageRect.BOTTOM_RIGHT),
		TURN_BOTTOM_RIGHT(1, TerrainImageRect.TOP_LEFT), TURN_BOTTOM_LEFT(1, TerrainImageRect.TOP_RIGHT);

		private static final String[] paths = { "roads1.png", "roads2.png"};
		private final TerrainImageRect location;
		private final int index;

		RoadLocation(int index, TerrainImageRect loc) {
			this.index = index;
			this.location = loc;
		}

		public String getPath() {
			return paths[index];
		}

		public TerrainImageRect location() {
			return location;
		}

		@Override
		public ScaleRect getRect() {
			return location.getRect();
		}

		@Override
		public ScaleRect getDefaultRect() {
			return CENTER.getRect();
		}
	}

	public class GenericTerrainLocation implements TerrainLocation {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6634774216280256727L;
		@SuppressWarnings("unused")
		private String path;
		@SuppressWarnings("unused")
		private String name;
		private LinkedList<Sticker> stickers;
		private final TerrainLocation base;

		public GenericTerrainLocation (TerrainLocation base) {
			this.base = base;
			this.stickers = new LinkedList<>();
			setPath();
		}

		public GenericTerrainLocation (TerrainLocation base, Sticker s) {
			this.base = base;
			LinkedList<Sticker> ss = new LinkedList<>();
			ss.add(s);

			stickers = ss;

			setPath();
		}

		public GenericTerrainLocation (TerrainLocation base, LinkedList<Sticker> ss) {
			this.stickers = ss;
			this.base = base;
			setPath();
		}

		public void addSticker(Sticker sticker) {
			this.stickers.add(sticker);
			addToPath(sticker);
		}

		private void setPath() {
			this.path = base.getPath();
			for (Sticker s : stickers)
				addToPath(s);
		}

		private void addToPath (Sticker s) {
			path += s.loc.getPath() + s.x + "_" + s.y;
		}

		@Override
		public ScaleRect getRect() {
			return base.getRect(); // TODO.
		}

		@Override
		public ScaleRect getDefaultRect() {
			return base.getRect(); // TODO.
		}

		@Override
		public String getPath() {
			return base.getPath(); // TODO.
		}

		public TerrainLocation getBase() {
			return base;
		}

		public LinkedList<Sticker> getStickers() {
			return stickers;
		}

		public int listLength () {
			return stickers.size();
		}

		public boolean isEmpty() {
			return stickers.isEmpty();
		}
	}

	public class Sticker {
		public final TerrainLocation loc;
		public final int x, y;


		public Sticker(TerrainLocation loc, int x, int y) {
			this.loc = loc;
			this.x = x;
			this.y = y;
		}

		@SuppressWarnings("unused")
		private boolean is(Sticker s) {
			return s.loc == this.loc && s.y == this.y && s.x == this.x;
		}

		@SuppressWarnings("unused")
		private boolean is(TerrainLocation loc, int x, int y) {
			return loc == this.loc && y == this.y && x == this.x;
		}
	}
}
