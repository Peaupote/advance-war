package fr.main.model.units;

import java.util.LinkedList;
import java.awt.Point;

import fr.main.model.Direction;
import fr.main.model.Universe;

public class Path extends LinkedList<Direction> {

    protected AbstractUnit unit;

    /*
    * assuming each terrain move cost is at least 1, we can only represent the part of the map on which the unit can move
    */
    private Node[][] map;
    private Point offset;

    public Path () {
        super();
    }

    public void rebase (AbstractUnit unit) {
        removeAll(this);
        this.unit = unit;

        Universe u = Universe.get();

        int move = unit.getMoveQuantity();
        int posX = unit.getX();
        int posY = unit.getY();
        int x    = Math.min(move,posX);
        int y    = Math.min(move,posY);

        offset = new Point(posX-x,posY-y);
        map    = new Node[x+Math.min(move,u.getMapWidth()-posX)+1][y+Math.min(move,u.getMapHeight()-posY)+1];
        shortestCalculus();
    }

    private final Node removeMin(LinkedList<Node> l){
        Node node=l.getFirst();
        for (Node n : l)
            if (n.compareTo(node)<0)
                node=n;
        l.remove(node);        
        return node;
    }

    public void shortestCalculus(){
        int width  = map.length,
            height = map[0].length,
            move   = unit.getMoveQuantity();

        // initialization
        for (int i=0;i<width;i++)
            for (int j=0;j<height;j++)
                map[i][j]=new Node(i,j,move+1);

        LinkedList<Node> unsettled = new LinkedList<Node>();
        LinkedList<Node> settled   = new LinkedList<Node>();

        Node current=map[unit.getX()-offset.x][unit.getY()-offset.y];
        current.lowestCost=0;
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
        Point p = new Point(unit.getX(), unit.getY());
        for (Direction d : this)
            d.move(p);
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
        Direction d;
        while ((d = map[p.x][p.y].previous) != Direction.NONE){
            addFirst(d);
            d.opposed().move(p);
        }
    }

    private class Node implements Comparable<Node>{
        public Direction previous;
        public final int x,y,moveCost;
        public int lowestCost;

        public Node(int x, int y, int lowestCost){
            this.x=x;
            this.y=y;
            Integer i = unit.moveCost(offset.x+x,offset.y+y);
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
    }

    @Override
    public boolean add (Direction dir) {
        Point t = new Point (unit.getX()-offset.x, unit.getY()-offset.y);
        LinkedList<Point> pts = new LinkedList<>();
        pts.add(t);

        int pathMoveCost=0;
        for (Direction d: this) {
            t = (Point)t.clone();
            d.move(t);
            pts.add(t);
            pathMoveCost += map[t.x][t.y].moveCost;
        }

        t = (Point)t.clone();
        dir.move(t);
        pathMoveCost += map[t.x][t.y].moveCost;
        int i = pts.indexOf(t);
        if (i != -1) {
            for (int j = i; j < pts.size() - 1; j++) removeLast();
            return true;
        }


        if (pathMoveCost > unit.getMoveQuantity()){
            t.translate(offset.x,offset.y);
            shorten(t);
            return true;
        }

        return super.add(dir);
    }

    public void apply () {
        makeAppliable();
        unit.move(this);
    }

    protected void makeAppliable(){
        Universe u = Universe.get();
        Point p    = new Point(unit.getX(),unit.getY());
        for (Direction d : this) d.move(p);
        while (!u.getTerrain(p.x,p.y).canStop(unit))
            removeLast().opposed().move(p);
    }

}
