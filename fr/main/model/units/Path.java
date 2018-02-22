package fr.main.model.units;

import java.util.LinkedList;
import java.awt.Point;
import fr.main.model.Direction;

public class Path extends LinkedList<Direction> {

  protected Unit unit;

  public Path () {
    super();
  }

  public void rebase (Unit unit) {
    removeAll(this);
    this.unit = unit;
  }

  @Override
  public boolean add (Direction dir) {
    // TODO: test if over path length and eval a new path
    Point t = new Point (0, 0);
    LinkedList<Point> pts = new LinkedList<>();
    pts.add(t);

    for (Direction d: this) {
      t = (Point)t.clone();
      d.move(t);
      pts.add(t);
    }

    t = (Point)t.clone();
    dir.move(t);
    int i = pts.indexOf(t);
    if (i != -1) {
      for (int j = i; j < pts.size() - 1; j++) removeLast();
      return true;
    }

    return super.add(dir);
  }

  public void apply () {
    unit.move(this);
  }

}

