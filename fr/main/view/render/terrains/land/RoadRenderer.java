package fr.main.view.render.terrains.land;

import fr.main.view.render.terrains.TerrainLocation;
import fr.main.model.terrains.land.Road;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.terrains.TerrainImage;

import java.awt.*;
import java.awt.Image;
import java.util.HashMap;

public class RoadRenderer extends Road implements Renderer {
	private transient Image image;
	private transient static HashMap<TerrainLocation.RoadLocation, RoadRenderer> instances;

	private RoadRenderer(TerrainLocation.RoadLocation location) {
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

	public static RoadRenderer get(TerrainLocation.RoadLocation loc) {
		if (instances == null) instances = new HashMap<>();
		if (!instances.containsKey(loc)) new RoadRenderer(loc);
		return instances.get(loc);
	}
}
