package fr.main.model.players;

import fr.main.model.Universe;
import fr.main.model.buildings.*;
import fr.main.model.units.AbstractUnit;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Random;

public class EconomicAI implements ArtificialIntelligence {

    public final AIPlayer player;
    private final HashSet<FactoryBuilding> factories = new HashSet<FactoryBuilding>();

    public EconomicAI (AIPlayer player){
    	this.player = player;
    }

    public void add(OwnableBuilding b){
        if (b instanceof Airport || b instanceof Dock || b instanceof Barrack) factories.add((FactoryBuilding)b);
    }

    public void remove(OwnableBuilding b){
    	factories.remove(b);
    }

    /**
     * Choose the units to create with the buildings
     */
    public void run (){ // TODO : improve the unit creation method (ie less randomness)
        // total random
        Random rand = new Random();
		Universe u = Universe.get();

        ArrayList<FactoryBuilding> factoriesCopy = new ArrayList<FactoryBuilding>();
        for (FactoryBuilding b : factories)
        	if (u.getUnit(b.getX(), b.getY()) == null)
        		factoriesCopy.add(b);

        for (int i = 0; i < 50 && factoriesCopy.size() > 0; i++){
            FactoryBuilding f = factoriesCopy.get(rand.nextInt(factoriesCopy.size()));
            if (f.create(get(f.getUnitList(), rand.nextInt(f.getUnitList().size()))))
                factoriesCopy.remove(f);
        }
    }

    /**
     * @param set is the set of units that can be created
     * @param n is the number of the unit to return
     * @return the n th unit of the set
     * This method exists because set don't have any ordering
     */
    private final Class<? extends AbstractUnit> get(Set<Class<? extends AbstractUnit>> set, int n){
        for (Class<? extends AbstractUnit> c : set)
            if (n == 0) return c;
            else n--;
        return null;
    }
}