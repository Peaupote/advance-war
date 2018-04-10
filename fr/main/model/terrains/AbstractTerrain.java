package fr.main.model.terrains;

import fr.main.model.Weather;
import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.MoveType;
import fr.main.model.units.weapons.PrimaryWeapon;

/**
 * Interface for all the terrains 
 */
@SuppressWarnings("serial")
public interface AbstractTerrain extends java.io.Serializable {
    /**
     * @return the height of the terrain
     * Heights of the different terrains :
     * 2 : mountains
     * 1 : hill
     * 0 : all others
     *
     * flying units are considered to have a height of 2 whatever the terrain, landunits and navalunits have the height of their terrain
     */
    public int getHeight();
    public int getDefense(AbstractUnit u);
    public int getBonusVision(AbstractUnit u);
    public int getBonusRange(AbstractUnit u, PrimaryWeapon p);
    /**
     * @param u is the unit we want to know if it can see the terrain
     * @return true if and only if the terrain hides its content from the unit given in parameter
     */
    public boolean hideFrom(AbstractUnit u);
    /**
     * @param w is the weather
     * @param mt is the move type of the unit moving
     * @return true if and only if the unit can go in the terrain with the current weather
     */
    public boolean canMoveIn(Weather w, MoveType mt);
    /**
     * @param w is the weather
     * @param mt is the move type of the unit moving
     * @return true if and only if the unit can stop on the terrain
     */
    public boolean canStop(Weather w, MoveType mt);
    /**
     * @param w is the weather
     * @param mt is the move type of the unit moving
     * @return an Integer which is null if the move type can't go on the terrain and the value of the move cost otherwise
     */
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