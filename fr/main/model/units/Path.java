package fr.main.model.units;

import java.util.LinkedList;
import java.util.HashSet;
import java.awt.Point;

import fr.main.model.Direction;
import fr.main.model.Universe;
import fr.main.model.units.naval.NavalUnit;
import fr.main.model.buildings.Dock;

public class Path extends LinkedList<Direction> {

    protected AbstractUnit unit;

    /*
    * assuming each terrain move cost is at least 1, we can only represent the part of the map on which the unit can move
    */
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

        Universe u = Universe.get();
        int move   = unit.getMoveQuantity(),
            posX   = unit.getX(), posY = unit.getY(),
            x      = Math.min(move, posX), y = Math.min(move, posY);
        points.add(new Point(x, y));

        offset = new Point(posX - x, posY - y);
        map    = new Node[x + 1 + Math.min(move, u.getMapWidth() - posX - 1)][y + 1 + Math.min(move, u.getMapHeight() - posY - 1)];
        shortestCalculus();
    }

    private final Node removeMin(HashSet<Node> l){
        Node node = null;
        for (Node n : l)
            if (node == null || n.compareTo(node)<0)
                node=n;
        l.remove(node);        
        return node;
    }

    public void shortestCalculus(){
        int width  = map.length,
            height = map[0].length,
            move   = unit.getMoveQuantity();

        // initialization
        for (int i = 0; i < width; i ++)
            for (int j = 0; j < height; j ++)
                map[i][j] = new Node(i, j, move + 1);

        HashSet<Node> unsettled = new HashSet<Node>();
        HashSet<Node> settled   = new HashSet<Node>();

        Node current = map[unit.getX() - offset.x][unit.getY() - offset.y];
        current.lowestCost = 0;
        unsettled.add(current);

        //  evaluation
        while (!unsettled.isEmpty()){
            Node actual=removeMin(unsettled);
            for (Direction d : Direction.cardinalDirections())
                if (actual.x+d.x<width && actual.x+d.x>=0 && actual.y+d.y<height && actual.y+d.y>=0){
                    Node target = map[actual.x+d.x][actual.y+d.y];
                    if (!settled.contains(target) && actual.lowestCost + target.moveCost < target.lowestCost){
                        target.previous = d;
                        target.lowestCost = actual.lowestCost + target.moveCost;
                        unsettled.add(target);
                    }
                }
            settled.add(actual);
        }
    }

    public void shorten(){
        Point p = (Point)points.getLast().clone();
        p.translate(offset.x,offset.y);
        shorten(p);
    }

    /*
    * @param p is the objective in the absolute map
    */
    protected void shorten(Point p){
        p.translate(-offset.x,-offset.y);
        if (p.x < 0 || p.y < 0 || p.x >= map.length || p.y >= map[0].length)
            return;
        removeAll(this);
        points.removeAll(points);
        points.add((Point)p.clone());
        pathMoveCost = map[p.x][p.y].lowestCost;
        Direction d;
        while ((d = map[p.x][p.y].previous) != Direction.NONE){
            addFirst(d);
            d.opposed().move(p);
            points.addFirst((Point)p.clone());
        }
    }

    private class Node implements Comparable<Node>{
        public Direction previous;
        public final int x,y,moveCost;
        public int lowestCost;
        public final boolean canStop;

        public Node(int x, int y, int lowestCost){
            this.x          = x;
            this.y          = y;
            this.canStop    = unit.canStop(offset.x + x, offset.y + y);
            Integer i       = unit.moveCost(offset.x + x, offset.y + y);
            this.moveCost   = i == null ? lowestCost : i;
            this.lowestCost = lowestCost;
            this.previous   = Direction.NONE;
        }

        /*
        * return 0 if distances and the same, <0 if this is closer than n and >0 otherwise
        */
        public int compareTo(Node n){
            return this.lowestCost-n.lowestCost;
        }

        public boolean canStop(){
            return canStop;
        }
    }

    @Override
    public /*synchronized*/ boolean add (Direction dir) {
        Point t = (Point)points.getLast().clone();
        dir.move(t);

        int i = points.lastIndexOf(t);
        if (i != -1) {
            while (points.size() != i+1){
                removeLast();
                pathMoveCost -= map[points.getLast().x][points.getLast().y].moveCost;
                points.removeLast();
            }
            return true;
        }

        if (pathMoveCost + map[t.x][t.y].moveCost > unit.getMoveQuantity()){
            t.translate(offset.x,offset.y);
            shorten(t);
            return true;
        }

        points.add(t);
        pathMoveCost += map[t.x][t.y].moveCost;
        return super.add(dir);
    }

    public void apply () {
        unit.move(this);
    }
}
