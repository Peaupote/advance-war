package fr.main.model;

import java.awt.Point;

/**
 * Class listing movements
 */
public enum Direction {
    LEFT(-1,0),
    RIGHT(1,0),
    TOP(0,-1),
    BOTTOM(0,1),
    NONE(0,0);

    /**
     * value associated to the movement
     */
    public final int x,y;

    private Direction (int valueX, int valuxY) {
        x = valueX;
        y = valuxY;
    }

    /**
     * Translate the given point by the given direction
     * @param Point point to move
     */
    public void move (Point pt, int incr) {
        pt.translate (x * incr, y * incr);
    }

    public void move (Point pt) {
        move (pt, 1);
    }

    public Direction opposed(){
        return Direction.opposed(this);
    }

    public static Direction opposed(Direction d){
        if      (d == Direction.TOP)    return Direction.BOTTOM;
        else if (d == Direction.LEFT)   return Direction.RIGHT;
        else if (d == Direction.RIGHT)  return Direction.LEFT;
        else if (d == Direction.BOTTOM) return Direction.TOP;
        else                            return Direction.NONE;
    }

    public static Direction[] cardinalDirections(){
        return new Direction[]{
            LEFT,TOP,RIGHT,BOTTOM
        };
    }

}
