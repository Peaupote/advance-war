package fr.main.model.units;

import java.awt.Point;

import fr.main.model.Universe;
import fr.main.model.terrains.Buildable;
import fr.main.model.buildings.RepairBuilding;
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
    boolean setPlayer(Player p);
    int getCost();
    void renderVision(boolean[][] fog);
    void reachableLocation (boolean[][] map);

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
        try{
            ((RepairBuilding)((Buildable)Universe.get().getTerrain(getX(),getY())).getBuilding()).repair(this);
        }catch(java.lang.ClassCastException | NullPointerException e){
            getFuel().consume(getFuelTurnCost());
        }
    }

    default void turnEnds(){
        setMoveQuantity(getMaxMoveQuantity());        
    }
}
