package fr.main.view.render.terrains.land;

import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import fr.main.model.terrains.TerrainLocation;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.model.terrains.land.Lowland;
import fr.main.view.render.terrains.TerrainImage;

import javax.imageio.ImageIO;

public class LowlandRenderer extends Lowland implements Renderer {

	private transient Image image;
	private transient static HashMap<LowlandLocation, LowlandRenderer> instances;

	private LowlandRenderer(LowlandLocation location) {
		if (instances == null) instances = new HashMap<>();
		if (!instances.containsKey(location)) instances.put(location, this);
		this.tLocation = location;
		update();
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
			g.setColor(Color.green);
			g.fillRect(x, y, MainFrame.UNIT, MainFrame.UNIT);
			return;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, x, y, MainFrame.UNIT, MainFrame.UNIT, null);
	}

	public static LowlandRenderer get(LowlandLocation loc) {
		if (instances == null) instances = new HashMap<>();
		if (!instances.containsKey(loc)) new LowlandRenderer(loc);
		return instances.get(loc);
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
}

