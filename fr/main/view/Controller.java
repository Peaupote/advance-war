package fr.main.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Point;

import fr.main.model.Universe;
import fr.main.model.Player;
import fr.main.view.MainFrame;
import fr.main.view.render.UniverseRenderer;

public class Controller extends KeyAdapter implements MouseMotionListener {

  public final Position.Cursor cursor;
  public final Position.Camera camera;
  public final UniverseRenderer world;
  public final int mouveRange = 2;

  private boolean isListening = false, listenMouse = false;
  private Point mouse;

  public Controller (Player ps[]) {
    world  = new UniverseRenderer("maps/maptest.map");
    camera = new Position.Camera(world.getDimension());
    cursor = new Position.Cursor(camera, world.getDimension());
    mouse  = new Point(1,1);
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
    if (e.getKeyCode() == KeyEvent.VK_SPACE)
      world.next();
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

    if (!isListening && listenMouse) {
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

}
