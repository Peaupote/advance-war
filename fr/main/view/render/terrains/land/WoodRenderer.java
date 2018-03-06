package fr.main.view.render.terrains.land;

import fr.main.model.terrains.TerrainLocation;
import fr.main.model.terrains.land.Wood;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.terrains.TerrainImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class WoodRenderer extends Wood implements Renderer {
	private transient Image image;
	private transient static WoodRenderer instance;

	private WoodRenderer(WoodLocation location) {
		if (instance == null) instance = this;
		this.tLocation = location;
		update();
	}

	private WoodRenderer() {
		this(WoodLocation.NORMAL);
	}

	@Override
	public String getFilename() {
		return tLocation.getPath();
	}

	@Override
	public void update() {
		this.image = TerrainImage.get(tLocation.getPath()).getSubImg(tLocation.location());
	}

	@Override
	public void setImage(Image image) {
		this.image = image;
	}

	public void draw(Graphics g, int x, int y) {
		if (image == null) {
			g.setColor(Color.green);
			g.fillRect(x, y, MainFrame.UNIT, MainFrame.UNIT);
			return;
		}


		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, x, y, MainFrame.UNIT, MainFrame.UNIT, null);
	}

	public static WoodRenderer get() {
		if (instance == null) instance = new WoodRenderer();
		return instance;
	}

	public static WoodRenderer get(WoodLocation loc) {
		return get();
	}

	public enum WoodLocation implements TerrainLocation {
		NORMAL(TerrainImage.Location.CENTER);

		private static final String path = "assets/terrains/beach2.png";
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
}
