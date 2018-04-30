package fr.main.model.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Predicate;

import fr.main.model.Direction;
import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.buildings.Headquarter;

public class UnitControlAI implements ArtificialIntelligence {

	/**
	 * Add UnitControlAI UID
	 */
	private static final long serialVersionUID = 7107098815082828456L;
	private AIPlayer player;
	private final HashMap<AbstractUnit, UnitActionChooser> units;
    private final ArrayList<OwnableBuilding> objectives;

	public UnitControlAI(AIPlayer p){
		this.player     = p;
		this.units      = new HashMap<AbstractUnit, UnitActionChooser>();
        this.objectives = new ArrayList<OwnableBuilding>();
	}

    /**
     * When a headQuarter is captured, it is changed in a city, so the objectives list has to be updated
     * @param old the former building
     * @param gold the new building
     */
    public void changeBuilding(AbstractBuilding old, AbstractBuilding gold){
        if (gold instanceof OwnableBuilding){
            int tmp = objectives.indexOf(old);
            if (tmp != -1)
                objectives.add(tmp, (OwnableBuilding)gold);            
        }
    }

    public void loose(){
        units.clear();
        objectives.clear();
        player = null;
    }

    /**
     * Finds the player's headquarter and calls findObjectives
     */
    public void findObjectives(){
        for (OwnableBuilding b : player.buildingList()){
            if (b instanceof Headquarter){
                findObjectives((Headquarter)b);
                break;
            }
        }
    }

    /**
     * Finds all the buildings of the map from the closest of the headquarter to the farthest
     * @param hq is the headquarter of the player
     */
    private void findObjectives(Headquarter hq){
        this.objectives.clear();
        objectives.add(hq);

        Universe u = Universe.get();

        int x = hq.getX(), y = hq.getY();
        boolean bool = true;
        int[][] tab = Direction.getNonCardinalDirections();
        for (int i = 1; bool; i ++){
            bool = false;
            for (Direction d : Direction.cardinalDirections()){
                bool = bool || u.isValidPosition(x + d.x * i, y + d.y * i);
                AbstractBuilding b = u.getBuilding(x + d.x * i, y + d.y * i);
                if (b != null && b instanceof OwnableBuilding)
                    objectives.add((OwnableBuilding) b);
            }
            for (int j = 1; j < i; j++)
                for (int[] t : tab){
                    bool = bool || u.isValidPosition(x + j * t[0], y + (i - j) * t[1]);
                    AbstractBuilding b = u.getBuilding(x + j * t[0], y + (i - j) * t[1]);
                    if (b != null && b instanceof OwnableBuilding)
                        objectives.add((OwnableBuilding) b);
                }
        }
    }

	public void add(AbstractUnit u){
		units.put(u, new UnitActionChooser(u));
	}

	public void remove(AbstractUnit u){
		units.remove(u);
	}

	public void run(){
        // we have to use a copy of the list to avoid concurrent modification exception if an unit dies for example
        ArrayList<UnitActionChooser> unitList = new ArrayList<UnitActionChooser>(units.values());

        // first ranged units play
        checkUnits(unitList.iterator(), u -> u.getPrimaryWeapon() != null && !u.getPrimaryWeapon().isContactWeapon());

        // then contact units play
        checkUnits(unitList.iterator(), u -> u.getPrimaryWeapon() != null || u.getSecondaryWeapon() != null);

        // eventually other units play
        checkUnits(unitList.iterator(), u -> true);

        // if one unit still has move points, we give it a try to do something
        // because the situation may have changed since the first calculus of the thing to do
        checkUnits(unitList.iterator(), u -> true);

        if (Universe.get().getDay() % 10 == 0)
            for (UnitActionChooser uac : units.values())
                if (uac.getState() == UnitActionChooser.State.DEFEND || uac.getState() == UnitActionChooser.State.ATTACK || uac.getState() == UnitActionChooser.State.AIMLESS)
                    uac.findState();
	}

    /**
     * @param iterator is an iterator which contains the units that may move
     * @param test is the condition for the units to do something
     * this method checks all units of the iterator that pass the test, finds the good action and run it
     */
    private void checkUnits(Iterator<UnitActionChooser> iterator, Predicate<AbstractUnit> test){
        while (iterator.hasNext()){
            UnitActionChooser actionChooser = iterator.next();
            if (actionChooser.unit.isEnabled() && test.test(actionChooser.unit))
                actionChooser.findAction().run();
            if (!actionChooser.unit.isEnabled()) iterator.remove();
        }
    }

    public ArrayList<OwnableBuilding> getObjectives(){
        return new ArrayList<OwnableBuilding>(objectives);
    }

}