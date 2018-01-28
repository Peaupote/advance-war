package fr.main.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import fr.main.model.Universe;
import fr.main.view.MainFrame;

public class Controller extends KeyAdapter implements MouseMotionListener {

  public final Position.Cursor cursor;
  public final Position.Camera camera;
  public final Universe word;

  private boolean isListening = false;

  public Controller () {
    word   = new Universe("maps/maptest.map");
    camera = new Position.Camera(word.getDimension());
    cursor = new Position.Cursor(camera, word.getDimension());
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
      cursor.setPosition (x, y);
    }
  }

  /**
   * Function called each loop turn
   */
  public void update () {
    isListening = cursor.move() || camera.move();
  }

  private void move (Direction d) {
    camera.setDirection (d);
    cursor.setDirection (d);
  }

}
