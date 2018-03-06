package fr.main.model.units;

import java.awt.Point;
import java.util.Random;
import java.io.Serializable;

import fr.main.model.Player;
import fr.main.model.Weather;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.weapons.Weapon;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.terrains.Buildable;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.RepairBuilding;
import fr.main.model.Universe;
import fr.main.model.Direction;

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

    public final Fuel fuel;
    public final MoveType moveType;

    private PrimaryWeapon primaryWeapon;
    private SecondaryWeapon secondaryWeapon;

    public final int vision, maxMoveQuantity, cost;
    public final String name;

    public class Fuel implements java.io.Serializable{
        public final String name; // l'infanterie n'a pas de 'carburant' mais des 'rations' (c'est un détail mais bon)
        public final int maximumQuantity;
        private int quantity;
        public final boolean diesIfNoFuel;

        public Fuel(String name, int maximumQuantity, boolean diesIfNoFuel){
            this.name            = name;
            this.maximumQuantity = maximumQuantity;
            this.quantity        = maximumQuantity;
            this.diesIfNoFuel    = diesIfNoFuel;
        }

        /*
         *  @return true if the unit has still has fuel.
         */
        public boolean consume(int quantity){
            quantity*=Universe.get().getWeather().malusFuel;
            this.quantity=Math.max(0,this.quantity-quantity);
            if (fuel.quantity==0 && diesIfNoFuel){
                Universe.get().setUnit(location.x,location.y,null);
                player.remove(Unit.this);
            }
            return quantity!=0;
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

    public Unit (Player player, Point location) {
        this (player, location, "fuel", 0, false, MoveType.WHEEL, 5, 2, null, null, "unit",1);
    }

    public Unit (Player player, Point location, String fuelName, int maxFuel, boolean diesIfNoFuel, MoveType moveType, int moveQuantity, int vision, PrimaryWeapon primaryWeapon, SecondaryWeapon secondaryWeapon, String name, int cost) {
        this.life            = 100;
        this.player          = player;
        this.location        = location;
        this.fuel            = new Fuel(fuelName,maxFuel,diesIfNoFuel);
        this.moveType        = moveType;
        this.maxMoveQuantity = moveQuantity;
        this.moveQuantity    = moveQuantity;
        this.vision          = vision;
        this.primaryWeapon   = primaryWeapon;
        this.secondaryWeapon = secondaryWeapon;
        this.name            = name;
        this.cost            = cost;
        move(location.x, location.y);
    }

    public int getMoveQuantity(){
        return moveQuantity;
    }

    public int getMaxMoveQuantity(){
        return maxMoveQuantity;
    }

    public void setMoveQuantity(int m){
        this.moveQuantity=Math.min(m,getMaxMoveQuantity());
    }

    public Fuel getFuel(){
        return fuel;
    }

    /*
    * @return true if and only if the unit is still alive
    */
    public final boolean setLife (int life) {
        this.life = Math.max(0, Math.min(100, life));
        if (this.life==0){
            player.remove(this);
            Universe.get().setUnit(location.x,location.y,null);
            return false;
        }
        else
            return true;
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

    /*
    * @return true if and only if the move was done.
    */
    public final boolean move(int x, int y) {
        Universe u = Universe.get();
        if (u != null && u.isValidPosition(x, y)) {
            AbstractUnit unit = u.getUnit(x, y);
            if (unit != null) {
                if (unit.canAttack(this)) unit.attack(this, false);
                setMoveQuantity(0);
                return false;
            }

            int q = u.getTerrain(x,y).moveCost(this);
            if (q>getMoveQuantity()){
                setMoveQuantity(0);
                return false;
            }
            removeMoveQuantity(q);

            int fuelQuantity=Math.abs(x-location.x)+Math.abs(y-location.y);

            u.setUnit(location.x, location.y, null);
            location.x = x;
            location.y = y;
            u.setUnit(x, y, this);
            fuel.consume(fuelQuantity);
            u.updateVision();
            return true;
        }
        
        return false;
    }

    public MoveType getMoveType() {
        return moveType;
    }

    public final void renderVision (boolean[][] fog) {
        //TODO : can see beyond a moutain or a forest if the unit is on a mountain
        //TODO : can see beyond a forest if the unit is on a hill
        if (fog == null || fog.length == 0 || fog[0] == null || fog[0].length == 0)
            return;

        int x = location.x,
            y = location.y;

        fog[y][x] = true;

        if (vision != 0){
            if (y != 0){
                fog[y - 1][x] = true;
                renderVision (fog, x, y - 1, vision);
            }
            if (y != fog.length - 1){
                fog[y + 1][x] = true;
                renderVision (fog, x, y + 1, vision);
            }
            if (x != 0){
                fog[y][x - 1] = true;
                renderVision (fog, x - 1, y, vision);
            }
            if (x != fog[0].length - 1){
                fog[y][x + 1] = true;
                renderVision (fog, x + 1, y, vision);
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
        AbstractTerrain terrain = Universe.get().getTerrain(x,y);
        Integer mvP     = terrain.moveCost(this);

        if (mvP == null || movePoint < mvP)
            return;
        
        movePoint -= mvP;
        if (terrain.canStop(this))
            map[y][x] = true;

        for (Direction d : Direction.cardinalDirections())
            if (x + d.x >= 0 && x + d.x < map[0].length && y + d.y >= 0 && y + d.y < map.length)
                reachableLocation(map, x + d.x, y + d.y, movePoint);
    }

    public void renderTarget (boolean[][] map, int x, int y) {
        if (primaryWeapon != null)
            primaryWeapon.renderTarget(map, x, y, isEnabled(), getMoveQuantity() == getMaxMoveQuantity());
        if (secondaryWeapon != null)
            secondaryWeapon.renderTarget(map, x, y, isEnabled(), getMoveQuantity() == getMaxMoveQuantity());
    }

    public void renderAllTargets(boolean[][] map, int x, int y){
        renderAllTargets(map, x, y, moveQuantity);
    }

    private void renderAllTargets(boolean[][] map, int x, int y, int movePoint){
        if (Universe.get().getTerrain(x,y).canStop(this))
            renderTarget(map,x,y);

        for (Direction d : Direction.cardinalDirections())
            if (x+d.x>=0 && x+d.x<map[0].length && y+d.y>=0 && y+d.y<map.length){
                Integer mvP=Universe.get().getTerrain(x+d.x,y+d.y).moveCost(this);
                if (mvP==null?false:movePoint>mvP)
                    renderAllTargets(map,x+d.x,y+d.y,movePoint-mvP);
            }
    }

    public String getName (){
        return name;
    }

    @Override
    public String toString() {
        String out = getName() +
                "\nHP : " + life+"/100";
        if(player != null)
            out += "\nJoueur : " + player.name;
            out += "\nVision : " + vision;
            out += "\nMouvement : " + moveQuantity+"/"+maxMoveQuantity;
        if(fuel != null)
            out += "\n"+fuel.name+" : " + fuel.getQuantity()+"/"+fuel.maximumQuantity;
        if(primaryWeapon != null){
            out += "\n" + primaryWeapon.name+" : "+primaryWeapon.getAmmunition()+"/"+primaryWeapon.maximumAmmo;
            if (!primaryWeapon.isContactWeapon()) out+=", "+primaryWeapon.minimumRange+"~"+primaryWeapon.maximumRange;
        }
        if(secondaryWeapon != null)
            out += "\nSecondary : " + secondaryWeapon.name;
        return out;
    }

    public boolean canAttack(AbstractUnit u){
        return (primaryWeapon == null ? false : primaryWeapon.canAttack(this,u)) ||
                (secondaryWeapon==null ? false : secondaryWeapon.canAttack(this,u));
    }

    public boolean canAttack () {
      return primaryWeapon != null || secondaryWeapon != null;
    }

    public void attack(AbstractUnit u, boolean counter){
        if (getMoveQuantity()==0) return;
        if (primaryWeapon!=null && primaryWeapon.canAttack(this,u)){
            primaryWeapon.shoot();
            u.removeLife(damage(this,primaryWeapon,u));
        } else if (secondaryWeapon!=null && secondaryWeapon.canAttack(this,u)) {
            secondaryWeapon.shoot();
            u.removeLife(damage(this,secondaryWeapon,u));
        }

        if (counter){
            this.setMoveQuantity(0);
            if (u.getLife()!=0)
                u.attack(this,false);
        }
    }

    public int getFuelTurnCost(){
        return 0;
    }

    public int getCost(){
        return cost;
    }

    public static final int damage(AbstractUnit attacker, Weapon w, AbstractUnit defender){
        int b       = w.damage(defender);
        int aCO     = attacker.getPlayer().getCommander().getAttackValue(attacker);
        Random rand = new Random(); int r = rand.nextInt(1000);
        int aHP     = attacker.getLife();
        int dCO     = defender.getPlayer().getCommander().getDefenseValue(defender);
        int dTR     = Universe.get().getTerrain(defender.getX(),defender.getY()).getDefense(defender);
        int dHP     = defender.getLife();

        return Math.max(0,(b*aCO+r)*aHP*(2000-10*dCO-dTR*dHP)/10000000);
    }
}
