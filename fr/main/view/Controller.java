package fr.main.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import fr.main.model.Universe;

public class Controller extends KeyAdapter {

  public final Position.Cursor cursor;
  public final Position.Camera camera;
  public final Universe word;

  private int key = -1; 
  private boolean isListening = false;

  public Controller () {
    word   = new Universe("maps/maptest.map");
    camera = new Position.Camera(word.getDimension());
    cursor = new Position.Cursor(camera, word.getDimension());
  }

  @Override
  public void keyPressed (KeyEvent e) {
    key = e.getKeyCode();
  }

  @Override
  public void keyReleased (KeyEvent e) {
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  public void update () {
    if (!isListening) {
      if      (key == KeyEvent.VK_UP)    move(Direction.TOP);
      else if (key == KeyEvent.VK_LEFT)  move(Direction.LEFT);
      else if (key == KeyEvent.VK_RIGHT) move(Direction.RIGHT);
      else if (key == KeyEvent.VK_DOWN)  move(Direction.BOTTOM);
      key = -1;
    }

    isListening = cursor.move() || camera.move();
  }

  private void move (Direction d) {
    camera.setDirection (d);
    cursor.setDirection (d);
  }

}
