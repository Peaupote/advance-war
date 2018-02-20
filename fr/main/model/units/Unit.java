package fr.main.model.units;

import java.awt.Point;
import java.io.Serializable;

import fr.main.model.Player;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.Universe;
/**
 * Represents a unit on the board
 */
public abstract class Unit implements AbstractUnit {

    /**
     * Life in percentage
     */
    private Point location;
    private Player player;
    private int life, moveQuantity;
    public boolean enable;

    public final Fuel fuel;
    public final MoveType moveType;

    private PrimaryWeapon primaryWeapon;
    private SecondaryWeapon secondaryWeapon;

    public final int vision, maxMoveQuantity;
    public final String name;

    public class Fuel implements java.io.Serializable{
        public final String name; // l'infanterie n'a pas de 'carburant' mais des 'rations' (c'est un détail mais bon)
        public final int maximumQuantity;
        private int quantity;

        public Fuel(int maximumQuantity){
            this("Carburant",maximumQuantity);
        }

        public Fuel(String name, int maximumQuantity){
            this.name            = name;
            this.maximumQuantity = maximumQuantity;
            this.quantity        = maximumQuantity;
        }

        /*
         *  @return true if the unit has no more fuel.
         */
        public boolean consume(int quantity){
            this.quantity=Math.max(0,this.quantity-quantity);
            return this.quantity==0;
        }

        public void replenish(){
            this.quantity=this.maximumQuantity;
        }
        public int getQuantity() {
            return quantity;
        }
    }

    public Unit (Point location) {
        this (null, location);
    }

    public Unit (Player player, Point location, int maxFuel, MoveType moveType, int moveQuantity , int vision, PrimaryWeapon primaryWeapon, SecondaryWeapon secondaryWeapon, String name) {
        this(player, location, "Carburant", maxFuel, moveType, moveQuantity, vision, primaryWeapon, secondaryWeapon, name);
    }

    public Unit (Player player, Point location) {
        this (player, location, "fuel", 0, MoveType.WHEEL, 5, 2, null, null, "unit");
    }

    public Unit (Player player, Point location, String fuelName, int maxFuel, MoveType moveType, int moveQuantity, int vision, PrimaryWeapon primaryWeapon, SecondaryWeapon secondaryWeapon, String name) {
        this.life            = 100;
        this.player          = player;
        this.location        = location;
        this.fuel            = new Fuel(fuelName,maxFuel);
        this.moveType        = moveType;
        this.maxMoveQuantity = moveQuantity;
        this.moveQuantity    = moveQuantity;
        this.vision          = vision;
        this.primaryWeapon   = primaryWeapon;
        this.secondaryWeapon = secondaryWeapon;
        this.name            = name;
        move(location.x, location.y);
    }

    public int getMoveQuantity(){
        return moveQuantity;
    }

    public int getMaxMoveQuantity(){
        return maxMoveQuantity;
    }

    public void setMoveQuantity(int m){
        this.moveQuantity=m;
    }

    public Fuel getFuel(){
        return fuel;
    }

    public final boolean removeLife(int life){
        return setLife(this.life-life);
    }

    public final void addLife(int life){
        setLife(this.life+life);
    }

    /*
    * @return true if and only if the unit is still alive
    */
    public final boolean setLife (int life) {
        this.life = Math.max(0, Math.min(100, life));
        return this.life!=0;
    }

    public final int getLife () {
        return life;
    }

    public final boolean setPlayer (Player p) {
        if (player == null) {
            this.player = p;
            return true;
        } else return false;
    }

    public final Player getPlayer(){
        return this.player;
    }

    public final int getX () {
        return location.x;
    }

    public final int getY () {
        return location.y;
    }

    public boolean canAttackAfterMove(){
        return true;
    }

    public final void move(int x, int y) {
      Universe u = Universe.get();
      if (u != null && u.getUnit(x, y) == null) {
        u.setUnit(location.x, location.y, null);
        location.x = x;
        location.y = y;
        u.setUnit(x, y, this);
        enable = false;
        u.updateVision();
      }
    }

    public final void move (Path path) {
      // TODO: make real implementation
      Point pt = path.getLast();
      move(pt.x, pt.y);
    }

    public final MoveType getMoveType() {
        return moveType;
    }

    public final void renderVision (boolean[][] fog) {
        if (fog==null || fog.length==0 || fog[0]==null || fog[0].length==0)
            return;

        int x=location.x;
        int y=location.y;

        fog[y][x]=true;

        if (vision!=0){
            if (y!=0){
                fog[y-1][x]=true;
                renderVision (fog,x,y-1,vision);
            }
            if (y!=fog.length-1){
                fog[y+1][x]=true;
                renderVision (fog,x,y+1,vision);
            }
            if (x!=0){
                fog[y][x-1]=true;
                renderVision (fog,x-1,y,vision);
            }
            if (x!=fog[0].length-1){
                fog[y][x+1]=true;
                renderVision (fog,x+1,y,vision);
            }
        }
    }

    private void renderVision (boolean[][] fog, int x, int y, int vision){
        vision--;
        AbstractTerrain t = Universe.get().getTerrain(x,y);
        if (!fog[y][x] && !t.hideFrom(this))
            fog[y][x]=true;
        if (vision!=0 && !t.blockVision(this)){
            if (y!=0)
                renderVision(fog,x,y-1,vision);
            if (y!=fog.length-1)
                renderVision(fog,x,y+1,vision);
            if (x!=0)
                renderVision(fog,x-1,y,vision);
            if (x!=fog[0].length-1)
                renderVision(fog,x+1,y,vision);
        }
    }

    public void reachableLocation (boolean[][] map) {
        if (map!=null && map.length!=0 && map[0]!=null && map[0].length!=0)
            reachableLocation (map,location.x,location.y,moveQuantity+Universe.get().getTerrain(location.x,location.y).moveCost(this));
    }

    private void reachableLocation (boolean[][] map, int x, int y, int movePoint){
        Integer mvP=Universe.get().getTerrain(x,y).moveCost(this);
        if (mvP!=null && movePoint>=mvP)
            movePoint-=mvP;
        else
            return;

        map[y][x]=true;

        if (y!=0)
            reachableLocation(map,x,y-1,movePoint);
        if (y!=map.length-1)
            reachableLocation(map,x,y+1,movePoint);
        if (x!=0)
            reachableLocation(map,x-1,y,movePoint);
        if (x!=map[0].length-1)
            reachableLocation(map,x+1,y,movePoint);
    }

    public void canTarget (boolean[][] map) {
        if (map!=null && map.length!=0 && map[0]!=null && map[0].length!=0 && (primaryWeapon!=null || secondaryWeapon!=null)){
            if (canAttackAfterMove())
                canTarget (map,location.x,location.y,moveQuantity+Universe.get().getTerrain(location.x,location.y).moveCost(this));
            else{
                if (primaryWeapon!=null)
                    primaryWeapon.canTarget(map,location.x,location.y);
                if (secondaryWeapon!=null)
                    secondaryWeapon.canTarget(map,location.x,location.y);
            }
        }
    }

    private void canTarget (boolean[][] map, int x, int y, int movePoint){
        Integer mvP=Universe.get().getTerrain(x,y).moveCost(this);
        if (mvP!=null && movePoint>=mvP)
            movePoint-=mvP;
        else
            return;

        if (movePoint>0){
            if (primaryWeapon!=null)
                primaryWeapon.canTarget(map,location.x,location.y);
            if (secondaryWeapon!=null)
                secondaryWeapon.canTarget(map,location.x,location.y);
        }

        if (y!=0)
            reachableLocation(map,x,y-1,movePoint);
        if (y!=map.length-1)
            reachableLocation(map,x,y+1,movePoint);
        if (x!=0)
            reachableLocation(map,x-1,y,movePoint);
        if (x!=map[0].length-1)
            reachableLocation(map,x+1,y,movePoint);
    }

    public String getName (){
        return name;
    }

    @Override
    public String toString() {
        String out = getName() +
                "\nHP : " + Integer.toString(life);
        if(player != null)
            out += "\nPlayer : " + player.name;
            out += "\nVision : " + Integer.toString(vision);
        if(fuel != null)
            out += "\nFuel : " + Integer.toString(fuel.getQuantity());
        if(primaryWeapon != null)
            out += "\nPrimary : " + primaryWeapon.name;
        if(secondaryWeapon != null)
            out += "\nSecondary : " + secondaryWeapon.name;
        return out;
    }

    public boolean canAttack(AbstractUnit u){
        return true;
    }
}
