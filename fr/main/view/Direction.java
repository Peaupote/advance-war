package fr.main.view;

import java.awt.Point;

/**
 * Class listing movements
 */
enum Direction {
  LEFT(-1),
  RIGHT(1),
  TOP(-1),
  BOTTOM(1),
  NONE(0);

  /**
   * value associated to the movement
   */
  private final int value;

  private Direction (final int value) {
    this.value = value;
  }

  /**
   * Translate the given point by the given direction
   * @param Point point to move
   */
  public void move (Point pt, int incr) {
    if      (this == Direction.TOP)    pt.translate (0, -incr);
    else if (this == Direction.LEFT)   pt.translate (-incr, 0);
    else if (this == Direction.RIGHT)  pt.translate (incr, 0);
    else if (this == Direction.BOTTOM) pt.translate (0, incr);
  }

  public void move (Point pt) {
    move (pt, 1);
  }

};
