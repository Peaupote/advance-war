package fr.main.view.render.terrains.naval;

import fr.main.model.terrains.TerrainLocation;
import fr.main.model.terrains.land.River;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.terrains.TerrainImage;

import java.awt.*;
import java.util.HashMap;

public class RiverRenderer extends River implements Renderer {
	private transient Image image;
	private transient static HashMap<RiverLocation, RiverRenderer> instances;

	private RiverRenderer(RiverLocation location) {
		if (instances == null) instances = new HashMap<>();
		if (!instances.containsKey(location)) instances.put(location, this);
		this.location = location;
		update();
	}

	@Override
	public String getFilename() {
		return location.getPath();
	}

	@Override
	public void update() {
		this.image = TerrainImage.get(location.getPath()).getSubImg(location.location());
	}

	@Override
	public void setImage(Image image) {
		this.image = image;
	}

	public void draw(Graphics g, int x, int y) {
		if (image == null) {
			g.setColor(Color.cyan);
			g.fillRect(x, y, MainFrame.UNIT, MainFrame.UNIT);
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, x, y, MainFrame.UNIT, MainFrame.UNIT, null);
	}

	public static RiverRenderer get(RiverLocation loc) {
		if (instances == null) instances = new HashMap<>();
		if (!instances.containsKey(loc)) new RiverRenderer(loc);
		return instances.get(loc);
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
}
