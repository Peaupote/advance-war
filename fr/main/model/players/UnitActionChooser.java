package fr.main.model.players;

import fr.main.model.Universe;
import fr.main.model.Direction;
import fr.main.model.MoveZone;
import fr.main.model.Node;
import fr.main.model.commanders.*;
import fr.main.model.buildings.*;
import fr.main.model.units.*;
import fr.main.model.units.land.*;
import fr.main.model.units.naval.*;
import fr.main.model.units.air.*;

import java.awt.Point;
import java.util.Random;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Consumer;

/*
    Import some classes of the view for the actions of the units 
 */
import fr.main.view.render.UniverseRenderer;
import fr.main.view.render.PathRenderer;


/**
 * Class used to find the action of an unit
 * It compares the possibilities to find the perfect one
 * Each instance is an FSM (finite state machine) which manages one unit (with total independence with other units)
 */
public class UnitActionChooser implements java.io.Serializable {

    /**
     * Represents the differents states of the FSM
     * Different actions are done depending on the state of the unit
     */
    static enum State implements java.io.Serializable {
        AIMLESS   (u -> u::aimless),   // goes by the map without clear objective
        GO        (u -> u::go),        // goes toward a location
        FLEE      (u -> u::flee),      // flee from an opponent
        FLEE_HEAL (u -> u::flee_heal), // flee from an opponent toward a place to get healed
        DEFEND    (u -> u::defend),    // defend a specific tile or building or unit
        ATTACK    (u -> u::attack),    // attack opponents
        OBJECTIVE (u -> u::objective); // an objective is a tile, a building or an unit this unit wants to reach
                                       // once the objective is reached, a specific action is done

        private final Function<UnitActionChooser,Runnable> action;

        private State(Function<UnitActionChooser,Runnable> action){
            this.action = action;
        }

        public void apply (UnitActionChooser u){
            action.apply(u).run();
        }
    }

    public static final Random rand = new Random();

    public final AbstractUnit unit;

    private State state;

    private transient Runnable action;
    private transient Node[][] localMap;
    private transient Point offset;

    public final boolean attack;
    public final boolean indirect;
    public final boolean supply;
    public final boolean heal;
    public final boolean capture;
    public final boolean hide;
    public final boolean transport;

    public UnitActionChooser(AbstractUnit unit){
        this.unit     = unit;
        this.state    = findState();
        this.localMap = null;
        this.offset   = null;
        this.action   = null;

        attack    = unit.getPrimaryWeapon() != null || unit.getSecondaryWeapon() != null;
        indirect  = unit.getPrimaryWeapon() != null && !unit.getPrimaryWeapon().isContactWeapon();
        supply    = unit instanceof SupplyUnit;
        heal      = unit instanceof HealerUnit;
        capture   = unit instanceof CaptureBuilding;
        hide      = unit instanceof HideableUnit;
        transport = unit instanceof TransportUnit;
    }

    /**
     * @return the state the unit should be in according to what is around it
     */
    public State findState(){
        return State.AIMLESS;
    }

    /**
     * Finds the perfect action for the unit
     * @return the action it found to be the best
     */
    public Runnable findAction(){
        MoveZone m    = unit.getMoveMap();
        this.offset   = m.offset;
        this.localMap = m.map;
        state.apply(this);
        return action;
    }

    /**
     * @return the action of the unit
     */
    public Runnable getAction(){
        if (action == null) findAction();
        return action;
    }






    /**
     * Runnable doing what its name indicates
     */
    public void aimless (){
        ArrayList<Point> points = new ArrayList<Point>();
        for (int i = 0; i < localMap.length; i++)
            for (int j = 0; j < localMap[i].length; j++)
                if (localMap[i][j].canStop)
                    points.add(new Point(i ,j));
        if (points.isEmpty()) action = () -> {};
        final Point pt = points.get(rand.nextInt(points.size()));
        pt.translate(offset.x, offset.y);

        action = () -> {
            PathRenderer path = new PathRenderer(((UniverseRenderer)Universe.get()).controller.camera);
            path.rebase(unit);
            path.add(pt);
            path.apply();
        };
    }

    /**
     * Runnable doing what its name indicates
     */
    public void go (){


        action = () -> {};
    }

    /**
     * Runnable doing what its name indicates
     */
    public void flee (){
        action = () -> {};
    }

    /**
     * Runnable doing what its name indicates
     */
    public void flee_heal (){
        action = () -> {};
    }

    /**
     * Runnable doing what its name indicates
     */
    public void defend (){
        action = () -> {};
    }

    /**
     * Runnable doing what its name indicates
     */
    public void attack (){
        action = () -> {};
    }

    /**
     * Runnable doing what its name indicates
     */
    public void objective (){
        action = () -> {};
    }




    /* all these methods return how much points it earns to do the corresponding action */

    /**
     * @param move is the tile to which the unit will move
     * @return how much points it earns to move to the point
     */
    private int move           (Point move) {
        Universe u = Universe.get();
        int p = 0;
        for (int i = -5; i <= 5; i++)
            for (int j = -5; j <= 5; j++){
                AbstractUnit aU = u.getUnit(unit.position());
                if (aU != null){
                    if (aU.getPlayer() == unit.getPlayer()) p += aU.getCost() / 1000;
                    else if (attack){
                        if (indirect && aU.canAttack(unit)) p -= aU.getCost() / 1000;
                        else {
                            int damage2 = AbstractUnit.damage(unit,                     unit.getPrimaryWeapon() != null && unit.getPrimaryWeapon().canAttack(unit, aU), aU),
                                damage1 = AbstractUnit.damage(aU, aU.getLife() - damage2, aU.getPrimaryWeapon() != null &&   aU.getPrimaryWeapon().canAttack(aU, unit), unit);
                            if (damage2 > damage1)
                                p += aU.getCost() * damage2 / 1000;
                            else 
                                p -= unit.getCost() * (damage2 - damage1) / 1000;
                        }
                    }
                    else if (aU.canAttack(unit)) p -= aU.getCost() / 1000;
                }
                AbstractBuilding aB = u.getBuilding(unit.position());
                if (aB != null) p += 20;
            }
        return p;
    }

    /**
     * @param move is the tile to which the unit will move
     * @param attacked is the unit to attack
     * @return how much points it earns to move to the point and then attack the unit
     */
    private int attack         (Point move, AbstractUnit attacked) {
        int a = AbstractUnit.damage(unit,                                 unit.getPrimaryWeapon() != null &&     unit.getPrimaryWeapon().canAttack(unit,attacked), attacked);
        int b = AbstractUnit.damage(attacked, attacked.getLife() - a, attacked.getPrimaryWeapon() != null && attacked.getPrimaryWeapon().canAttack(attacked,unit), unit);
        return move(move) + (a >= b ? 1 : -1) * (a * attacked.getCost() / 100 - b * unit.getCost() / 100) + (a >= attacked.getLife() ? 10 : 0);
    }

    /**
     * @param move is the tile to which the unit will move
     * @param healed is the unit to heal
     * @return how much points it earns to move to the point and then heal the unit
     */
    private int heal           (Point move, AbstractUnit healed) {
        return move(move) + unit.getPlayer().getFunds() >= healed.getCost() / 10 && healed.getLife() <= 90 ? healed.getCost() / 1000 : 0;
    }

    /**
     * @param move is the tile to which the unit will move
     * @return how much points it earns to move to the point and then supply
     */
    private int supply         (Point move) {
        int n = 0;
        for (Direction d : Direction.cardinalDirections())
            if (((SupplyUnit)unit).canSupply(Universe.get().getUnit(move.x + d.x, move.y + d.y))) n += 5;
        return move(move) + n;
    }

    /**
     * @param captured is the building to capture
     * @return how much points it earns to move to the building and then capturing it
     */
    private int capture        (AbstractBuilding captured) {
        return move(new Point(captured.getX(), captured.getY())) + 150;
    }

    /**
     * @param move is the tile to which the unit will move
     * @return how much points it earns to move to the point and then hide or reveal (depends on if the unit is already hidden or not)
     */
    private int hide           (Point move){
        return move(move);
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