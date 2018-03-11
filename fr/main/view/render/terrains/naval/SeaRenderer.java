package fr.main.view.render.terrains.naval;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import fr.main.model.terrains.TerrainLocation;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.model.terrains.naval.Sea;
import fr.main.view.render.terrains.TerrainImage;


public class SeaRenderer extends Sea implements Renderer {

	private transient Image image;
	private transient static Map<SeaLocation, SeaRenderer> instances;

	public SeaRenderer(SeaLocation location) {
		if(instances == null) instances = new HashMap<>();
		if(!instances.containsKey(location)) instances.put(location, this);
		this.location = location;
		update();
	}


	@Override
	public String getFilename () {
		return location.getPath();
	}

	@Override
	public void setImage (Image image) {
		this.image = image;
	}

	@Override
	public void update() {
		this.image = TerrainImage.get(location.getPath()).getSubImg(location.location());
	}

	public void draw (Graphics g, int x, int y) {
		if(this.image == null) {
			g.setColor (Color.cyan);
			g.fillRect (x, y, MainFrame.UNIT, MainFrame.UNIT);
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, x, y, MainFrame.UNIT, MainFrame.UNIT, null);
	}

	public static SeaRenderer get(SeaLocation loc) {
		if(instances == null) instances = new HashMap<>();
		if(!instances.containsKey(loc)) new SeaRenderer(loc);
		return instances.get(loc);
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

}

