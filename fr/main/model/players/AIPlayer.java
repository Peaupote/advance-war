package fr.main.model.players;

import fr.main.model.Direction;
import fr.main.model.Universe;
import fr.main.model.MoveZone;
import fr.main.model.Node;
import fr.main.model.commanders.*;
import fr.main.model.buildings.*;
import fr.main.model.units.*;
import fr.main.model.units.land.*;
import fr.main.model.units.naval.*;
import fr.main.model.units.air.*;

import java.awt.Point;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * Represents an artificial intelligence
 */
public class AIPlayer extends Player{

    private static int num = 1;

    public AIPlayer(){
        super("IA " + num);
        num ++;
    }

    public synchronized void turnBegins(){
        super.turnBegins();
        new Thread(this::play).start();
    }

    /**
     * What the AI does when it plays
     */
    private void play(){
        System.out.println(name + " plays");
        try{ Thread.sleep(500); }
        catch(InterruptedException e){}

        HashSet<AbstractUnit> allUnits  = unitList();

        // first ranged units play
        checkUnits(allUnits.iterator(), u -> u.getPrimaryWeapon() != null && !u.getPrimaryWeapon().isContactWeapon());

        // then contact units play
        checkUnits(allUnits.iterator(), u -> u.getPrimaryWeapon() != null ||  u.getSecondaryWeapon() != null);

        // eventually other units play
        checkUnits(allUnits.iterator(), u -> true);

        // if one unit still has move points, we give it a try to do something
        // because the situation may have changed since the calculus of the thing to do for the unit
        checkUnits(unitList().iterator(), u -> u.isEnabled());

        createUnits();

        Universe.get().next();
    }

    /**
     * @param iterator is an iterator which contains the units that may move
     * @param test is the condition for the units to do something
     * this method checks all units of the iterator that pass the test, finds the good action and run it
     */
    private void checkUnits(Iterator<AbstractUnit> iterator, Predicate<AbstractUnit> test){
        while (iterator.hasNext()){
            AbstractUnit unit = iterator.next();
            if (test.test(unit)){
                (new UnitActionChooser(unit)).getAction().run();
                iterator.remove();
            }
        }
    }


    /**
     * Choose the units to create with the buildings
     */
    private void createUnits (){
        // TODO
    }
}

/**
 * Class used to find the action of an unit
 * It compares the possibilities to find the perfect one
 */
class UnitActionChooser {

    public  final AbstractUnit unit;
    private Runnable action;
    private MoveZone moveZone;

    public UnitActionChooser(AbstractUnit unit){
        this.unit     = unit;
        this.moveZone = unit.getMoveMap ();
        this.action   = null;
    }

    /**
     * Finds the perfect action for the unit
     */
    public void findAction(){
        this.action = () -> {};
    }

    /**
     * @return the action of the unit
     */
    public Runnable getAction(){
        if (action == null) findAction();
        return action;
    }



    /* all these methods return how much points it earns to do the corresponding action */

    /**
     * @param move is the tile to which the unit will move
     * @return how much points it earns to move to the point
     */
    private int move           (Point move) {
        return 0;
    }

    /**
     * @param move is the tile to which the unit will move
     * @param attacked is the unit to attack
     * @return how much points it earns to move to the point and then attack the unit
     */
    private int attack         (Point move, AbstractUnit attacked) {
        return move(move) + 0;
    }

    /**
     * @param move is the tile to which the unit will move
     * @param healed is the unit to heal
     * @return how much points it earns to move to the point and then heal the unit
     */
    private int heal           (Point move, AbstractUnit healed) {
        return move(move) + 0;
    }

    /**
     * @param move is the tile to which the unit will move
     * @return how much points it earns to move to the point and then supply
     */
    private int supply         (Point move) {
        return move(move) + 0;
    }

    /**
     * @param captured is the building to capture
     * @return how much points it earns to move to the building and then capturing it
     */
    private int capture        (AbstractBuilding captured) {
        return move(new Point(captured.getX(), captured.getY())) + 0;
    }

    /**
     * @param move is the tile to which the unit will move
     * @return how much points it earns to move to the point and then hide or reveal (depends on if the unit is already hidden or not)
     */
    private int hide           (Point move){
        return move(move) + 0;
    }

    /**
     * @param transport is the transport to go in
     * @return how much points it earns to go in the transport
     */
    private int goInTransport  (TransportUnit transport) {
        return 0;
    }

    /**
     * @param move is the tile to which the transport will move
     * @param toRemove is the unit to remove from the transport
     * @param dir is the direction in which the unit should be removed
     * @return how much points it earns to move to the point and then remove the unit on the tile specified by the direction
     */
    private int goOutTransport (Point move, AbstractUnit toRemove, Direction dir){
        return move(move) + 0;
    }
}