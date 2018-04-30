package fr.main.model.players;

import fr.main.model.Universe;
import fr.main.model.buildings.*;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.land.Infantry;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Random;

/**
 * The economic AI chooses which units are to be created and where
 */
public class EconomicAI implements ArtificialIntelligence {

    /**
	 * Add EconomicAI UID
	 */
	private static final long serialVersionUID = 8076973207707324406L;
	private AIPlayer player;
    private final HashSet<FactoryBuilding> factories = new HashSet<FactoryBuilding>();

    public EconomicAI (AIPlayer player){
    	this.setPlayer(player);
    }

    public void add(OwnableBuilding b){
        if (b instanceof Airport || b instanceof Dock || b instanceof Barrack)
            factories.add((FactoryBuilding)b);
    }

	@SuppressWarnings("unlikely-arg-type")
	public void remove(OwnableBuilding b){
    	factories.remove(b);
    }

    public void loose(){
        factories.clear();
        setPlayer(null);
    }

    /**
     * Choose the units to create with the buildings
     */
    public void run (){ // TODO : improve the unit creation method (ie less randomness)
        // total random
        Random rand = new Random();
		Universe u = Universe.get();

        // we get a list of factories that can create units
        ArrayList<FactoryBuilding> factoriesCopy = new ArrayList<FactoryBuilding>();
        for (FactoryBuilding b : factories)
        	if (u.getUnit(b.getX(), b.getY()) == null)
        		factoriesCopy.add(b);

        // at the beginning of the game, the best to do is to create infantries to capture buildings
        // but the ai isn't smart enough to guess what type of unit is the best to create (units are created randomly)
        // and without checking anything
        // so we cheat a little and create infantries at first
        if (u.getDay() <= 5)
            for (FactoryBuilding f : factories)
                f.create(Infantry.class);

        // we randomly select one and create a random unit in it
        // 50 times
        for (int i = 0; i < 50 && ! factoriesCopy.isEmpty(); i++){
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

	public AIPlayer getPlayer() {
		return player;
	}

	public void setPlayer(AIPlayer player) {
		this.player = player;
	}
}