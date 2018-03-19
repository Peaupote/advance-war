package fr.main.view.render;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class  Renderer {

    public abstract void draw (Graphics g, int x, int y);

}

