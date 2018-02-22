package fr.main.model.units;

import fr.main.model.Player;
import fr.main.model.Direction;

import java.io.Serializable;

public interface AbstractUnit extends Serializable {

    String fuelName="Carburant";

    int getX();
    int getY();
    int getLife();
    void addLife(int life);
    boolean removeLife(int life);
    boolean setLife(int life);
    Player getPlayer();
    boolean setPlayer(Player p);
    int getCost();
    void renderVision(boolean[][] fog);
    void reachableLocation (boolean[][] map);
    void canTarget (boolean[][] map);
    MoveType getMoveType();
    boolean canAttack(AbstractUnit u);
    boolean canAttackAfterMove();
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
    boolean move(Direction d);
    boolean move(Path path);

    void turnBegins();

}
