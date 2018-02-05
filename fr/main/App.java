package fr.main;

import java.awt.EventQueue;
import java.util.Random;
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

    Random rand = new Random();
    Terrain[][] map = new Terrain[30][30];
    for (int i = 0; i < 30; i++)
      for (int j = 0; j < 30; j++)
        switch (rand.nextInt(3)) {
          case 0: map[i][j] = new LowlandRenderer();break;
          case 1: map[i][j] = new SeaRenderer();break;
          case 2: map[i][j] = new ReefRenderer();break;
        }

    Unit[][] units = new Unit[][] {
            {new UnitRenderer(new Point(0,0))},
            {new UnitRenderer(new Point(3,5)), new UnitRenderer(new Point(1,1))}
    };

    Universe.save("maps/maptest.map", units, map);

    //Universe word = new Universe("maps/maptest.map");
    //System.out.println(word);


    
    EventQueue.invokeLater(MainFrame::new);
  }
}
