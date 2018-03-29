package fr.main.view.render;

import java.awt.Graphics;

/**
 * Representing element drawable at some point of the screen 
 */
public abstract class  Renderer {

    public abstract void draw (Graphics g, int x, int y);

}

