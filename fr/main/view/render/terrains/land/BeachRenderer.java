package fr.main.view.render.terrains.land;

import fr.main.model.terrains.land.Beach;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BeachRenderer extends Beach implements Renderer {
    private String imagePath;
    private transient BufferedImage image;
    private transient static BeachRenderer instance;

    private BeachRenderer(String imagePath) {
        this.imagePath = imagePath;
        update();
    }

    @Override
    public void update() {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {}
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

    public static BeachRenderer get () {
        if (instance == null) instance = new BeachRenderer ("./assets/terrains/beach.png");
        return instance;
    }
}
