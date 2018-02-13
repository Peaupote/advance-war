package fr.main.model.units;

import fr.main.model.Universe;

public interface SupplyUnit<T extends AbstractUnit> extends AbstractUnit {

    public boolean canSupply(AbstractUnit u);
    
    public default boolean canSupply(){
        Universe universe=Universe.get();
        for (int i=-1;i<2;i++){
            for (int j=-1;j<2;j++){
                if (Math.abs(i)+Math.abs(j)!=1)
                    continue;
                if (canSupply(universe.getUnit(getX()+i,getY()+j)))
                    return true;
            }
        }
        return false;
    }

    public default void supply(){
        Universe universe=Universe.get();
        for (int i=-1;i<2;i++){
            for (int j=-1;j<2;j++){
                if (Math.abs(i)+Math.abs(j)!=1)
                    continue;
                AbstractUnit u=universe.getUnit(getX()+i,getY()+j);
                if (canSupply(u))
                    u.getFuel().replenish();
            }
        }
        this.setMoveQuantity(0);
    }

}