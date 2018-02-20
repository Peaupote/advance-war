package fr.main.view;

import java.util.HashMap;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.Point;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.view.MainFrame;
import fr.main.view.render.UniverseRenderer;
import fr.main.view.interfaces.*;
import fr.main.view.render.PathRenderer;
import fr.main.model.units.Unit;
import java.util.LinkedList;
import fr.main.model.units.Unit;

public class Controller extends KeyAdapter implements MouseMotionListener {

  public final Position.Cursor cursor, unitCursor;
  public final Position.Camera camera;
  public final UniverseRenderer world;
  public final int moveRange = 2;

  private boolean isListening = false, listenMouse = false;
  private Point mouse;
  private Mode mode;
  private ActionPanel actionPanel;
  private DayPanel dayPanel;
  public PathRenderer path;

  public static enum Mode {
    MOVE,
    MENU,
    UNIT
  }

  private class MainActionPanel extends ActionPanel {

    public MainActionPanel () {
      super(null);
      x = MainFrame.WIDTH - 200;
      y = 10;

      actions = new HashMap<>();
      actions.put(new Index("Finish turn"), e -> {
        world.next();
        dayPanel.setVisible(true);
      });

      actions.put(new Index("Wait"), e -> {});
    }

    public void onOpen () {
      super.onOpen();
      for (InterfaceUI com: InterfaceUI.components())
        if (com != this) com.setVisible(false);
      mode = Mode.MENU;
    }

    public void onClose () {
      super.onClose();
      for (InterfaceUI com: InterfaceUI.components())
        // TODO: change condition by splitting components in 2 sets
        if (com != this && com != dayPanel) com.setVisible(true);
      mode = Mode.MOVE;
    }

  }

  public Controller (Player ps[]) {
    world      = new UniverseRenderer("maptest.map", this);
    camera     = new Position.Camera(world.getDimension());
    cursor     = new Position.Cursor(camera, world.getDimension());
    unitCursor = new Position.Cursor(camera, world.getDimension()) {

      @Override
      public boolean canMove(Direction d) {
        if (!super.canMove(d)) return false;
        boolean[][] map = new boolean[size.height][size.width];
        world.getUnit(cursor.getX(), cursor.getY()).reachableLocation(map);

        Point target = new Point(position.x, position.y);
        d.move(target);
        return map[target.y][target.x];
      }

    };

    mouse       = new Point(1,1);
    actionPanel = new MainActionPanel();
    dayPanel    = new DayPanel();
    mode        = Mode.MOVE;
    path        = new PathRenderer(camera);

    new PlayerPanel (cursor, camera);
    new Minimap (camera, new TerrainPanel (cursor, camera));
    dayPanel.setVisible(true);
  }

  @Override
  public void keyPressed (KeyEvent e) {
    int key = e.getKeyCode();
    if (!isListening) {
      isListening = true;
      if (mode == Mode.MOVE ||
          (mode == Mode.UNIT &&
           world.getCurrentPlayer() == world.getUnit(cursor.getX(), cursor.getY()).getPlayer())) {
        if      (key == KeyEvent.VK_UP)    move(Direction.TOP);
        else if (key == KeyEvent.VK_LEFT)  move(Direction.LEFT);
        else if (key == KeyEvent.VK_RIGHT) move(Direction.RIGHT);
        else if (key == KeyEvent.VK_DOWN)  move(Direction.BOTTOM);
        else if (key == KeyEvent.VK_ENTER) {
          if (mode == Mode.UNIT) {
            path.apply();
            mode = Mode.MOVE;
            cursor.setPosition(unitCursor.getX() - camera.getX(), unitCursor.getY() - camera.getY());
            path.visible = false;
          } else {
            Unit unit = world.getUnit(cursor.getX(), cursor.getY()); 
            if (unit == null) actionPanel.setVisible (true);
            else if (unit.enable) {
              mode = Mode.UNIT;
              path.rebase(unit);
              path.visible = true;
              unitCursor.setPosition(cursor.getX() - camera.getX(), cursor.getY() - camera.getY());
            }
          }
        }
        else if (key == KeyEvent.VK_ESCAPE) mode = Mode.MOVE;
      } else if (mode == Mode.MENU) {
        if      (key == KeyEvent.VK_UP)     actionPanel.goUp();
        else if (key == KeyEvent.VK_DOWN)   actionPanel.goDown();
        else if (key == KeyEvent.VK_ENTER)  actionPanel.perform();
        else if (key == KeyEvent.VK_ESCAPE) actionPanel.setVisible (false);
      } else if (key == KeyEvent.VK_ESCAPE) mode = Mode.MOVE;
    }
  }

  @Override
  public void keyReleased (KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void mouseDragged (MouseEvent e) {}

  @Override
  public void mouseMoved (MouseEvent e) {
      mouse.x = e.getX()  / MainFrame.UNIT;
      mouse.y = e.getY() / MainFrame.UNIT;
      listenMouse = true;
  }

  /**
   * Function called each loop turn
   */
  public void update () {
    isListening = cursor.move() | camera.move() | (mode == Mode.UNIT && unitCursor.move());

    if (!isListening && mode == Mode.MOVE && listenMouse) {
        if (mouse.x <= moveRange) camera.setDirection(Direction.LEFT);
        else if (camera.width - mouse.x <= moveRange) camera.setDirection(Direction.RIGHT);
        else if (mouse.y <= moveRange) camera.setDirection(Direction.TOP);
        else if (camera.height - mouse.y <= moveRange) camera.setDirection(Direction.BOTTOM);

        cursor.setPosition(mouse.x, mouse.y);
    }
  }

  private void move (Direction d) {
    listenMouse = false;
    Position.Cursor c = mode == Mode.MOVE ? cursor : unitCursor;

    if ((d == Direction.LEFT && c.getX() - camera.getX() <= moveRange) ||
        (d == Direction.RIGHT && camera.getX() + camera.width - c.getX() <= moveRange + 1) ||
        (d == Direction.TOP && c.getY() - camera.getY() <= moveRange) ||
        (d == Direction.BOTTOM && camera.getY() + camera.height - c.getY() <= moveRange + 1))
      camera.setDirection (d);

    c.setDirection (d);
    
    if (mode == Mode.UNIT) {
      Point p = new Point(unitCursor.getX(), unitCursor.getY());
      d.move(p);
      if (unitCursor.canMove(d)) path.add(p);
    }
  }

  public Mode getMode () {
    return mode;
  }

}
