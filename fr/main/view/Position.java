package fr.main.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Point;

import fr.main.view.MainFrame;

public abstract class Position {

  public static class Cursor extends Position {
    
    private final Dimension size;
    private final Camera camera;

    public Cursor (Camera camera, Dimension size) {
      super(0, 0);

      this.size   = size;
      this.camera = camera;
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

    public void draw (Graphics g) {
      g.setColor(Color.black);
      g.drawRect(real.x - camera.real.x, real.y - camera.real.y,
                 MainFrame.UNIT, MainFrame.UNIT);
    }

  }

  public static class Camera extends Position {

    public final int width, height;
    private final Dimension size;

    public Camera (Dimension size) {
      super();

      width  = MainFrame.WIDTH / MainFrame.UNIT;
      height = MainFrame.HEIGHT / MainFrame.UNIT;
      this.size = size;
      System.out.println(size);
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

  protected Point position, real, target;
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

  public final boolean move () {
    direction.move(real);

    if (hasReachLocation()) {
      direction.move(position);
      direction = Direction.NONE;
    }

    return direction != Direction.NONE;
  }

  public final void setDirection (Direction d) {
    if (canMove(d)) {
      direction = d;
      direction.move(target);
    }
  }

  protected abstract boolean canMove (Direction d);
  protected boolean hasReachLocation () { return true; }
}

