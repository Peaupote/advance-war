package fr.main.view.render.terrains;


import fr.main.view.AbstractImage;

import java.io.Serializable;

public interface TerrainLocation extends Serializable{
	public String getPath();
	public TerrainImage.Location location();

	public enum BeachLocation implements TerrainLocation {
		LEFT(1, TerrainImage.Location.LEFT), RIGHT(1, TerrainImage.Location.RIGHT),
		TOP(1, TerrainImage.Location.TOP), BOTTOM(1, TerrainImage.Location.BOTTOM),
		FILLED_LEFT(0, TerrainImage.Location.LEFT), FILLED_RIGHT(0, TerrainImage.Location.RIGHT),
		FILLED_TOP(0, TerrainImage.Location.TOP), FILLED_BOTTOM(0, TerrainImage.Location.BOTTOM),
		INNER_BOTTOM_RIGHT(1, TerrainImage.Location.BOTTOM_RIGHT), INNER_BOTTOM_LEFT(1, TerrainImage.Location.BOTTOM_LEFT),
		INNER_UPPER_RIGHT(1, TerrainImage.Location.TOP_RIGHT), INNER_UPPER_LEFT(1, TerrainImage.Location.TOP_LEFT),
		OUTER_BOTTOM_RIGHT(0, TerrainImage.Location.BOTTOM_RIGHT), OUTER_BOTTOM_LEFT(0, TerrainImage.Location.BOTTOM_LEFT),
		OUTER_UPPER_RIGHT(0, TerrainImage.Location.TOP_RIGHT), OUTER_UPPER_LEFT(0, TerrainImage.Location.TOP_LEFT);

		private static final String[] paths = {"assets/terrains/beach1.png", "assets/terrains/beach2.png"};
		private final TerrainImage.Location location;
		private final int index;

		BeachLocation(int index, TerrainImage.Location loc) {
			this.index = index;
			this.location = loc;
		}

		public String getPath() {
			return paths[index];
		}
		public TerrainImage.Location location() {
			return location;
		}
	}

	public enum BridgeLocation implements TerrainLocation {
		HORIZONTAL(TerrainImage.Location.TOP_LEFT), VERTICAL(TerrainImage.Location.LEFT);

		private TerrainImage.Location location;
		private String path = "assets/terrains/bridge.png";

		BridgeLocation(TerrainImage.Location loc) {
			this.location = loc;
		}

		@Override
		public String getPath() {
			return path;
		}

		public TerrainImage.Location location() {
			return location;
		}
	}

	public enum HillLocation implements TerrainLocation {
		NORMAL(TerrainImage.Location.TOP_LEFT);

		private TerrainImage.Location location;
		private String path = "assets/terrains/hill.png";

		HillLocation(TerrainImage.Location loc) {
			this.location = loc;
		}

		@Override
		public String getPath() {
			return path;
		}

		public TerrainImage.Location location() {
			return location;
		}
	}

	public enum LowlandLocation implements TerrainLocation {
		NORMAL(0, TerrainImage.Location.CENTER), SHADOW(1, TerrainImage.Location.TOP_LEFT);

		private TerrainImage.Location location;
		private int index;
		private String[] path = {"assets/terrains/rivers1.png", "assets/terrains/lowland_shadow.png"};

		LowlandLocation(int index, TerrainImage.Location loc) {
			this.index = index;
			this.location = loc;
		}

		@Override
		public String getPath() {
			return path[index];
		}

		public TerrainImage.Location location() {
			return location;
		}
	}

	public enum WoodLocation implements TerrainLocation {
		NORMAL(TerrainImage.Location.TOP_LEFT);

		private static final String path = "assets/terrains/mountain.png";
		private final TerrainImage.Location location;

		WoodLocation(TerrainImage.Location loc) {
			this.location = loc;
		}

		public String getPath() {
			return path;
		}

		public TerrainImage.Location location() {
			return location;
		}
	}

	public enum ReefLocation implements TerrainLocation {
		NORMAL(TerrainImage.Location.CENTER);

		private static final String path = "assets/terrains/cliffs.png";
		private final TerrainImage.Location location;

		ReefLocation(TerrainImage.Location loc) {
			this.location = loc;
		}

		public String getPath() {
			return path;
		}

		public TerrainImage.Location location() {
			return location;
		}
	}

	public enum MountainLocation implements TerrainLocation {
		NORMAL(TerrainImage.Location.TOP_LEFT);

		private static final String path = "assets/terrains/mountain.png";
		private final TerrainImage.Location location;

		MountainLocation(TerrainImage.Location location) {
			this.location = location;
		}

		public String getPath() {
			return path;
		}

		public TerrainImage.Location location() {
			return location;
		}
	}

	public enum RiverLocation implements TerrainLocation {
		HORIZONTAL(0, TerrainImage.Location.TOP), VERTICAL(0, TerrainImage.Location.LEFT),
		CENTER(1, TerrainImage.Location.CENTER),
		LEFT_END(1, TerrainImage.Location.TOP_LEFT), RIGHT_END(1, TerrainImage.Location.TOP_RIGHT),
		TOP_END(1, TerrainImage.Location.BOTTOM_LEFT), BOTTOM_END(1, TerrainImage.Location.BOTTOM_RIGHT),
		T_TOP(1, TerrainImage.Location.BOTTOM), T_RIGHT(1, TerrainImage.Location.LEFT),
		T_LEFT(1, TerrainImage.Location.RIGHT), T_BOTTOM(1, TerrainImage.Location.TOP),
		TURN_TOP_RIGHT(0, TerrainImage.Location.BOTTOM_LEFT), TURN_TOP_LEFT(0, TerrainImage.Location.BOTTOM_RIGHT),
		TURN_BOTTOM_RIGHT(0, TerrainImage.Location.TOP_LEFT), TURN_BOTTOM_LEFT(0, TerrainImage.Location.TOP_RIGHT);

		private static final String[] paths = {"assets/terrains/rivers1.png", "assets/terrains/rivers2.png"};
		private final TerrainImage.Location location;
		private final int index;

		RiverLocation(int index, TerrainImage.Location loc) {
			this.index = index;
			this.location = loc;
		}

		public String getPath() {
			return paths[index];
		}

		public TerrainImage.Location location() {
			return location;
		}
	}

	public enum SeaLocation implements TerrainLocation {
		NORMAL(0, TerrainImage.Location.CENTER),
		LEFT(1, TerrainImage.Location.LEFT), RIGHT(1, TerrainImage.Location.RIGHT),
		TOP(1, TerrainImage.Location.TOP), BOTTOM(1, TerrainImage.Location.BOTTOM),
		TOP_LEFT(1, TerrainImage.Location.TOP_LEFT), TOP_RIGHT(1, TerrainImage.Location.TOP_RIGHT),
		BOTTOM_LEFT(1, TerrainImage.Location.BOTTOM_LEFT), BOTTOM_RIGHT(1, TerrainImage.Location.BOTTOM_RIGHT);

		private static final String[] paths = {"assets/terrains/beach1.png", "assets/terrains/cliffs.png"};
		private final TerrainImage.Location location;
		private final int index;

		SeaLocation(int index, TerrainImage.Location loc) {
			this.index = index;
			this.location = loc;
		}

		public String getPath() {
			return paths[index];
		}
		public TerrainImage.Location location() {
			return location;
		}
	}

	public enum RoadLocation implements TerrainLocation {
		HORIZONTAL(0, TerrainImage.Location.LEFT), VERTICAL(0, TerrainImage.Location.BOTTOM_LEFT),
		CENTER(1, TerrainImage.Location.CENTER),
		T_TOP(1, TerrainImage.Location.BOTTOM), T_RIGHT(1, TerrainImage.Location.LEFT),
		T_LEFT(1, TerrainImage.Location.RIGHT), T_BOTTOM(1, TerrainImage.Location.TOP),
		TURN_TOP_RIGHT(1, TerrainImage.Location.BOTTOM_LEFT), TURN_TOP_LEFT(1, TerrainImage.Location.BOTTOM_RIGHT),
		TURN_BOTTOM_RIGHT(1, TerrainImage.Location.TOP_LEFT), TURN_BOTTOM_LEFT(1, TerrainImage.Location.TOP_RIGHT);

		private static final String[] paths = {"assets/terrains/roads1.png", "assets/terrains/roads2.png"};
		private final TerrainImage.Location location;
		private final int index;

		RoadLocation(int index, TerrainImage.Location loc) {
			this.index = index;
			this.location = loc;
		}

		public String getPath() {
			return paths[index];
		}

		public TerrainImage.Location location() {
			return location;
		}
	}
}
