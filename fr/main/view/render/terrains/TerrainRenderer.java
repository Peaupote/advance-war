package fr.main.view.render.terrains;

import java.awt.*;

import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.Direction;
import fr.main.model.terrains.land.*;
import fr.main.model.terrains.naval.*;

import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
//import fr.main.view.render.terrains.land.*;
import fr.main.view.render.terrains.naval.*;
import fr.main.view.render.animations.*;

public class TerrainRenderer {


  public static class Render extends Renderer {

    protected Animation anim;

    public void draw (Graphics g, int x, int y) {
      anim.draw(g, x, y);
    }

  }

  public static Render getRender (AbstractTerrain terrain) {
    return null; 
  }

  public SeaRenderer sea = new SeaRenderer();
  public static void render(Graphics g, Point coords, AbstractTerrain terrain) {
    sea.draw(g, coords.x, coords.y);
  }

}

