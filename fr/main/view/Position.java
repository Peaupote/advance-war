package fr.main.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import fr.main.model.Direction;
import fr.main.view.render.sprites.Sprite;

/**
 * Represents a moving point on the map.
 */
public abstract class Position {

  /**
   * Represents the user cursor.
   */
  public static class Cursor extends Position {

    /**
     * Camera showing the game.
     */
    private final Camera camera;

    public static final Image cursorBasic;
    public static final Image cursorAttack;

    static{
      Sprite attack = Sprite.get("./assets/ingame/attack.png");
      cursorBasic = attack.getImage(39, 2, 28, 31);
      cursorAttack = attack.getImage(70, 1, 30, 29);
    }

    private Image cursor;

    public Cursor (Camera camera, Dimension size) {
      super(size);

      this.camera = camera;

      cursor = cursorBasic;
    }

    @Override
    public boolean canMove (Direction d) {
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
      int offset = (int)(5 * Math.cos(MainFrame.getTimer() / 5)),
               s = MainFrame.UNIT + offset;
      g.drawImage (cursor, 2 + real.x - camera.real.x - offset / 2 + 1,
                           2 + real.y - camera.real.y - offset / 2 + 1, s, s, null);
    }

    public void setLocation (int x, int y) {
      if (x >= 0 && y >= 0 &&
          x < size.width && y < size.width) {
        position.x = x;
        position.y = y;
        target.x = position.x;
        target.y = position.y;

        real.x = x * MainFrame.UNIT;
        real.y = y * MainFrame.UNIT;
      }
    }

    public void setLocation (Point pt) {
      setLocation (pt.x, pt.y);
    }

    public void draw (Graphics g) {
      draw (g, Color.black);
    }


    public void setCursor(boolean normal){
      cursor = normal ? cursorBasic : cursorAttack;
    }

  }

  /**
   * Class representing the camera, ie what the user can see.
   */
  public static class Camera extends Position {

    /**
     * Dimension of the camera.
     */
    public int width, height;

    public Camera (Dimension size) {
      super(size);
    }

    @Override
    public boolean canMove (Direction d) {
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
   * Point representing respectivly position on the map, 
   * the position on the screen and the target position
   * on the map while moving.
   */
  protected Point position, real, target;

  /**
   * Dimension of the universe.
   */
  protected final Dimension size;

  /**
   * Moving direction.
   */
  protected Direction direction;

  public Position (int x, int y, Dimension size) {
    this.direction = Direction.NONE;
    this.position = new Point(x, y);
    this.target = new Point(x,y);
    this.real = new Point(x * MainFrame.UNIT, y * MainFrame.UNIT);
    this.size = size;
  }

  public Position (Dimension size) {
    this(0, 0, size);
  }

  public final int getX () {
    return position.x;
  }

  public final int getY () {
    return position.y;
  }

  public final int getRealX () {
    return real.x;
  }

  public final int getRealY () {
    return real.y;
  }

  public final Point position () {
    return position.getLocation();
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
   * Set the movement of the position if he can move by the given way.
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
  public abstract boolean canMove (Direction d);

  /**
   * @return true if real location has reach target location
   */
  protected boolean hasReachLocation () { return true; }

}

