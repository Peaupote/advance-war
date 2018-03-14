package fr.main.view.render.terrains.land;

import fr.main.view.render.terrains.TerrainLocation;
import fr.main.model.terrains.land.Bridge;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.terrains.TerrainImage;

import java.awt.*;
import java.awt.Image;
import java.util.HashMap;

public class BridgeRenderer extends Bridge implements Renderer {
	private transient Image image;
	private transient static HashMap<BridgeLocation, BridgeRenderer> instances;

	private BridgeRenderer(BridgeLocation location) {
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
			g.setColor(Color.darkGray);
			g.fillRect(x, y, MainFrame.UNIT, MainFrame.UNIT);
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, x, y, MainFrame.UNIT, MainFrame.UNIT, null);
	}

	public static BridgeRenderer get(BridgeLocation loc) {
		if (instances == null) instances = new HashMap<>();
		if (!instances.containsKey(loc)) new BridgeRenderer(loc);
		return instances.get(loc);
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
}
