package fr.main.view.render.terrains.land;

import fr.main.model.terrains.TerrainLocation;
import fr.main.model.terrains.land.Road;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.terrains.TerrainImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class RoadRenderer extends Road implements Renderer {
	private transient Image image;
	private transient static HashMap<RoadLocation, RoadRenderer> instances;

	private RoadRenderer(RoadLocation location) {
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
			g.setColor(Color.gray);
			g.fillRect(x, y, MainFrame.UNIT, MainFrame.UNIT);
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, x, y, null);
	}

	public static RoadRenderer get(RoadLocation loc) {
		if (instances == null) instances = new HashMap<>();
		if (!instances.containsKey(loc)) new RoadRenderer(loc);
		return instances.get(loc);
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
