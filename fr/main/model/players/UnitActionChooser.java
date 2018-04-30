package fr.main.model.players;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Function;

import fr.main.model.Direction;
import fr.main.model.MoveZone;
import fr.main.model.Node;
import fr.main.model.Universe;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.buildings.RepairBuilding;
import fr.main.model.buildings.Headquarter;
import fr.main.model.buildings.FactoryBuilding;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.CaptureBuilding;
import fr.main.model.units.HealerUnit;
import fr.main.model.units.HideableUnit;
import fr.main.model.units.SupplyUnit;
import fr.main.model.units.TransportUnit;
import fr.main.model.units.Path;
import fr.main.model.units.weapons.PrimaryWeapon;


/*
    Import some classes of the view for the actions of the units 
 */
import fr.main.view.MainFrame;
import fr.main.view.render.buildings.BuildingRenderer;
import fr.main.view.render.UniverseRenderer;
import fr.main.view.Position;
import fr.main.view.render.units.UnitRenderer;

/**
 * Class used to find the action of an unit
 * It compares the possibilities to find the perfect one
 * Each instance is an FSM (finite state machine) which manages one unit (with total independence with other units)
 */
public class UnitActionChooser implements java.io.Serializable {

    /**
     * Add UnitActionChooser UID
     */
    private static final long serialVersionUID = 7642686049267806350L;

    /**
     * Represents the differents states of the FSM
     * Different actions are done depending on the state of the unit
     */
    public static enum State implements java.io.Serializable {
        AIMLESS   (u -> u::aimless),   // goes by the map without clear objective
        REPLENISH (u -> u::replenish), // bring the unit on a tile it can replenish (get fuel, ammo & get healed)
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

        private int distance (Point beg, Point end){
            return Math.abs(beg.x - end.x) + Math.abs(beg.y - end.y);
        }

        public int move (UnitActionChooser u, Point move){
            switch(u.getState()){
                case AIMLESS :
                    return 0;
                case REPLENISH :
                    return - 10 * distance(move, u.getTarget());
                case DEFEND :
                    int distance = distance(move, u.getTarget());
                    return distance <= 3 ? 10 : - 3 * (distance - 2);
                case ATTACK :
                    int dist = distance(move, u.getTarget());
                    return dist == 0 ? -20 : (dist <= 3 ? 10 : - 3 * (dist - 2));
                case OBJECTIVE :
                    return u.objective.move(move);
            }
            return 0;
        }

        public int attack (UnitActionChooser u, Point move, AbstractUnit attacked){
            switch(u.getState()){
                case AIMLESS :
                    return 0;
                case REPLENISH :
                    return -5;
                case DEFEND :
                    return 20;
                case ATTACK :
                    return 20;
                case OBJECTIVE :
                    return u.objective.attack(move, attacked);
            }
            return 0;
        }

        public int heal (UnitActionChooser u, Point move, AbstractUnit healed){
            switch(u.getState()){
                case AIMLESS :
                    return 0;
                case REPLENISH :
                    return 20;
                case DEFEND :
                    return 20;
                case ATTACK :
                    return 20;
                case OBJECTIVE :
                    return u.objective.heal(move, healed);
            }
            return 0;
        }

        public int supply (UnitActionChooser u, Point move){
            switch(u.getState()){
                case AIMLESS :
                    return 0;
                case REPLENISH :
                    return 20;
                case DEFEND :
                    return 20;
                case ATTACK :
                    return 20;
                case OBJECTIVE :
                    return u.objective.supply(move);
            }
            return 0;
        }

        public int capture (UnitActionChooser u, OwnableBuilding captured){
            switch(u.getState()){
                case AIMLESS :
                    return 0;
                case REPLENISH :
                    return 0;
                case DEFEND :
                    return 0;
                case ATTACK :
                    return 500;
                case OBJECTIVE :
                    return u.objective.capture(captured);
            }
            return 0;
        }
    }

    public static final Random rand = new Random();

    public final AbstractUnit unit;

    private State state;

    private transient Runnable action;
    private transient Node[][] localMap;
    private transient Point offset;

    public final boolean indirect, supply, heal, capture, hide, transport;

    public UnitActionChooser(AbstractUnit unit){
        this.unit      = unit;
        this.localMap  = null;
        this.offset    = null;
        this.action    = null;
        this.target    = null;
        this.objective = null;

        this.indirect  = unit.getPrimaryWeapon() != null && !unit.getPrimaryWeapon().isContactWeapon();
        this.supply    = unit instanceof SupplyUnit;
        this.heal      = unit instanceof HealerUnit;
        this.capture   = unit instanceof CaptureBuilding;
        this.hide      = unit instanceof HideableUnit;
        this.transport = unit instanceof TransportUnit;

        findState();
    }

    /**
     * finds the state the unit should be in according to what is around it and what is on the map
     */
    public void findState(){
        if (unit.getLife() < 30 || unit.getFuel().getQuantity() < 25 || (unit.getPrimaryWeapon() != null && unit.getPrimaryWeapon().getAmmunition() < 3)) {
            for (OwnableBuilding b : unit.getPlayer().buildingList())
                if (b instanceof RepairBuilding && ((RepairBuilding)b).canRepair(unit)){
                    LinkedList<Direction> path = unit.findPath(b.getLocation(), 3);
                    if (! path.isEmpty()){
                        final Objective o = new Objective(b.getLocation(), 0, path);
                        o.setAction(() -> {
                            if (unit.position().equals(b.getLocation()))
                                state = State.REPLENISH;
                            else
                                objective = o;
                        });
                        target = b.getLocation();
                        objective = o;
                        state = State.OBJECTIVE;
                        return;
                    }
                }
        }
        for (OwnableBuilding b : ((AIPlayer)unit.getPlayer()).unitControlAI.getObjectives())
            if (b.getOwner() != unit.getPlayer()){
                LinkedList<Direction> path = unit.findPath(b.getLocation(), 3);
                if (! path.isEmpty()){
                    target = b.getLocation();
                    objective = new Objective(b.getLocation(), 3, () -> state = State.ATTACK, path);
                    state = State.OBJECTIVE;
                    return;
                }
            }

        OwnableBuilding b = (new ArrayList<OwnableBuilding>(unit.getPlayer().buildingList())).get((new Random()).nextInt(unit.getPlayer().buildingList().size()));
        LinkedList<Direction> path = unit.findPath(b.getLocation(), 2);
        if (path.isEmpty())
            state = State.AIMLESS;
        else{
            target = b.getLocation();
            objective = new Objective(b.getLocation(), 3, () -> state = State.DEFEND, path);
            state = State.OBJECTIVE;
        }
    }

    public State getState(){
        return state;
    }

    /**
     * Finds the perfect action for the unit
     * @return the action it found to be the best
     */
    public Runnable findAction(){
        MoveZone m    = unit.getMoveMap();
        this.offset   = m.offset;
        this.localMap = m.map;
        this.action   = null;
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
     * This class describe an objective (used when mode is OBJECTIVE)
     */
    private class Objective implements java.io.Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -1886235246950234142L;
        /**
         * The location of the target tile
         */
        private final Point target;
        /**
         * The distance to the objective to consider it reached
         * If approx == 1, then if the unit is on a tile next to the target it is considered that the unit reached the target
         */
        private final int approx;
        /**
         * The action done once the objective is reached
         */
        private Runnable todo;
        /**
         * The timer is used to check that an objective is reachable, once the timer is 0 then the objective is re-calculated
         */
        private int timer;
        /**
         * The path to follow to go to the objective, can be checked sometimes to be sure it is still valid
         */
        private final LinkedList<Direction> path;

        public Objective (Point target, int approx, LinkedList<Direction> path){
            this(target, approx, null, path);
        }

        public Objective(Point target, int approx, Runnable todo, LinkedList<Direction> path){
            this.target = target;
            this.approx = approx;
            this.todo   = todo;
            this.path   = path;
            this.setTimer(5);
        }

        public Objective(Point target, Runnable todo, LinkedList<Direction> path){
            this(target, 2, todo, path);
        }

        public Objective(Point target, Runnable todo){
            this(target, todo, UnitActionChooser.this.unit.findPath(target, 2));
        }

        public Point getTarget() {
            return target;
        }

        public int getApprox() {
            return approx;
        }

        public void run() {
            todo.run();
        }

        public int getTimer() {
            return timer;
        }

        public void setTimer(int timer) {
            this.timer = timer;
        }

        public void decreaseTimer(){
            timer --;
            if (timer <= 0){
                path.clear();
                path.addAll(UnitActionChooser.this.unit.findPath(target, approx));
                setTimer(5);
            }
        }

        public LinkedList<Direction> getPath() {
            return path;
        }

        public void setAction(Runnable r){
            todo = r;
        }

        public int move(Point p){
            Point pt = unit.position();
            for (int i = 0; i < unit.getMoveQuantity(); i++){
                if (path.size() >= i + 1)
                    path.get(i).move(pt);
            }

            return - ( Math.abs(p.x - target.x) + Math.abs(p.y - target.y) ) * 10;
        }

        public int attack(Point p, AbstractUnit attacked){
            return 10;
        }

        public int heal(Point p, AbstractUnit healed){
            return 10;
        }

        public int supply(Point p){
            return 10;
        }

        public int capture (OwnableBuilding b){
            return 500;
        }
    }

    private Objective objective;
    /**
     * Used in ATTACK, DEFEND and HEAL modes
     */
    private Point target;

    public Point getTarget(){
        return target;
    }

    public void aimless (){
        action();
    }

    /**
     * Runnable doing what its name indicates : take the unit to a building where it may replenish
     */
    public void replenish (){
        action();
    }

    /**
     * Runnable doing what its name indicates : defend a position
     */
    public void defend (){
        action();
    }

    /**
     * Runnable doing what its name indicates : attack a position
     */
    public void attack (){
        action();
    }

    /**
     * Runnable doing what its name indicates : activates the objective
     */
    public void objective (){
        objective.decreaseTimer();
        if (unit.isEnabled() && Math.abs(unit.getX() - target.x) + Math.abs(unit.getY() - target.y) <= objective.getApprox())
            action = () -> {
                Objective oo = objective;
                objective = null;
                oo.run();
                if (objective == null && state == State.OBJECTIVE)
                    findState();
            };
        else
            action();
    }

    private void action (){
        int actionPoint = - Integer.MIN_VALUE;

        UniverseRenderer world = (UniverseRenderer)Universe.get();
        int[][] t = Direction.getNonCardinalDirections();
        int x = unit.getX(), y = unit.getY();

        boolean attackBis = unit.canAttack() && (unit.getPrimaryWeapon() == null || unit.getPrimaryWeapon().isContactWeapon());

        for (int i = 0; i < localMap.length; i++)
            for (int j = 0; j < localMap[i].length; j++)
                if (localMap[i][j].lowestCost < unit.getMoveQuantity()){
                    Point pt = new Point(offset.x + j, offset.y + i);
                    for (Direction d : Direction.cardinalDirections()){
                        AbstractUnit u = world.getUnit(pt.x + d.x, pt.y + d.y);

                        if (unit.canAttack(u)) {
                            int tmp = attack(pt, u);
                            if (tmp > actionPoint){
                                actionPoint = tmp;
                                action      = () -> {
                                    Path p = Path.getPath();
                                    p.rebase(unit);
                                    p.add(pt.getLocation());
                                    if (p.apply()){
                                        Position.Camera camera = world.controller.camera;
                                        int aLife = unit.getLife(),
                                            tLife = u.getLife();
                                        unit.attack(u);
                                        UnitRenderer.getRender(unit).attackSound();
                                        world.flash ("" + (unit.getLife() - aLife),
                                                    (unit.getX() - camera.getX() + 1) * MainFrame.UNIT + 5,
                                                    (unit.getY() - camera.getY()) * MainFrame.UNIT + 5, 1000,
                                                    UniverseRenderer.FlashMessage.Type.ALERT);
                                        world.flash ("" + (u.getLife() - tLife),
                                                    (u.getX() - camera.getX() + 1) * MainFrame.UNIT + 5,
                                                    (u.getY() - camera.getY()) * MainFrame.UNIT + 5, 1000,
                                                    UniverseRenderer.FlashMessage.Type.ALERT);
                                    }
                                };
                            }
                        }

                        if (heal && ((HealerUnit)unit).canHeal(u)){
                            int tmp = heal(pt, u);
                            if (tmp > actionPoint){
                                actionPoint = tmp;
                                action      = () -> {
                                    Path p = Path.getPath();
                                    p.rebase(unit);
                                    p.add(pt.getLocation());
                                    if (p.apply()){
                                        Position.Camera camera = world.controller.camera;
                                        int life = u.getLife();
                                        ((HealerUnit)unit).heal(u);
                                        world.flash("+" + (u.getLife() - life),
                                                  (u.getX() - camera.getX() + 1) * MainFrame.UNIT + 5,
                                                  (u.getY() - camera.getY()) * MainFrame.UNIT + 5, 1000);

                                    }
                                };
                            }
                        }

                    }

                    AbstractUnit u = world.getUnit(pt);
                    if (supply && ((SupplyUnit)unit).canSupply()){
                        int tmp = supply(pt);
                        if (tmp > actionPoint){
                            actionPoint = tmp;
                            action      = () -> {
                                Path p = Path.getPath();
                                p.rebase(unit);
                                p.add(pt.getLocation());
                                if (p.apply()){
                                    Position.Camera camera = world.controller.camera;
                                    ((SupplyUnit)unit).supply();
                                    for (Direction d : Direction.cardinalDirections()){
                                        int xx = unit.getX() + d.x, yy = unit.getY() + d.y;
                                        AbstractUnit unitBis = world.getUnit(xx, yy);
                                        if (((SupplyUnit)unit).canSupply(unitBis))
                                            world.flash("replenished",
                                                (xx - camera.getX()) * MainFrame.UNIT + 5,
                                                (yy - camera.getY()) * MainFrame.UNIT + 5, 1000,
                                                UniverseRenderer.FlashMessage.Type.ALERT);
                                    }
                                }
                            };
                        }
                    }

                    if (capture && ((CaptureBuilding)unit).canCapture(world.getBuilding(pt))){
                        final AbstractBuilding building = world.getBuilding(pt);
                        int tmp = capture((OwnableBuilding)building);
                        if (tmp > actionPoint){
                            actionPoint = tmp;
                            action      = () -> {
                                Path p = Path.getPath();
                                p.rebase(unit);
                                p.add(pt.getLocation());
                                if (p.apply())
                                    if (((CaptureBuilding)unit).capture(building))
                                        BuildingRenderer.getRender(building).updateState("");
                            };
                        }
                    }

                    int tmp = move(pt);
                    if (tmp > actionPoint){
                        actionPoint = tmp;
                        action = () -> {
                            Path p = Path.getPath();
                            p.rebase(unit);
                            p.add(pt.getLocation());
                            p.apply();
                        };
                    }
                }

        if (indirect && unit.getPrimaryWeapon().getAmmunition() != 0){
            PrimaryWeapon p = unit.getPrimaryWeapon();

            for (int i = p.getMinimumRange(unit); i <= p.getMaximumRange(unit); i++)
                for (int j = 0; j <= i; j++)
                    for (int[] d : t){
                        int xx = x + d[0] * (i - j), yy = y + d[1] * j;
                        if (unit.canAttack(world.getUnit(xx, yy))){
                            AbstractUnit u = world.getUnit(xx, yy);
                            int tmp = attack(unit.position(), u);
                            if (tmp > actionPoint){
                                actionPoint = tmp;
                                action = () -> unit.attack(u);
                            }
                        }

                    }
        }
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
                    else if (unit.canAttack(aU)) {
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
        return state.move(this, move) + p;
    }

    /**
     * @param move is the tile to which the unit will move
     * @param attacked is the unit to attack
     * @return how much points it earns to move to the point and then attack the unit
     */
    private int attack         (Point move, AbstractUnit attacked) {
        int a = AbstractUnit.damage(unit,                                 unit.getPrimaryWeapon() != null &&     unit.getPrimaryWeapon().canAttack(unit,attacked), attacked);
        int b = AbstractUnit.damage(attacked, attacked.getLife() - a, attacked.getPrimaryWeapon() != null && attacked.getPrimaryWeapon().canAttack(attacked,unit), unit);
        return state.attack(this, move, attacked) + move(move) + (a >= b ? 1 : -1) * (a * attacked.getCost() / 100 - b * unit.getCost() / 100) + (a >= attacked.getLife() ? 10 : 0);
    }

    /**
     * @param move is the tile to which the unit will move
     * @param healed is the unit to heal
     * @return how much points it earns to move to the point and then heal the unit
     */
    private int heal           (Point move, AbstractUnit healed) {
        return state.heal(this, move, healed) + move(move) + unit.getPlayer().getFunds() >= healed.getCost() / 10 && healed.getLife() <= 90 ? healed.getCost() / 1000 : 0;
    }

    /**
     * @param move is the tile to which the unit will move
     * @return how much points it earns to move to the point and then supply
     */
    private int supply         (Point move) {
        int n = 0;
        for (Direction d : Direction.cardinalDirections())
            if (((SupplyUnit)unit).canSupply(Universe.get().getUnit(move.x + d.x, move.y + d.y))) n += 5;
        return state.supply(this, move) + move(move) + n;
    }

    /**
     * @param captured is the building to capture
     * @return how much points it earns to move to the building and then capturing it
     */
    private int capture        (OwnableBuilding captured) {
        return state.capture(this, captured) + move(new Point(captured.getX(), captured.getY())) +
                    (captured instanceof Headquarter ? 1000 : (captured instanceof FactoryBuilding ? 500 : 250));
    }

    /**
     * @param move is the tile to which the unit will move
     * @return how much points it earns to move to the point and then hide or reveal (depends on if the unit is already hidden or not)
     */
    private int hide           (Point move){
        return 0;
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
        return 0;
    }
}