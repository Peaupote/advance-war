package fr.main;

import java.awt.EventQueue;
import java.util.Random;
import java.awt.Point;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.model.terrains.Terrain;
import fr.main.model.units.Unit;
import fr.main.view.render.terrains.land.*;
import fr.main.view.render.terrains.naval.*;
import fr.main.view.render.units.LanderRenderer;
import fr.main.view.MainFrame;

/**
 * Classe qui lance le projet
 */
public class App {

  public static void main (String[] args) {

    Random rand = new Random();
    Terrain[][] map = new Terrain[30][30];
//    for (int i = 0; i < 30; i++)
//      for (int j = 0; j < 30; j++)
//        switch (rand.nextInt(3)) {
//          case 0: map[i][j] = LowlandRenderer.get();break;
//          case 1: map[i][j] = SeaRenderer.get();break;
//          case 2: map[i][j] = ReefRenderer.get();break;
//        }
//
    int [][] intMap = Universe.randMap(30, 30);

    for (int i = 0; i < 30; i++)
      for (int j = 0; j < 30; j++)
        switch (intMap[i][j]) {
          case -1: map[i][j] = BeachRenderer.get(); break;
          case 0: map[i][j] = LowlandRenderer.get();break;
          case 1: map[i][j] = SeaRenderer.get();break;
          case 2: map[i][j] = ReefRenderer.get();break;
        }


    Player[] players = new Player[]{
      new Player("P1"), new Player("P2")
    };

    Unit[][] units = new Unit[30][30];
    units[0][0] = new LanderRenderer(new Point(0,0));
    units[1][1] = new LanderRenderer(new Point(1,1));
    units[3][6] = new LanderRenderer(new Point(6,3));

    players[0].add(units[0][0]);
    players[0].add(units[3][6]);
    players[1].add(units[1][1]);

    Universe.save("maps/maptest.map", units, map, players);

    //Universe word = new Universe("maps/maptest.map");
    //System.out.println(word);


    
    EventQueue.invokeLater(MainFrame::new);
  }
}
