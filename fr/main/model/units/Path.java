package fr.main.model.units;

import java.awt.Point;
import java.util.LinkedList;

public class Path extends LinkedList<Point> {

  private Unit unit;

  public Path (Unit unit) {
    super();
    // we assume here that start has correct coordinates
    add(new Point(unit.getX(), unit.getY()));

    this.unit = unit;
  }

  public Path () {
    super();
  }

  public void rebase (Unit unit) {
    removeAll(this);
    this.unit = unit;
    super.add(new Point(unit.getX(), unit.getY()));
  }

  @Override
  public boolean add (Point point) {
    Point last = getLast();
    if (last.equals(point) ||
        Math.abs(point.x - last.x) > 1 || Math.abs(point.y - last.y) > 1)
      return false;
    super.add(point);
    return true;
  }

  public void apply () {
    unit.move(this);
  }

}

