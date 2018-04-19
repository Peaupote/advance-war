package fr.main.view.controllers;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.MouseEvent;
import java.awt.Point;

import fr.main.model.TerrainEnum;
import fr.main.model.generator.MapGenerator;
import fr.main.model.Universe;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.units.AbstractUnit;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.view.MainFrame;
import fr.main.view.views.EditorView;
import fr.main.view.render.MapRenderer;
import fr.main.view.Position;

public class EditorController extends Controller {

  public MapRenderer world;
  public Position.Camera camera;

  public EditorController () {
    generate(50, 50, 2);
  }

  public class Adaptater implements ChangeListener {

    final JSlider width, height, seed;

    public Adaptater (JSlider width, JSlider height, JSlider seed) {
      this.width  = width;
      this.height = height;
      this.seed   = seed;

      width.addChangeListener(this);
      height.addChangeListener(this);
      seed.addChangeListener(this);
    }

    public void stateChanged (ChangeEvent e) {
      generate(width.getValue(), height.getValue(), seed.getValue());
    }
  }

  public void generate (int width, int height, int seed) {
    AbstractTerrain[][] map = new AbstractTerrain[width][height];
    TerrainEnum[][] eMap = new MapGenerator(seed).randMap(width, height);

    for (int i = 0; i < eMap.length; i++)
      for (int j = 0; j < eMap[0].length; j++)
		  map[i][j] = eMap[i][j].terrain;

    AbstractUnit[][] units = new AbstractUnit[width][height];
    AbstractBuilding[][] buildings = new AbstractBuilding[width][height];

    world  = new MapRenderer(new Universe.Board(units, null, map, buildings, null, null, 0));
    camera = new Position.Camera(world.getDimension());
  }

  private Point ref;

  public void mouseDragged (MouseEvent e) {
    Point vec = e.getPoint();
    vec.x = (vec.x - ref.x);
    vec.y = (vec.y - ref.y);

    camera.setLocation(camera.getRealX() +  + vec.x,
                       camera.getRealY() + vec.y);
  }

  public void mousePressed (MouseEvent e) {
    ref = e.getPoint();
  }

  public void mouseReleased (MouseEvent e) {
    ref = null;
  }

  public EditorView makeView () {
    return new EditorView(this);
  }

}
