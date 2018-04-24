package fr.main.view.controllers;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

import fr.main.model.TerrainEnum;
import fr.main.model.generator.MapGenerator;
import fr.main.model.Universe;
import fr.main.model.Direction;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.units.AbstractUnit;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.view.MainFrame;
import fr.main.view.views.EditorView;
import fr.main.view.render.MapRenderer;
import fr.main.view.Position;
import fr.main.view.MainFrame;

public class EditorController extends Controller {

  public MapRenderer world;
  public Position.Camera camera;
  private boolean isListening = false;
  private EditorView view;

  public static int b = 60,
                     h = b/3;

  public static final int[][][] refs = new int[][][]{
  { // right    
    {0, 0, h},
    {0, b, b/2}
  }, { // bottom
    {0, b/2, b},
    {0, h, 0}
  }, { // top
    {0, b/2, b},
    {h, 0, h}
  }, { // left
    {0, h, h},
    {b/2, 0, b}
  }};

  public final int[][][] arrows = new int[4][2][3];
  
  public final Rectangle[] arrowButtons;

  private boolean press = false;
  private Point pt;

  public EditorController () {
    arrowButtons = new Rectangle[4];
    for (int i = 0; i < 4; i++)
      arrowButtons[i] = new Rectangle(0,0,0,0);
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

    // save previous coordinates so
    // the camera doesn't move while generating new world
    
    int x = 0, y = 0;
    if (camera != null) {
        x = camera.getX();
        y = camera.getY();
    }
    world  = new MapRenderer(new Universe.Board(units, null, map, buildings, null, null, 0));
    camera = new Position.Camera(world.getDimension());
    camera.setLocation(x, y);
  }

  public EditorView makeView () {
    view = new EditorView(this);
    return view;
  }
  
  public void mousePressed (MouseEvent e) {
    pt = new Point(e.getX(), e.getY());
    press = true;
  }

  public void mouseReleased (MouseEvent e) {
    pt = null;
    press = false;
  }

  public void update () {
    isListening = camera.move();

    if (!isListening && press) {
      if (arrowButtons[0].contains(pt))
        camera.setDirection(Direction.RIGHT);
      else if (arrowButtons[1].contains(pt) &&
               camera.canMove(Direction.BOTTOM))
        camera.setDirection(Direction.BOTTOM);
      else if (arrowButtons[2].contains(pt) &&
               camera.canMove(Direction.TOP))
        camera.setDirection(Direction.TOP);
      else if (arrowButtons[3].contains(pt) &&
               camera.canMove(Direction.LEFT))
        camera.setDirection(Direction.LEFT);
    }

    // TODO: clean
    
    if (view != null) {
      if (camera != null) {
        camera.width = (view.map.getWidth() + 1) / MainFrame.UNIT;
        camera.height = (view.map.getHeight() + 1) / MainFrame.UNIT;
      }

      Rectangle rect = view.map.getBounds(null);

      int m1 = ((int)rect.getWidth()) / 2 - b/2,
          m2 = ((int)rect.getWidth()) - h - 20,
          m3 = ((int)rect.getHeight()) / 2 - b/2,
          m4 = ((int)rect.getHeight()) - h - 20;

      int[][] basePoints = new int[][]{
        {m2, m3},
        {m1, m4},
        {m1, 20},
        {20, m3}
      };

      for (int i = 0; i < 4; i++) {
        arrowButtons[i].setBounds(basePoints[i][0], basePoints[i][1], b, b);
        for (int j = 0; j < 2; j++)
          for (int k = 0; k < 3; k++)
            arrows[i][j][k] = basePoints[i][j] + refs[i][j][k];
      }

    }
    
  }

}
