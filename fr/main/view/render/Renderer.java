package fr.main.view.render;

import java.awt.Graphics;
import java.awt.Graphics2D;

public interface Renderer {

    void draw (Graphics g, int x, int y);
    void update();

}

