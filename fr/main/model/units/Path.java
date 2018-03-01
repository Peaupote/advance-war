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

    public void shortestCalculus(){
        int width  = map.length;
        int height = map[0].length;
        int move=unit.getMoveQuantity();

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
            Node actual=unsettled.removeFirst();
            for (Direction d : Direction.notNullDirections()){
                if (actual.x+d.x<width && actual.x+d.x>=0 && actual.y+d.y<height && actual.y+d.y>=0){
                    Node target = map[actual.x+d.x][actual.y+d.y];
                    if (actual.lowestCost + target.moveCost < target.lowestCost){
                        target.previous = d;
                        target.lowestCost = actual.lowestCost + target.moveCost;
                        if (!settled.contains(target))
                            unsettled.add(target);
                    }
                }
            }
            settled.add(actual);
        }
    }

    public void shorten(){
        Point p=new Point(unit.getX()-offset.x,unit.getY()-offset.y);
        for (Direction d : this)
            d.move(p);
        shorten(p);
    }

    /*
    * @param p is the objective in the relative map
    */
    private void shorten(Point p){
        removeAll(this);
        Direction d;
        while ((d=map[p.x][p.y].previous)!=Direction.NONE){
            addFirst(d);
            d.opposed().move(p);
        }
    }

    private class Node{
        public Direction previous;
        public final int x,y,moveCost;
        public int lowestCost;

        public Node(int x, int y, int lowestCost){
            this.x=x;
            this.y=y;
            Integer i = Universe.get().getTerrain(offset.x+x,offset.y+y).moveCost(unit);
            this.moveCost   = (i==null?lowestCost:i);
            this.lowestCost = lowestCost;
            this.previous   = Direction.NONE;
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
            pathMoveCost+=map[t.x][t.y].moveCost;
        }

        t = (Point)t.clone();
        dir.move(t);
        pathMoveCost+=map[t.x][t.y].moveCost;
        int i = pts.indexOf(t);
        if (i != -1) {
            for (int j = i; j < pts.size() - 1; j++) removeLast();
            return true;
        }


        if (pathMoveCost>unit.getMoveQuantity()){
            shorten(t);
            return true;
        }

        return super.add(dir);
    }

    public void apply () {
        unit.move(this);
    }

}