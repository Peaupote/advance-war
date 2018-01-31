package fr.main;

import java.awt.EventQueue;
import java.awt.Point;

import fr.main.model.Universe;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.Unit;
import fr.main.view.render.terrains.land.*;
import fr.main.view.render.terrains.naval.*;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.MainFrame;

/**
 * Classe qui lance le projet
 */
public class App {

  public static void main (String[] args) {
    Unit[][] units = new Unit[][] {
      {new UnitRenderer(new Point(0,0))},
      {new UnitRenderer(new Point(3,5)), new UnitRenderer(new Point(1,1))}
    };

    Terrain[][] map = new Terrain[][]{
      {new SeaRenderer(), new SeaRenderer(), new SeaRenderer(), new SeaRenderer(), new SeaRenderer(), new ReefRenderer()},
      {new SeaRenderer(), new LowlandRenderer(), new ReefRenderer(), new SeaRenderer(), new LowlandRenderer(), new SeaRenderer()},
      {new SeaRenderer(), new LowlandRenderer(), new ReefRenderer(), new SeaRenderer(), new LowlandRenderer(), new SeaRenderer()},
      {new SeaRenderer(), new LowlandRenderer(), new ReefRenderer(), new ReefRenderer(), new LowlandRenderer(), new SeaRenderer()},
      {new LowlandRenderer(), new LowlandRenderer(), new LowlandRenderer(), new LowlandRenderer(), new LowlandRenderer(), new SeaRenderer()},
      {new SeaRenderer(), new LowlandRenderer(), new LowlandRenderer(), new SeaRenderer(), new LowlandRenderer(), new SeaRenderer()},
      {new ReefRenderer(), new LowlandRenderer(), new LowlandRenderer(), new LowlandRenderer(), new LowlandRenderer(), new LowlandRenderer()}};
    Universe.save("maps/maptest.map", units, map);

    //Universe word = new Universe("maps/maptest.map");
    //System.out.println(word);
    
    EventQueue.invokeLater(MainFrame::new);
  }
}
