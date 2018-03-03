package fr.main.model.terrains;

import fr.main.model.Weather;
import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;

public interface AbstractTerrain extends java.io.Serializable {

    public int getDefense(AbstractUnit u);
    public int getBonusVision(AbstractUnit u);
    public int getBonusRange(AbstractUnit u);
    public boolean hideFrom(AbstractUnit u);
    public boolean blockVision(AbstractUnit u);
    public boolean canMoveIn(Weather w, MoveType mt);
    public boolean canStop(Weather w, MoveType mt);
    public Integer moveCost(Weather w, MoveType mt);

    public default boolean canMoveIn(MoveType mt){
        return canMoveIn(Universe.get().getWeather(),mt);
    }
    public default boolean canMoveIn(AbstractUnit u){
        return canMoveIn(u.getMoveType());
    }
    public default boolean canMoveIn(Weather w, AbstractUnit u){
        return canMoveIn(w,u.getMoveType());
    }

    public default boolean canStop(AbstractUnit u){
        return canStop(u.getMoveType());
    }
    public default boolean canStop(Weather w, AbstractUnit u){
        return canStop(w,u.getMoveType());
    }
    public default boolean canStop(MoveType mt){
        return canStop(Universe.get().getWeather(),mt);
    }

    public default Integer moveCost(AbstractUnit u){
        return moveCost(u.getMoveType());
    }
    public default Integer moveCost(Weather w, AbstractUnit u){
        return moveCost(w,u.getMoveType());
    }
    public default Integer moveCost(MoveType mt){
        return moveCost(Universe.get().getWeather(),mt);
    }
}