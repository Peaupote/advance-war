package fr.main.view.render.terrains.land;

import fr.main.model.terrains.TerrainLocation;
import fr.main.model.terrains.land.Mountain;
import fr.main.model.terrains.naval.NavalTerrain;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.terrains.TerrainImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class MountainRenderer extends Mountain implements Renderer {
	private transient Image image;
	private transient static MountainRenderer instance;

	private MountainRenderer(MountainLocation location) {
		if (instance == null) instance = this;
		this.location = location;
		update();
	}

	private MountainRenderer() {
		this(MountainLocation.NORMAL);
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
			g.setColor(Color.lightGray);
			g.fillRect(x, y, MainFrame.UNIT, MainFrame.UNIT);
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, x, y - 7, MainFrame.UNIT, MainFrame.UNIT + 7, null);
	}

	public static MountainRenderer get(MountainLocation loc) {
		if (instance == null) instance = new MountainRenderer();
		return instance;
	}

	public static MountainRenderer get() {
		return get(MountainLocation.NORMAL);
	}

	protected enum WoodLocation implements TerrainLocation {
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
}
