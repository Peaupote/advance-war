package fr.main.view.render.terrains.land;

import fr.main.view.render.terrains.TerrainLocation;
import fr.main.model.terrains.land.Hill;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.terrains.TerrainImage;

import java.awt.*;
import java.awt.Image;

public class HillRenderer extends Hill implements Renderer {
	private transient Image image;
	private transient static HillRenderer instance;

	private HillRenderer(TerrainLocation.HillLocation location) {
		if (instance == null) instance = this;
		this.location = location;
		update();
	}

	private HillRenderer() {
		this(TerrainLocation.HillLocation.NORMAL);
	}

	@Override
	public String getFilename() {
		return location.getPath();
	}

	@Override
	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public void update() {
		this.image = TerrainImage.get(location.getPath()).getSubImg(location.location());
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

	public static HillRenderer get(TerrainLocation.HillLocation loc) {
		return get();
	}


}
