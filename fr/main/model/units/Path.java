package fr.main.model.units;

import java.awt.Point;
import java.util.LinkedList;

import fr.main.model.Direction;
import fr.main.model.MoveZone;
import fr.main.model.Node;

/**
 * A class representing a path for the move of an unit
 */
public class Path extends LinkedList<Direction> {

    /**
	 * Add Path UID
	 */
	private static final long serialVersionUID = -3190430267430534047L;

    protected static Path instance = new Path();

    public static Path getPath(){
        return instance;
    }

	/**
     * The unit that is moving
     */
    protected AbstractUnit unit;

    /**
     * The part of the map on which the unit can move
     */
    private Node[][] map;
    /**
     * The offset of Node[][] map (see Node)
     */
    private Point offset;

    /**
     * The amount of move point needed to do the actual path described
     */
    private int pathMoveCost;
    /**
     * The list of points described by the path (only used internally)
     */
    private LinkedList<Point> points;

    public Path () {
        super();
        points = new LinkedList<Point>();
    }

    /**
     * @param unit is the new unit considered in this path
     * change the unit considered
     */
    public void rebase (AbstractUnit unit) {
        clear();
        points.clear();
        pathMoveCost = 0;

        this.unit = unit;

        // see function getMoveMap in unit, which returns the part of the map
        // on which the unit can move (with informations about each tiles)
        MoveZone o = unit.getMoveMap();
        map    = o.map;
        offset = o.offset;
        points.add(new Point(unit.getX() - offset.x, unit.getY() - offset.y));
    }

    /**
     * Delete the whole path and replace it by the shortest way to go to the last point
     */
    public void shorten(){
        Point p = points.getLast().getLocation();
        p.translate(offset.x,offset.y);
        shorten(p);
    }

    /**
     * @param p is the objective in the absolute map
     * Delete the whole path and replace it by the shortest way to go to the point
     */
    protected void shorten(Point p){
        // the point in the relative map
        p.translate(-offset.x,-offset.y);
        if (p.x < 0 || p.y < 0 || p.x >= map[0].length || p.y >= map.length)
            // if it is not a point of the relative map, there is no way to go to it
            return;
        clear();
        points.clear();
        points.add(p.getLocation()); // add the point we want to go to
        pathMoveCost = map[p.y][p.x].lowestCost;

        // the strategy here is to begin at the target and to go back tile by tile until we reached the unit's position
        Direction d;
        while ((d = map[p.y][p.x].previous) != Direction.NONE){
            // the position of the unit is the only accessible tile whose 'previous' direction is NONE
            // so if the tile is accessible, and its previous direction is NONE, it means we arrived onto the unit original position
            addFirst(d);
            d.opposed().move(p);
            points.addFirst(p.getLocation());
        }
    }

    /**
     * @param p is the point we want to add to this path
     * @return true if and only if the unit can stop on the point and the point is not the last one
     * Add the point to the path
     */
    public boolean add (Point p){
        // the point in the relative map
        p.translate(- offset.x, - offset.y);
        if (p.x >= 0 && p.y >= 0 && p.x < map[0].length && p.y < map.length){
            // if it is a point of the relative map, we try to add it to the path
            if (points.getLast().equals(p)) return false; // the point is the last one, so the list isn't modified
            Direction d = Direction.getDir(points.getLast(), p); // the direction to go from the last point to the argument point
            // it is NONE if these point aren't adjacents
            if (map[p.y][p.x].canStop && map[p.y][p.x].lowestCost <= unit.getMoveQuantity())
                // if the unit can go on the tile, and the tile is adjacent, we add it
                // if the tile isn't adjacent, we use the shortest path to go to it
                if (d == Direction.NONE){
                    p.translate(offset.x, offset.y);
                    shorten(p);
                    return true;
                }else return add(d);
        }
        return false;
    }

    /**
     * @param dir is the direction we want to add to this path
     * @return true if and only if the unit can stop on the point
     * Add the direction to the path
     */
    @Override
    public boolean add (Direction dir) {
        Point t = points.getLast().getLocation();
        dir.move(t);
        // we consider the point we want to add (which is the last point move according to the parameter direction)

        if (!map[t.y][t.x].canStop) return false;

        // if the point is already in the path, we remove the last directions
        int i = points.lastIndexOf(t);
        if (i != -1) {
            while (points.size() != i+1){
                removeLast();
                pathMoveCost -= map[points.getLast().y][points.getLast().x].moveCost;
                points.removeLast();
            }
            return true;
        }

        // if the move quantity needed to go to the point going by the path is too high, we shorten the path (replace it by the shortest)
        if (pathMoveCost + map[t.y][t.x].moveCost > unit.getMoveQuantity()){
            t.translate(offset.x,offset.y);
            shorten(t);
            return true;
        }

        points.add(t);
        pathMoveCost += map[t.y][t.x].moveCost;
        return super.add(dir);
    }

    /**
     * @return true if and only if the path was applied until the end
     */
    public boolean apply () {
        return unit.move(this);
    }
}
