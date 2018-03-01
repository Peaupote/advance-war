package fr.main.view.render.terrains.naval;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;

import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.model.terrains.naval.Reef;

public class ReefRenderer extends Reef implements Renderer {

  private transient static ReefRenderer instance;

  private ReefRenderer () {}

  public void draw (Graphics g, int x, int y) {
    g.setColor (Color.blue);
    g.fillRect (x, y, MainFrame.UNIT, MainFrame.UNIT);
  }

  public void setImage (Image image) {}

  public String getFilename () { return ""; }

  public static ReefRenderer get () {
    if (instance == null) instance = new ReefRenderer();
    return instance;
  }
}

