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
import java.util.LinkedList;

public class Controller extends KeyAdapter implements MouseMotionListener {

  public final Position.Cursor cursor;
  public final Position.Camera camera;
  public final UniverseRenderer world;
  public final int mouveRange = 2;

  private boolean isListening = false, listenMouse = false;
  private Point mouse;
  private Mode mode;
  private ActionPanel actionPanel;
  private DayPanel dayPanel;

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
    world  = new UniverseRenderer("maps/maptest.map", this);
    camera = new Position.Camera(world.getDimension());
    cursor = new Position.Cursor(camera, world.getDimension());
    mouse  = new Point(1,1);

    actionPanel = new MainActionPanel();
    dayPanel = new DayPanel();
    mode = Mode.MOVE;

    new TerrainPanel (cursor, camera);
    new PlayerPanel (cursor, camera);
  }

  @Override
  public void keyPressed (KeyEvent e) {
    int key = e.getKeyCode();
    if (!isListening) {
      if (mode == Mode.MOVE) {
        if      (key == KeyEvent.VK_UP)    move(Direction.TOP);
        else if (key == KeyEvent.VK_LEFT)  move(Direction.LEFT);
        else if (key == KeyEvent.VK_RIGHT) move(Direction.RIGHT);
        else if (key == KeyEvent.VK_DOWN)  move(Direction.BOTTOM);
        else if (key == KeyEvent.VK_ENTER)
          if (world.getUnit(cursor.getX(), cursor.getY()) == null) actionPanel.setVisible (true);
          else mode = Mode.UNIT;
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
    isListening = cursor.move() | camera.move();

    if (!isListening && mode == Mode.MOVE && listenMouse) {
        if (mouse.x <= mouveRange) camera.setDirection(Direction.LEFT);
        else if (camera.width - mouse.x <= mouveRange) camera.setDirection(Direction.RIGHT);
        else if (mouse.y <= mouveRange) camera.setDirection(Direction.TOP);
        else if (camera.height - mouse.y <= mouveRange) camera.setDirection(Direction.BOTTOM);

        cursor.setPosition(mouse.x, mouse.y);
    }
  }

  private void move (Direction d) {
    listenMouse = false;

    if ((d == Direction.LEFT && cursor.getX() - camera.getX() == mouveRange) ||
        (d == Direction.RIGHT && camera.getX() + camera.width - cursor.getX() == mouveRange + 1) ||
        (d == Direction.TOP && cursor.getY() - camera.getY() == mouveRange) ||
        (d == Direction.BOTTOM && camera.getY() + camera.height - cursor.getY() == mouveRange + 1))
      camera.setDirection (d);
    cursor.setDirection (d);
  }

  public Mode getMode () {
    return mode;
  }

}
