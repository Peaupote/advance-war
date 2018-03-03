package fr.main.view.render.terrains.land;

import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.land.Beach;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.terrains.TerrainImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class BeachRenderer extends Beach implements Renderer {
    private BeachLocation bLocation;
    private transient Image image;
    private transient static HashMap<BeachLocation, BeachRenderer> instances;

    private BeachRenderer(BeachLocation location) {
        if(!instances.containsKey(location)) instances.put(location, this);
        this.bLocation = location;
        update();
    }

    private BeachRenderer(Image image) {
        this.image = image;
    }

    @Override
    public String getFilename () {
        return bLocation.getPath();
    }

    @Override
    public void update() {
        this.image = TerrainImage.get(bLocation.getPath()).getSubImg(bLocation.location);
    }

    @Override
    public void setImage (Image image) {
        this.image = image;
    }

    public void draw (Graphics g, int x, int y) {
        if(image == null) {
            g.setColor (Color.yellow);
            g.fillRect (x, y, MainFrame.UNIT, MainFrame.UNIT);
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, x, y, null);
    }

    public static BeachRenderer get (BeachLocation loc) {
        if(!instances.containsKey(loc)) instances.put(loc, new BeachRenderer(loc));
        return instances.get(loc);
    }

    enum BeachLocation {
        LEFT(1, TerrainImage.Location.LEFT), RIGHT(1, TerrainImage.Location.RIGHT),
        TOP(1, TerrainImage.Location.TOP), BOTTOM(1, TerrainImage.Location.BOTTOM),
        FILLED_LEFT(0, TerrainImage.Location.LEFT), FILLED_RIGHT(0, TerrainImage.Location.RIGHT),
        FILLED_TOP(0, TerrainImage.Location.TOP), FILLED_BOTTOM(0, TerrainImage.Location.BOTTOM),
        INNER_BOTTOM_RIGHT(1, TerrainImage.Location.BOTTOM_RIGHT), INNER_BOTTOM_LEFT(1, TerrainImage.Location.BOTTOM_LEFT),
        INNER_UPPER_RIGHT(1, TerrainImage.Location.TOP_RIGHT), INNER_UPPER_LEFT(1, TerrainImage.Location.TOP_LEFT),
        OUTER_BOTTOM_RIGHT(0, TerrainImage.Location.BOTTOM_RIGHT), OUTER_BOTTOM_LEFT(0, TerrainImage.Location.BOTTOM_LEFT),
        OUTER_UPPER_RIGHT(0, TerrainImage.Location.TOP_RIGHT), OUTER_UPPER_LEFT(0, TerrainImage.Location.TOP_LEFT);

        private static final String[] paths = {"assets/terrains/beach1.png", "assets/terrains/beach2.png"};
        public final TerrainImage.Location location;
        private final int index;

        BeachLocation(int index, TerrainImage.Location loc) {
            this.index = index;
            this.location = loc;
        }

        public String getPath() {
          return paths[index];
        }
    }
}
