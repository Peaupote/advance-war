package fr.main.model;

public class Node implements Comparable<Node>{
    public Direction previous;
    public final int x,y,moveCost;
    public int lowestCost;
    public final boolean canStop;

    public Node(int x, int y, boolean canStop, Integer moveCost, int lowestCost){
        this.x          = x;
        this.y          = y;
        this.canStop    = canStop;
        this.moveCost   = moveCost == null ? lowestCost : moveCost;
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
