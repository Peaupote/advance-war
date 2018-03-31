package fr.main.model;

/**
 * Represent how a tile is seen by a specific unit, it is used in specific methods such as getMoveMap in AbstractUnit
 */
public class Node implements Comparable<Node>{
    /**
     * When coming from the position of the unit, the direction from which we arrive on the node.
     * For example if we go from l to i, then the path will be for example l-m-h-i (there are differents path possible) and the last direction if Right
     */
    /* a b c d e
       f g h i j
       k l m n o
       p q r s t
       u v w x y
    */
    public Direction previous;
    /**
     * The position of the map and its move cost for the unit
     */
    public final int x,y,moveCost;
    /**
     * The current lowestCost to go from the unit position to the node's position
     */
    public int lowestCost;
    /**
     * true if and only if the unit can stop on the tile
     */
    public final boolean canStop;

    public Node(int x, int y, boolean canStop, Integer moveCost, int lowestCost){
        this.x          = x;
        this.y          = y;
        this.canStop    = canStop;
        this.moveCost   = moveCost == null ? lowestCost : moveCost;
        this.lowestCost = lowestCost;
        this.previous   = Direction.NONE;
    }

    /**
     * @param n is the node to which the current Node is compared to
     * @return 0 if distances and the same, <0 if this is closer than n and >0 otherwise
     */
    public int compareTo(Node n){
        return this.lowestCost-n.lowestCost;
    }

    public boolean canStop(){
        return canStop;
    }
}
