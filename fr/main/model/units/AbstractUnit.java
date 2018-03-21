package fr.main.model.units;

import java.awt.Point;
import java.util.HashSet;

import fr.main.model.Universe;
import fr.main.model.MoveZone;
import fr.main.model.Node;
import fr.main.model.units.naval.NavalUnit;
import fr.main.model.terrains.Buildable;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.RepairBuilding;
import fr.main.model.buildings.Dock;
import fr.main.model.Player;
import fr.main.model.Direction;

import java.io.Serializable;

public interface AbstractUnit extends Serializable {

    String fuelName="Carburant";

    int getX();
    int getY();

    default Point position() {
        return new Point(getX(), getY());
    }

    int getLife();

    default void addLife(int life){
        setLife(getLife()+life);
    }

    default boolean removeLife(int life){
        return setLife(getLife()-life);
    }

    boolean setLife(int life);
    Player getPlayer();
    void setPlayer(Player p);
    int getCost();

    /*
    * Airunits can see every tiles in range of their vision.
    * For navalunits and landunits, it depends on the height of the terrain compared to the one of the unit, and if the terrain hide its content from the unit  
    */
    void renderVision(boolean[][] fog, boolean l);
    default void renderVision (boolean[][] fog){
        renderVision(fog, true);
    }

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
                map[i][j] = new Node(j, i, canStop(offset.x + j, offset.y + i), moveCost(offset.x + j, offset.y + i), move + 1);
        Node current = new Node(posX - offset.x, posY - offset.y, true, 0, 0);
        map[posY - offset.y][posX - offset.x] = current;

        HashSet<Node> unsettled = new HashSet<Node>();
        HashSet<Node> settled   = new HashSet<Node>();

        unsettled.add(current);

        //  evaluation
        while (!unsettled.isEmpty()){
            Node actual = null;
            for (Node n : unsettled)
                if (actual == null || n.compareTo(actual) < 0)
                    actual = n;
            unsettled.remove(actual);

            for (Direction d : Direction.cardinalDirections())
                if (actual.x + d.x < width && actual.x + d.x >= 0 && actual.y + d.y < height && actual.y + d.y >= 0){
                    Node target = map[actual.y+d.y][actual.x+d.x];
                    if (!settled.contains(target) && actual.lowestCost + target.moveCost < target.lowestCost){
                        target.previous = d;
                        target.lowestCost = actual.lowestCost + target.moveCost;
                        unsettled.add(target);
                    }
                }
            settled.add(actual);
        }
        return new MoveZone(map, offset);
    }

    /*
    * set all tiles of the map that can be attacked (with the main or secondary weapon) from the position (x,y) to true, whatever the unit on it
    */
    void renderTarget (boolean[][] map, int x, int y);

    /*
    * set all tiles of the map that can be attacked (with the main or secondary weapon) from any position attainable from (x,y) to true, whatever the unit on it
    */
    void renderAllTargets(boolean[][] map, int x, int y);

    default void renderTarget(boolean[][] map){
        renderTarget(map,getX(),getY());
    }
    default void renderAllTargets(boolean[][] map){
        renderAllTargets(map,getX(),getY());
    }

    MoveType getMoveType();
    boolean canAttack(AbstractUnit u);
    boolean canAttack();

    default void attack(AbstractUnit u){
        attack(u,true);
    }

    void attack(AbstractUnit u, boolean counter);

    String getName();
    Unit.Fuel getFuel();
    default int getVision(){
        return getPlayer().getCommander().getVision(this);
    }
    int getBaseVision();
    int getFuelTurnCost();
    int getMoveQuantity();
    int getMaxMoveQuantity();
    void setMoveQuantity(int x);

    default void removeMoveQuantity(int q){
        setMoveQuantity(getMoveQuantity()-q);
    }
    
    default boolean isEnabled(){
        return getMoveQuantity()!=0;
    }

    boolean move(int x, int y);

    /*
    * @return true if and only if the move was done.
    */
    default boolean move (Direction d) {
        Point t = new Point(getX(),getY());
        d.move(t);
        return move(t.x, t.y);
    }

    /*
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

    default void turnBegins(){
        AbstractBuilding building = Universe.get().getBuilding(getX(), getY());
        if (building != null && building instanceof RepairBuilding)
          ((RepairBuilding)building).repair(this);
        else getFuel().consume(getFuelTurnCost());
    }

    default void turnEnds(){
        setMoveQuantity(getMaxMoveQuantity());        
    }

    default Integer moveCost(int x, int y){
        Universe u = Universe.get();
        if (u.getUnit(x,y) != null && u.getUnit(x,y).getPlayer() != getPlayer()) // if there is an opponent unit we can't go through
            return null;
        else if (this instanceof NavalUnit && u.getBuilding(x,y) instanceof Dock) // if this is a ship, it can go in a dock
            return 1;
        else
            return u.getTerrain(x,y).moveCost(this);
    }
    default boolean canStop (int x, int y){
        Universe u = Universe.get();
        return u.getUnit(x,y) == null && ((this instanceof NavalUnit && u.getBuilding(x,y) instanceof Dock) || u.getTerrain(x,y).canStop(this));
    }
}
