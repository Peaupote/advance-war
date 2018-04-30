package fr.main.model.units;

import java.awt.Point;
import java.util.HashSet;
import java.util.Random;
import java.util.LinkedList;

import fr.main.model.units.weapons.*;
import fr.main.model.Universe;
import fr.main.model.MoveZone;
import fr.main.model.Node;
import fr.main.model.units.naval.NavalUnit;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.RepairBuilding;
import fr.main.model.buildings.Dock;
import fr.main.model.players.Player;
import fr.main.model.Direction;

import java.io.Serializable;

/**
 * Interface used to represent an unit
 */
@SuppressWarnings("serial")
public interface AbstractUnit extends Serializable {
    String fuelName="Carburant";

    /**
     * @return the horizontal position of the unit
     */
    int getX();
    /**
     * @return the vertical position of the unit
     */
    int getY();

    /**
     * @return a Point which is the position of the unit
     */
    default Point position() {
        return new Point(getX(), getY());
    }

    /**
     * changes the position of the unit
     */
    void setLocation(int x, int y);

    /**
     * What happens when the unit dies
     */
    void dies();
    /**
     * @return true if and only if the unit is dead
     */
    default boolean dead(){
        return getLife() <= 0;
    }

    /**
     * @return the life of the unit
     */
    int getLife();

    /**
     * @param life is the amount of life to add
     */
    default void addLife(int life){
        setLife(getLife()+life);
    }

    /**
     * @param life is the amount of life to remove
     * @return true if and only if the unit is still alive
     */
    default boolean removeLife(int life){
        return setLife(getLife()-life);
    }

    /**
     * @param life is the new amount of life of the unit
     * @return true if and only if the unit is still alive
     */
    boolean setLife(int life);
    /**
     * @return the player that owns the unit
     */
    Player getPlayer();
    /**
     * @param p is the new owner of the unit
     */
    void setPlayer(Player p);
    /**
     * @return the cost of the unit
     */
    int getCost();
    /**
     * @return the primary weapon of the unit
     */
    PrimaryWeapon getPrimaryWeapon();
    /**
     * @return the secondary weapon of the unit
     */
    SecondaryWeapon getSecondaryWeapon();

    /**
     * @param fog is a representation of the map, true if the tile if visible and false otherwise
     * @param b true to use "linear regression" (some tiles may not be visible if a mountain is between it and the unit for example)
     * Airunits can see every tiles in their range of vision.
     * For navalunits and landunits, it depends on the height of the terrain compared to the one of the unit, and if the terrain hide its content from the unit
     */
    void renderVision(boolean[][] fog, boolean b);
    default void renderVision (boolean[][] fog){
        renderVision(fog, true);
    }

    /**
     * @param map is the boolean map
     * Sets to true all tiles that can be attacked within one day (move + attack)
     */
    default void renderTargets(boolean[][] map){
        MoveZone m = getMoveMap();
        Node[][] n = m.map;

		int oX = m.offset.x, oY = m.offset.y, moveQuantity = getMaxMoveQuantity();
        
        for (int i = 0; i < n.length; i++)
            for (int j = 0; j < n[i].length; j++){
                if (n[i][j].canStop && n[i][j].lowestCost < moveQuantity)
                    renderTarget(map, oX + j, oY + i);
            }
    }

    /**
     * @param map is a representation of the map
     * @param x is the horizontal position from which we shoot
     * @param y is the vertical position from which we shoot
     * set all tiles of the map that can be attacked (with the main or secondary weapon) from the position (x,y) to true, whatever the unit on it
     */
    default void renderTarget (boolean[][] map, int x, int y) {
        if (getPrimaryWeapon() != null)
            getPrimaryWeapon().renderTarget(map, this, x, y);
        if (getSecondaryWeapon() != null)
            getSecondaryWeapon().renderTarget(map, this, x, y);
    }

    default void renderTarget (boolean[][] map){
        renderTarget(map, getX(), getY());
    }

    /**
     * @return a move zone which represents the part of the map on which the unit can go
     */
    default MoveZone getMoveMap(){
        Universe u = Universe.get();
        int move   = getMoveQuantity(),
            posX   = getX(), posY = getY(),
            x      = Math.min(move, posX), y = Math.min(move, posY),
            width  = x + 1 + Math.min(move, u.getMapWidth() - posX - 1), height = y + 1 + Math.min(move, u.getMapHeight() - posY - 1);

        Point offset = new Point(posX - x, posY - y);
        Node[][] map = new Node[height][width];


        // initialization
        for (int i = 0; i < height; i ++)
            for (int j = 0; j < width; j ++)
                // creation of the new nodes, by default there is no way to go to it
                map[i][j] = new Node(j, i, canStop(offset.x + j, offset.y + i), moveCost(offset.x + j, offset.y + i), move + 1);
        Node current = new Node(posX - offset.x, posY - offset.y, true, 0, 0);
        map[posY - offset.y][posX - offset.x] = current;
        // the node on which the unit is has a moveCost of 0

        // these two sets are used to know which one are already evaluated and which are yet to evaluate
        // the nodes that are to evaluate
        HashSet<Node> unsettled = new HashSet<Node>();
        // the nodes that are already evaluated
        HashSet<Node> settled   = new HashSet<Node>();

        // the first node to evaluate is the one on which the unit is
        unsettled.add(current);


        //  evaluation
        while (!unsettled.isEmpty()){
            // we get the closest node (the node for which there is a path with the smallest move quantity needed to go on it)
            Node actual = null;
            for (Node n : unsettled)
                if (actual == null || n.compareTo(actual) < 0)
                    actual = n;
            unsettled.remove(actual);
            // could be quickier if we used priority list or something like that but the distance to the node may change
            // so if we did so we may need to remove some elements and put them back in the list
            // and other operations would take longer

            for (Direction d : Direction.cardinalDirections())
                // we add to the unsettled list all adjacent nodes (except those who are already evaluated)
                if (actual.x + d.x < width && actual.x + d.x >= 0 && actual.y + d.y < height && actual.y + d.y >= 0){
                    Node target = map[actual.y+d.y][actual.x+d.x];
                    if (!settled.contains(target) && actual.lowestCost + target.moveCost < target.lowestCost){
                        // if this path is shorter, change the move quantity needed to go on this Node
                        target.previous = d;
                        target.lowestCost = actual.lowestCost + target.moveCost;
                        unsettled.add(target);
                    }
                }
            settled.add(actual);
        }
        return new MoveZone(map, offset);
        // the object MoveZone is used to return both the offset and the Node[][] map
    }

    default LinkedList<Direction> findPath(Point point){
        return findPath(point, 2);
    }

    /**
     * @param point the target tile
     * @param approx the acceptable distance to the target point 
     * @return a list of Direction which is a path to go to the target tile (it is the shortest path)
     */
    default LinkedList<Direction> findPath(Point point, int approx){
        Universe u = Universe.get();
        int posX   = getX(), posY = getY(),
            max    = Math.max(u.getMapWidth(), u.getMapHeight());

        Node[][] map = new Node[u.getMapHeight()][u.getMapWidth()];

        // initialization
        for (int i = 0; i < map.length; i ++)
            for (int j = 0; j < map[i].length; j ++)
                // creation of the new nodes, by default there is no way to go to it
                map[i][j] = new Node(j, i, canStop(j, i), moveCost(j, i), max);
        Node current = new Node(posX, posY, true, 0, 0),
             pt = map[point.y][point.x];
        map[posY][posX] = current;
        // the node on which the unit is has a moveCost of 0

        // these two sets are used to know which one are already evaluated and which are yet to evaluate
        // the nodes that are to evaluate
        HashSet<Node> unsettled = new HashSet<Node>();
        // the nodes that are already evaluated
        HashSet<Node> settled   = new HashSet<Node>();

        // the first node to evaluate is the one on which the unit is
        unsettled.add(current);


        //  evaluation
        while (!unsettled.isEmpty() && pt.previous == Direction.NONE){
            // we get the closest node (the node for which there is a path with the smallest move quantity needed to go on it)
            Node actual = null;
            for (Node n : unsettled)
                if (actual == null || n.compareTo(actual) < 0)
                    actual = n;
            unsettled.remove(actual);
            // could be quickier if we used priority list or something like that but the distance to the node may change
            // so if we did so we may need to remove some elements and put them back in the list
            // and other operations would take longer

            for (Direction d : Direction.cardinalDirections())
                // we add to the unsettled list all adjacent nodes (except those who are already evaluated)
                if (u.isValidPosition(actual.x + d.x, actual.y + d.y)){
                    Node target = map[actual.y+d.y][actual.x+d.x];
                    if (!settled.contains(target) && actual.lowestCost + target.moveCost < target.lowestCost){
                        // if this path is shorter, change the move quantity needed to go on this Node
                        target.previous = d;
                        target.lowestCost = actual.lowestCost + target.moveCost;
                        unsettled.add(target);
                    }
                }
            settled.add(actual);
        }

        LinkedList<Direction> path = new LinkedList<Direction>();
        Direction d;
        Point ptt = point.getLocation();

        int[][] tab = Direction.getNonCardinalDirections();
        for (int i = 0; i <= approx; i++)
            for (int j = 0; j <= i; j++)
                for (int[] t : tab){
                    ptt.move(point.x + t[0] * j, point.y + t[1] * (i - j));   
                    if (u.isValidPosition(ptt.x, ptt.y) && map[ptt.y][ptt.x].previous != Direction.NONE) {
                        while ((d = map[ptt.y][ptt.x].previous) != Direction.NONE) {
                            path.addFirst(d);
                            d.opposed().move(ptt);
                        }
                        return path;                    
                    }
                }

        return path;
    }

    /**
     * @return the move type of the unit
     */
    MoveType getMoveType();
    /**
     * @param u is the unit we want to attack
     * @return true if and only if the unit can attack the one given as a parameter
     */
    boolean canAttack(AbstractUnit u);
    /**
     * @return true if and only if the unit may attack something (doesn't depend on the thing)
     */
    boolean canAttack();

    /**
     * @param u is the unit we want to attack
     * The unit attack the argument who can counter attack
     */
    default void attack(AbstractUnit u){
        attack(u,true);
    }

    /**
     * @param u is the unit we want to attack
     * @param counter is true to allow a counter attack from the unit given in parameter
     */
    void attack(AbstractUnit u, boolean counter);

    /**
     * @return the name of the unit
     */
    String getName();
    /**
     * @return the fuel of the unit
     */
    Unit.Fuel getFuel();
    /**
     * @return the real vision of the unit (it depends on the commander)
     */
    default int getVision(){
        return getPlayer().getCommander().getVision(this);
    }
    /**
     * @return the basic vision of the unit
     */
    int getBaseVision();
    /**
     * @return the fuel that is used every turn to stay alive
     */
    int getFuelTurnCost();
    /**
     * @return the move quantity of the unit
     */
    int getMoveQuantity();
    /**
     * @return the maximum move quantity of the unit
     */
    int getMaxMoveQuantity();
    /**
     * @param x the new move quantity of the unit
     */
    void setMoveQuantity(int x);

    /**
     * @param q is the move quantity to remove to the unit
     */
    default void removeMoveQuantity(int q){
        setMoveQuantity(getMoveQuantity() - q);
    }

    /**
     * @return true if and only if the unit still has move points
     */
    default boolean isEnabled(){
        return getMoveQuantity() != 0;
    }

    /**
     * move the unit to the specified location
     * (unlike setLocation it doesn't really teleport because fuel is spent, plus it changes the map of units of the Universe)
     */
    boolean move(int x, int y);

    /**
     * @param d is the direction in which the unit moves
     * @return true if and only if the move was done.
     */
    default boolean move (Direction d) {
        Point t = new Point(getX(),getY());
        d.move(t);
        return move(t.x, t.y);
    }

    /**
     * @param path is the path the unit has to follow
     * @return true if and only if the move was done entirely.
     */
    default boolean move (Path path) {
        for (Direction d: path){
            if (!move(d)){
                setMoveQuantity(0);
                return false;
            }
        }
        return true;
    }

    /**
     * Things that happen at the beggining of the turn
     */
    default void turnBegins(){
        AbstractBuilding building = Universe.get().getBuilding(getX(), getY());
        if (building != null && building instanceof RepairBuilding)
          ((RepairBuilding)building).repair(this);
        else getFuel().consume(getFuelTurnCost());
    }

    /**
     * Things that happen at the end of the turn
     */
    default void turnEnds(){
        setMoveQuantity(getMaxMoveQuantity());
    }

    /**
     * @return the move quantity needed to go on the point given in parameter
     */
    default Integer moveCost(int x, int y){
        Universe u = Universe.get();
        if (u.isVisibleOpponentUnit(x,y)) // if there is an opponent unit we can't go through
            return null;
        else if (this instanceof NavalUnit && u.getBuilding(x,y) instanceof Dock) // if this is a ship, it can go in a dock
            return 1;
        else
            return u.getTerrain(x,y).moveCost(this);
    }

    /**
     * @return true if and only if the unit can stop on the given position
     */
    default boolean canStop (Point pt) {
      return canStop(pt.x, pt.y);
    }

    /**
     * @return true if and only if the unit can stop on the tile
     */
    default boolean canStop (int x, int y){
        Universe u = Universe.get();
        return (u.getUnit(x,y) == null || (u.getUnit(x, y).getPlayer() != getPlayer() && !u.isVisibleOpponentUnit(x, y))) &&
                ((this instanceof NavalUnit && u.getBuilding(x,y) instanceof Dock) || u.getTerrain(x,y).canStop(this));
    }

    /**
     * @param attacker is the unit that attacks
     * @param b is the weapon used to attack : true for the primary weapon and false for the secondary weapon
     * @param defender is the unit that defends
     * @return the damage inflicted by the attacker to the defender with the weapon
     */
    public static int damage(AbstractUnit attacker, int life_attacker, boolean b, AbstractUnit defender){
        Weapon w    = b ? attacker.getPrimaryWeapon() : attacker.getSecondaryWeapon();
        if (w == null || ! w.canAttack(attacker,defender)) return 0;

        int d       = w.damage(defender);
        int aCO     = attacker.getPlayer().getCommander().getAttackValue(attacker);
        Random rand = new Random(); int r = rand.nextInt(1000);
        int aHP     = life_attacker;
        int dCO     = defender.getPlayer().getCommander().getDefenseValue(defender);
        AbstractBuilding building = Universe.get().getBuilding(defender.getX(), defender.getY());
        int dTR     = Universe.get().getTerrain(defender.getX(),defender.getY()).getDefense(defender) + (building == null ? 0 : building.getDefense(defender));
        int dHP     = defender.getLife();

        // the formula isn't obvious but works nicely
        return Math.min(100, Math.max(0, (d * aCO + r) * aHP * (2000 - 10 * dCO - dTR * dHP) / 10000000));
    }

    /**
     * Same method with default life_attacker (the actual life of the attacker)
     */
    public static int damage(AbstractUnit attacker, boolean b, AbstractUnit defender){
        return damage(attacker, attacker.getLife(), b, defender);
    }
}
