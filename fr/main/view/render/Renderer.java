package fr.main.view.render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public interface Renderer {

    void draw (Graphics g, int x, int y);
    String getFilename ();
    void setImage (Image image);

    default public void update() {
        try {
            // TODO: make real stuff here
            setImage(ImageIO.read(new File (getFilename())));
        } catch (IOException e) {
            setImage(null);
            System.err.println(e.getMessage());
        }
    }

}

