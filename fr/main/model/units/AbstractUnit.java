package fr.main.model.units;

import fr.main.model.Player;
import fr.main.model.units.MoveType;

import java.io.Serializable;

public interface AbstractUnit extends Serializable {
	int getX();
	int getY();
	boolean removeLife(int life);
	void addLife(int life);
	boolean setLife(int life);
	Player getPlayer();
	void renderVision(boolean[][] fog);
	MoveType getMoveType();

}
