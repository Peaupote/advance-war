package fr.main.model.units;

import fr.main.model.Player;

import java.io.Serializable;

public interface AbstractUnit extends Serializable {
	int getX();
	int getY();
	boolean removeLife(int life);
	void addLife(int life);
	boolean setLife(int life);
	Player getPlayer();
	void renderVision(boolean[][] fog);
    void reachableLocation (boolean[][] map);
    void canTarget (boolean[][] map);
	MoveType getMoveType();
	boolean canAttack(AbstractUnit u);
	boolean canAttackAfterMove();
	String getName();
	Unit.Fuel getFuel();
	int getMoveQuantity();
	int getMaxMoveQuantity();
	void setMoveQuantity(int x);
}
