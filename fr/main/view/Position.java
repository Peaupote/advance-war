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

    public void draw (Graphics g) {
      g.setColor(Color.black);
      g.drawRect((x - camera.getX()) * MainFrame.UNIT,
                 (y - camera.getY()) * MainFrame.UNIT, MainFrame.UNIT, MainFrame.UNIT);
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

  }

  protected int x, y;

  public Position (int x, int y) {
    this.x = x;
    this.y = y;
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

  public final void move (Direction d) {
    if (canMove (d)) {
      if      (d == Direction.TOP)    y--;
      else if (d == Direction.LEFT)   x--;
      else if (d == Direction.RIGHT)  x++;
      else if (d == Direction.BOTTOM) y++;
    }
    System.out.println(this.getClass() + ": " + x + "," + y);
  }

  protected abstract boolean canMove (Direction d);
}

