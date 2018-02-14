package fr.main.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.*;
import java.lang.Thread;
import java.lang.InterruptedException;

import fr.main.view.MainFrame;

/**
 * Represents a moving point on the map
 */
public abstract class Position {

  /**
   * Represents the user cursor
   */
  public static class Cursor extends Position {

    /**
     * Dimension of the universe
     */
    protected final Dimension size;

    /**
     * Camera showing the game
     */
    private final Camera camera;

    private Image image;
    private int cursorScale;

    public Cursor (Camera camera, Dimension size) {
      super(0, 0);

      this.size   = size;
      this.camera = camera;
      cursorScale = 1;

      image = null;
      try {
        image = ImageIO.read(new File("./assets/cursor.png"));

        new Thread(() -> {
          while(true) {
            cursorScale = (cursorScale + 1) % 10;

            try {
              Thread.sleep(50);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }).start();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    @Override
    protected boolean canMove (Direction d) {
      return (d == Direction.LEFT   && position.x > 0) ||
             (d == Direction.RIGHT  && position.x < size.width - 1) ||
             (d == Direction.TOP    && position.y > 0) ||
             (d == Direction.BOTTOM && position.y < size.height - 1);
    }

    @Override
    protected boolean hasReachLocation () {
      return (real.x == target.x * MainFrame.UNIT) &&
             (real.y == target.y * MainFrame.UNIT);
    }

    public void draw (Graphics g, Color color) {
      int s = MainFrame.UNIT + cursorScale;
      g.drawImage (image, 2 + real.x - camera.real.x, 2 + real.y - camera.real.y, s, s, null);
    }

    public void draw (Graphics g) {
      draw (g, Color.black);
    }

    public void setPosition (int x, int y) {
      if (x >= 0 && x < camera.width && y >= 0 && y < camera.height) {
        x += camera.position.x;
        y += camera.position.y;

        position.x = x;
        position.y = y;
        target.x = x;
        target.y = y;

        real.x = x * MainFrame.UNIT;
        real.y = y * MainFrame.UNIT;
      }
    }

  }

  /**
   * Class representing the camera, ie what the user can see
   */
  public static class Camera extends Position {

    /**
     * Dimension of the camera
     */
    public final int width, height;

    /**
     * Dimension of the universe
     */
    private final Dimension size;

    public Camera (Dimension size) {
      super();

      width  = MainFrame.WIDTH / MainFrame.UNIT;
      height = MainFrame.HEIGHT / MainFrame.UNIT;
      this.size = size;
    }

    @Override
    protected boolean canMove (Direction d) {
      return (d == Direction.LEFT   && position.x > 0) ||
             (d == Direction.RIGHT  && position.x + width < size.width) ||
             (d == Direction.TOP    && position.y > 0) ||
             (d == Direction.BOTTOM && position.y + height < size.height);
    }

    @Override
    protected boolean hasReachLocation () {
      return (real.x == target.x * MainFrame.UNIT) &&
             (real.y == target.y * MainFrame.UNIT);
    }

  }

  /**
   * Point representing respectivly position on the map, the position on the screen and the target position on the map while moving
   */
  protected Point position, real, target;

  /**
   * Moving direction
   */
  protected Direction direction;

  public Position (int x, int y) {
    this.direction = Direction.NONE;
    this.position = new Point(x, y);
    this.target = new Point(x,y);
    this.real = new Point(x * MainFrame.UNIT, y * MainFrame.UNIT);
  }

  public Position () {
    this(0,0);
  }

  public final int getX () {
    return position.x;
  }

  public final int getY () {
    return position.y;
  }

  public final int getOffsetX () {
    return real.x - position.x * MainFrame.UNIT;
  }

  public final int getOffsetY() {
    return real.y - position.y * MainFrame.UNIT;
  }

  /**
   * Make the position move
   * @return true if the movement is finished, false otherwise
   */
  public final boolean move () {
    direction.move(real, 4);

    if (hasReachLocation()) {
      direction.move(position);
      direction = Direction.NONE;
    }

    return direction != Direction.NONE;
  }

  /**
   * Set the movement of the position if he can move by the given way
   */
  public final void setDirection (Direction d) {
    if (canMove(d)) {
      direction = d;
      direction.move(target);
    }
  }

  /**
   * @return true if can move by the given direction, false othewise
   */
  protected abstract boolean canMove (Direction d);

  /**
   * @return true if real location has reach target location
   */
  protected boolean hasReachLocation () { return true; }
}

