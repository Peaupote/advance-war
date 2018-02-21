package fr.main.model.units;

import java.util.LinkedList;
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
  public boolean add (Direction d) {
    // TODO: do some check or change on the path here
    return super.add(d);
  }

  public void apply () {
    unit.move(this);
  }

}

