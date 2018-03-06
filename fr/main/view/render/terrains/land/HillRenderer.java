package fr.main.view.render.terrains.land;

import fr.main.model.terrains.TerrainLocation;
import fr.main.model.terrains.land.Hill;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.terrains.TerrainImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class HillRenderer extends Hill implements Renderer {
	private transient Image image;
	private transient static HillRenderer instance;

	private HillRenderer(HillLocation location) {
		if (instance == null) instance = this;
		this.tLocation = location;
		update();
	}

	private HillRenderer() {
		this(HillLocation.NORMAL);
	}

	@Override
	public String getFilename() {
		return tLocation.getPath();
	}

	@Override
	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public void update() {
		this.image = TerrainImage.get(tLocation.getPath()).getSubImg(tLocation.location());
	}

	public void draw(Graphics g, int x, int y) {
		if (image == null) {
			g.setColor(Color.pink);
			g.fillRect(x, y, MainFrame.UNIT, MainFrame.UNIT);
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, x, y, MainFrame.UNIT, MainFrame.UNIT, null);
	}

	public static HillRenderer get() {
		if (instance == null) instance = new HillRenderer();
		return instance;
	}

	public static HillRenderer get(HillLocation loc) {
		return get();
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
}
