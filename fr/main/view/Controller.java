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
    isListening = true;
  }

  @Override
  public void keyReleased (KeyEvent e) {
    isListening = false;
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  public void update () {
    if (isListening) {
      if (key == KeyEvent.VK_UP)         moveScreen(Direction.TOP);
      else if (key == KeyEvent.VK_LEFT)  moveScreen(Direction.LEFT);
      else if (key == KeyEvent.VK_RIGHT) moveScreen(Direction.RIGHT);
      else if (key == KeyEvent.VK_DOWN)  moveScreen(Direction.BOTTOM);
      isListening = false;
    }
  }

  private void moveScreen (Direction d) {
    cursor.move(d);
    camera.move(d);
  }

}
