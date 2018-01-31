package fr.main.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.view.MainFrame;
import fr.main.view.render.UniverseRenderer;

public class Controller extends KeyAdapter implements MouseMotionListener {

  public final Position.Cursor cursor;
  public final Position.Camera camera;
  public final UniverseRenderer world;

  private boolean isListening = false;

  public Controller (Player ps[]) {
    world   = new UniverseRenderer("maps/maptest.map", ps);
    camera = new Position.Camera(world.getDimension());
    cursor = new Position.Cursor(camera, world.getDimension());
  }

  @Override
  public void keyPressed (KeyEvent e) {
    int key = e.getKeyCode();
    if (!isListening) {
      if      (key == KeyEvent.VK_UP)    move(Direction.TOP);
      else if (key == KeyEvent.VK_LEFT)  move(Direction.LEFT);
      else if (key == KeyEvent.VK_RIGHT) move(Direction.RIGHT);
      else if (key == KeyEvent.VK_DOWN)  move(Direction.BOTTOM);
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
    if (!isListening) {
      int x = e.getX() / MainFrame.UNIT,
          y = e.getY() / MainFrame.UNIT;
      if (x == camera.getX()) camera.setDirection(Direction.LEFT);
      else if (y == camera.getY()) camera.setDirection(Direction.TOP);
      else if (x == camera.getX() + camera.width - 1) camera.setDirection(Direction.RIGHT);
      else if (y == camera.getY() + camera.height - 1) camera.setDirection(Direction.BOTTOM);
      cursor.setPosition (x, y);
    }
  }

  /**
   * Function called each loop turn
   */
  public void update () {
    isListening = cursor.move() | camera.move();
  }

  private void move (Direction d) {
    if ((d == Direction.LEFT && cursor.getX() - camera.getX() == 1) ||
        (d == Direction.RIGHT && camera.getX() + camera.width - cursor.getX() == 2) ||
        (d == Direction.TOP && cursor.getY() - camera.getY() == 1) ||
        (d == Direction.BOTTOM && camera.getY() + camera.height - cursor.getY() == 2))
      camera.setDirection (d);
    cursor.setDirection (d);
  }

}
