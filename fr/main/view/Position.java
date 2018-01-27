package fr.main.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

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
      return (d == Direction.LEFT   && x > 0) ||
             (d == Direction.RIGHT  && x < size.width - 1) ||
             (d == Direction.TOP    && y > 0) ||
             (d == Direction.BOTTOM && y < size.height - 1);
    }

    @Override
    protected boolean hasReachLocation () {
      return (realX == targetX * MainFrame.UNIT) &&
             (realY == targetY * MainFrame.UNIT);
    }

    public void draw (Graphics g) {
      g.setColor(Color.black);
      g.drawRect(realX - camera.realX , realY - camera.realY, MainFrame.UNIT, MainFrame.UNIT);
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
      return (d == Direction.LEFT   && x > 0) ||
             (d == Direction.RIGHT  && x + width < size.width) ||
             (d == Direction.TOP    && y > 0) ||
             (d == Direction.BOTTOM && y + height < size.height);
    }

    @Override
    protected boolean hasReachLocation () {
      return (realX == targetX * MainFrame.UNIT) &&
             (realY == targetY * MainFrame.UNIT);
    }

  }

  protected int x, y, realX, realY, targetX, targetY;
  protected Direction direction;

  public Position (int x, int y) {
    this.x = x;
    this.y = y;
    realX = x * MainFrame.UNIT;
    realY = y * MainFrame.UNIT;
  }

  public Position () {
    this(0,0);
  }

  public final int getX () {
    return x;
  }

  public final int getY () {
    return y;
  }

  public final int getOffsetX () {
    return realX - x * MainFrame.UNIT;
  }

  public final int getOffsetY() {
    return realY - y * MainFrame.UNIT;
  }

  public final boolean move () {
    if      (direction == Direction.TOP)    realY--;
    else if (direction == Direction.LEFT)   realX--;
    else if (direction == Direction.RIGHT)  realX++;
    else if (direction == Direction.BOTTOM) realY++;

    if (hasReachLocation()) {
      if      (direction == Direction.TOP)    y--;
      else if (direction == Direction.LEFT)   x--;
      else if (direction == Direction.RIGHT)  x++;
      else if (direction == Direction.BOTTOM) y++;
      direction = Direction.NONE;
    }

    return direction != Direction.NONE;
  }

  public final void setDirection (Direction d) {
    if (canMove(d)) {
      direction = d;

      if      (direction == Direction.TOP)    targetY--;
      else if (direction == Direction.LEFT)   targetX--;
      else if (direction == Direction.RIGHT)  targetX++;
      else if (direction == Direction.BOTTOM) targetY++;
    }
  }

  protected abstract boolean canMove (Direction d);
  protected boolean hasReachLocation () { return true; }
}

