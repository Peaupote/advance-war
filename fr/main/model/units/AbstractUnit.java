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
    Unit.MoveType getMoveType();
    void renderVision(boolean[][] fog);

}
