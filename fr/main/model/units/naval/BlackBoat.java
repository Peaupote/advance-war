package fr.main.model.units.naval;

import java.awt.Point;
import java.util.ArrayList;

import fr.main.model.Player;

import fr.main.model.units.Unit;
import fr.main.model.units.MoveType;
import fr.main.model.units.HealerUnit;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.SupplyUnit;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.WalkingUnit;

public class BlackBoat extends Unit implements NavalUnit,HealerUnit<AbstractUnit>,TransportUnit<WalkingUnit>,SupplyUnit<AbstractUnit> {

    public static final String NAME = "NavLog"; // => navire logistique
    public static final int PRICE   = 7500;

    private ArrayList<WalkingUnit> units = new ArrayList<WalkingUnit>();

    public BlackBoat(Player player, Point point){
        super(player, point, 60, MoveType.LANDER, 7, 1, null, null, NAME);
    }

    public BlackBoat(Player player, int x, int y){
        this(player, new Point(x,y));
    }

    public boolean canHeal(AbstractUnit u){
        return u!=null && this.getMoveQuantity()!=0;
    }

    public void heal(AbstractUnit u){
        //TODO : deal with the money (one must pay to heal)
        if (canHeal(u))
            u.addLife(1);
        else
            throw new RuntimeException("Cannot heal.");
    }

    public int getCapacity(){
        return 2;
    }

    public boolean isFull(){
        return getCapacity()!=units.size();
    }

    public ArrayList<WalkingUnit> getUnits(){
        return new ArrayList<WalkingUnit>(units);
    }

    public boolean canCharge(AbstractUnit u){
        return (u instanceof WalkingUnit) && !isFull();
    }

    public boolean charge(WalkingUnit u){
        if (!isFull())
            return units.add(u);
        return false;
    }

    public boolean remove(WalkingUnit u){
        return units.remove(u);
    }

    public boolean canSupply(AbstractUnit u){
        return u!=null && u.getPlayer()==this.getPlayer() && u.getFuel()!=null;
    }
}