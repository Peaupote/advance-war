package fr.main.view.render.terrains.naval;

import java.awt.*;

import fr.main.model.terrains.TerrainLocation;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.model.terrains.naval.Reef;
import fr.main.view.render.terrains.TerrainImage;

public class ReefRenderer extends Reef implements Renderer {

	private transient Image image;
	private transient static ReefRenderer instance;

	private ReefRenderer() {
		if (instance == null) instance = this;
		this.location = ReefLocation.NORMAL;
	}

	@Override
	public void update() {
		this.image = TerrainImage.get(location.getPath()).getSubImg(location.location());
	}

	public void draw(Graphics g, int x, int y) {
		if (image == null) {
			g.setColor(Color.blue);
			g.fillRect(x, y, MainFrame.UNIT, MainFrame.UNIT);
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, x, y, MainFrame.UNIT, MainFrame.UNIT, null);
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getFilename() {
		return location.getPath();
	}

	public static ReefRenderer get() {
		if (instance == null) instance = new ReefRenderer();
		return instance;
	}

	public enum ReefLocation implements TerrainLocation {
		NORMAL(TerrainImage.Location.CENTER);

		private static final String path = "assets/terrains/cliffs.png";
		private final TerrainImage.Location location;

		ReefLocation(TerrainImage.Location loc) {
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

