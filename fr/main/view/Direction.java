package fr.main.view;

import java.awt.Point;

enum Direction {
  LEFT(-1),
  RIGHT(1),
  TOP(-1),
  BOTTOM(1),
  NONE(0);

  private final int value;

  private Direction (final int value) {
    this.value = value;
  }

  public void move (Point pt) {
    if      (this == Direction.TOP)    pt.translate (0, -1);
    else if (this == Direction.LEFT)   pt.translate (-1, 0);
    else if (this == Direction.RIGHT)  pt.translate (1, 0);
    else if (this == Direction.BOTTOM) pt.translate (0, 1);
  }

};
