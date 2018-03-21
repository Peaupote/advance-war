package fr.main.model.units;

import java.util.LinkedList;
import java.util.HashSet;
import java.awt.Point;

import fr.main.model.MoveZone;
import fr.main.model.Node;
import fr.main.model.Direction;
import fr.main.model.Universe;
import fr.main.model.units.naval.NavalUnit;
import fr.main.model.buildings.Dock;

public class Path extends LinkedList<Direction> {

    protected AbstractUnit unit;

    private Node[][] map;
    private Point offset;

    private int pathMoveCost;
    private LinkedList<Point> points;

    public Path () {
        super();
        points = new LinkedList<Point>();
    }

    public void rebase (AbstractUnit unit) {
        removeAll(this);
        points.removeAll(points);
        pathMoveCost = 0;

        this.unit = unit;

        MoveZone o = unit.getMoveMap();
        map    = o.map;
        offset = o.offset;
        points.add(new Point(unit.getX() - offset.x, unit.getY() - offset.y));
    }

    public void shorten(){
        Point p = points.getLast().getLocation();
        p.translate(offset.x,offset.y);
        shorten(p);
    }

    /*
    * @param p is the objective in the absolute map
    */
    protected void shorten(Point p){
        p.translate(-offset.x,-offset.y);
        if (p.x < 0 || p.y < 0 || p.x >= map[0].length || p.y >= map.length)
            return;
        removeAll(this);
        points.removeAll(points);
        points.add(p.getLocation());
        pathMoveCost = map[p.y][p.x].lowestCost;
        Direction d;
        while ((d = map[p.y][p.x].previous) != Direction.NONE){
            addFirst(d);
            d.opposed().move(p);
            points.addFirst(p.getLocation());
        }
    }

    public boolean add (Point p){
        p.translate(- offset.x, - offset.y);
        if (p.x >= 0 && p.y >= 0 &&
                p.x < map[0].length && p.y < map.length){
            if (points.getLast().equals(p)) return false; // rien n'a changé, le point est déjà le dernier du chemin
            Direction d = Direction.getDir(points.getLast(), p);
            if (map[p.y][p.x].canStop && map[p.y][p.x].lowestCost <= unit.getMoveQuantity())
                if (d == Direction.NONE){
                    p.translate(offset.x, offset.y);
                    shorten(p);
                    return true;
                }else return add(d);
        }
        return false;
    }

    @Override
    public /*synchronized*/ boolean add (Direction dir) {
        Point t = points.getLast().getLocation();
        dir.move(t);

        if (!map[t.y][t.x].canStop) return false;

        int i = points.lastIndexOf(t);
        if (i != -1) {
            while (points.size() != i+1){
                removeLast();
                pathMoveCost -= map[points.getLast().y][points.getLast().x].moveCost;
                points.removeLast();
            }
            return true;
        }

        if (pathMoveCost + map[t.y][t.x].moveCost > unit.getMoveQuantity()){
            t.translate(offset.x,offset.y);
            shorten(t);
            return true;
        }

        points.add(t);
        pathMoveCost += map[t.y][t.x].moveCost;
        return super.add(dir);
    }

    public void apply () {
        unit.move(this);
    }
}