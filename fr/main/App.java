package fr.main;

import java.awt.EventQueue;

import fr.main.model.Universe;
import fr.main.model.terrains.Terrain;
import fr.main.model.terrains.land.*;
import fr.main.model.terrains.naval.*;
import fr.main.view.MainFrame;

/**
 * Classe qui lance le projet
 */
public class App {

  public static void main (String[] args) {
    Terrain[][] map = new Terrain[][]{
      {new Sea(), new Sea(), new Sea(), new Sea(), new Sea(), new Reef()},
      {new Sea(), new Lowland(), new Reef(), new Sea(), new Lowland(), new Sea()},
      {new Sea(), new Lowland(), new Reef(), new Sea(), new Lowland(), new Sea()},
      {new Sea(), new Lowland(), new Reef(), new Sea(), new Lowland(), new Sea()},
      {new Sea(), new Lowland(), new Reef(), new Sea(), new Lowland(), new Sea()},
      {new Sea(), new Lowland(), new Reef(), new Reef(), new Lowland(), new Sea()},
      {new Lowland(), new Lowland(), new Lowland(), new Lowland(), new Lowland(), new Sea()},
      {new Sea(), new Lowland(), new Lowland(), new Sea(), new Lowland(), new Sea()},
      {new Reef(), new Lowland(), new Lowland(), new Lowland(), new Lowland(), new Lowland()}};
    Universe.save("maps/maptest.map", map);

    Universe world = new Universe("maps/maptest.map");
    System.out.println(world);
    
    EventQueue.invokeLater(MainFrame::new);
  }
}
