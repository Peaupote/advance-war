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
     * @param int the increment
     */
    public void move (Point pt, int incr) {
        pt.translate (x * incr, y * incr);
    }

    /**
     * @param Point the point we want to move
     * Move to point accordingly to the direction
     */
    public void move (Point pt) {
        move (pt, 1);
    }

    /**
     * @return the direction opposed to this
     */
    public Direction opposed(){
        return Direction.opposed(this);
    }

    /**
     * @param Direction the direction we consider
     * @return the direction opposed to the considered direction
     */
    public static Direction opposed(Direction d){
        if      (d == Direction.TOP)    return Direction.BOTTOM;
        else if (d == Direction.LEFT)   return Direction.RIGHT;
        else if (d == Direction.RIGHT)  return Direction.LEFT;
        else if (d == Direction.BOTTOM) return Direction.TOP;
        else                            return Direction.NONE;
    }

    /**
     * @return an array of the 4 cardinal directions
     */
    public static Direction[] cardinalDirections(){
        return new Direction[]{
            LEFT,TOP,RIGHT,BOTTOM
        };
    }

    /**
     * @param Point the departure point
     * @param Point the arrival point
     * @return the direction to go from the departure to the arrival, return NONE if the points aren't adjacents
     */
    public static Direction getDir(Point dep, Point arr){
        return getDir(dep.x, dep.y, arr.x, arr.y);
    }

    /**
     * @return the direction to go from the departure to the arrival
     */
    public static Direction getDir(int a, int b, int c, int d){
        if (d == b){
            if (c - a == 1)       return RIGHT;
            else if (c - a == -1) return LEFT;
        }
        else if (c == a){
            if (d - b == 1)       return BOTTOM;
            else if (d - b == -1) return TOP;
        }
        return NONE;
    }
}
