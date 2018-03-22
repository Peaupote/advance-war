package fr.main.model.units;

import java.awt.*;
import java.util.Random;
import java.util.HashSet;
import java.io.Serializable;

import fr.main.model.Player;
import fr.main.model.Weather;
import fr.main.model.units.AbstractUnit;
import fr.main.model.units.air.AirUnit;
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
 * Represents an unit on the board
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

    public Unit (Player player, Point location, String fuelName, int maxFuel, boolean diesIfNoFuel, MoveType moveType, int moveQuantity, int vision, PrimaryWeapon primaryWeapon, SecondaryWeapon secondaryWeapon, String name, int cost) {
        this.life            = 100;
        this.player          = player;
        if (player != null) player.add(this);
        this.location        = location;
        Universe.get().setUnit(location.x, location.y, this);
        this.fuel            = new Fuel(fuelName, maxFuel, diesIfNoFuel);
        this.moveType        = moveType;
        this.maxMoveQuantity = moveQuantity;
        this.moveQuantity    = moveQuantity;
        this.vision          = vision;
        this.primaryWeapon   = primaryWeapon;
        this.secondaryWeapon = secondaryWeapon;
        this.name            = name;
        this.cost            = cost;
        Universe.get().updateVision();
    }

    public int getBaseVision(){
        return vision;
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

    public final void setPlayer (Player p) {
        this.player = p;
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
            if (unit != null && unit.getPlayer() != getPlayer()) {
                if (unit.canAttack(this)) unit.attack(this, false);
                setMoveQuantity(0);
                return false;
            }

            Integer q = moveCost(x,y);
            if (q == null || q > getMoveQuantity()){
                setMoveQuantity(0);
                return false;
            }
            removeMoveQuantity(q);

            int fuelQuantity = Math.abs(x-location.x)+Math.abs(y-location.y);

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

    public final void renderVision (boolean[][] fog, boolean linearRegression) {
        int x = location.x, y = location.y, visionT = getVision(),
            height = this instanceof AirUnit ? 2 : Universe.get().getTerrain(x,y).getHeight();

        Point start = location.getLocation();
        fog[y][x]=true;
        if (visionT != 0){
            for (int i = 1 ; i <= visionT ; i++)
                for (Direction d : Direction.cardinalDirections()){
                    int xx = x + i * d.x, yy = y + i * d.y;
                    if (xx >= 0 && yy >= 0 && yy < fog.length && xx < fog[yy].length && !fog[yy][xx] && (i == 1 || !Universe.get().getTerrain(xx, yy).hideFrom(this)) && (!linearRegression || height == 2 || linearRegression(x, y, xx, yy, height)))
                        fog[yy][xx]=true;
                }
            int[][] t = {
                {1,1},{1,-1},{-1,-1},{-1,1}
            };
            for (int i = 2 ; i <= visionT ; i++)
                for (int j = 1 ; j <= i ; j ++)
                    for (int[] tab : t){
                        int xx = x + j * tab[0], yy = y + (i - j) * tab[1];
                        if (xx >= 0 && yy >= 0 && yy < fog.length && xx < fog[yy].length && !fog[yy][xx] && !Universe.get().getTerrain(xx, yy).hideFrom(this) && (height == 2 || linearRegression(x, y, xx, yy, height)))
                             fog[yy][xx]=true;
                    }
        }
    }

    private boolean linearRegression(int startX, int startY, int x, int y, int height){
        x = 2 * (x - startX); y = 2 * (y - startY);
        if (Math.abs(x) + Math.abs(y) <= 2) return true;
        Universe u = Universe.get();

        int epsilonX = x > 0 ? 1 : -1, epsilonY = y > 0 ? 1 : -1;

        int a = 0, b = 0, c = x, d = y;

        if (x * x >= y * y){ // mainly horizontal
            a = 2 * epsilonX; c = x - 2 * epsilonX;
        }
        if (x * x <= y * y){ // mainly vertical
            b = 2 * epsilonY; d = y - 2 * epsilonY;
        }

        AbstractTerrain t;

        if (x * x == y * y){ // diagonal path, check first and last tiles
                if (!canSeeThrough(startX, startY, 2 * epsilonX, 0, height) && !canSeeThrough(startX, startY, 0, 2 * epsilonY, height))
                        return false;
                if (!canSeeThrough(startX, startY, x - 2 * epsilonX, y, height) && !canSeeThrough(startX, startY, x, y - 2 * epsilonY, height))
                        return false;
        }

        while (a * epsilonX < c * epsilonX || b * epsilonY < d * epsilonY){
            if (!canSeeThrough(startX, startY, a, b, height) || !canSeeThrough(startX, startY, c, d, height)) // check actual
                return false;

            if (y * (a + epsilonX) == x * (b + epsilonY)){ // diagonal
                if (!canSeeThrough(startX, startY, a + 2 * epsilonX, b, height) && !canSeeThrough(startX, startY, a, b + 2 * epsilonY, height))
                        return false;

                if (!canSeeThrough(startX, startY, c - 2 * epsilonX, d, height) && !canSeeThrough(startX, startY, c, d - 2 * epsilonY, height))
                        return false;
            }

            boolean sup = epsilonX * epsilonY * y * (a + epsilonX) >= epsilonX * epsilonY * x * (b + epsilonY),
                    inf = epsilonX * epsilonY * y * (a + epsilonX) <= epsilonX * epsilonY * x * (b + epsilonY);
            if (sup){ // vertical 
                b += 2 * epsilonY;
                d -= 2 * epsilonY;
            }
            if (inf) { // horizontal
                a += 2 * epsilonX;
                c -= 2 * epsilonX;
            }
        }
        return a != c || b != d || canSeeThrough(startX, startY, a, b, height);
    }

    private final boolean canSeeThrough(int startX, int startY, int x, int y, int height){ // especially written for linearRegression, should probably not be used in any other method
        AbstractTerrain t = Universe.get().getTerrain(x / 2 + startX, y / 2 + startY);
        return t.hideFrom(this) ? t.getHeight() < height : t.getHeight() <= height;
    }

    public PrimaryWeapon getPrimaryWeapon(){
        return primaryWeapon;
    }

    public SecondaryWeapon getSecondaryWeapon(){
        return secondaryWeapon;
    }

    public void renderTarget (boolean[][] map, int x, int y) {
        if (primaryWeapon != null)
            primaryWeapon.renderTarget(map, this);
        if (secondaryWeapon != null)
            secondaryWeapon.renderTarget(map, this);
    }

    public void renderAllTargets(boolean[][] map, int x, int y){
        renderAllTargets(map, x, y, moveQuantity);
    }

    //TODO : réécrire en utilisant Dijkstra
    private void renderAllTargets(boolean[][] map, int x, int y, int movePoint){
        if (canStop(x,y))
            renderTarget(map, x, y);

        for (Direction d : Direction.cardinalDirections()){
            int xx = x + d.x, yy = y + d.y;
            if (yy >= 0 && xx >= 0 && yy < map.length && xx < map[yy].length){
                Integer mvP=moveCost(xx,yy);
                if (mvP==null?false:movePoint>mvP)
                    renderAllTargets(map, xx, yy, movePoint-mvP);
            }
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
            out += "\nVision : " + getVision();
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
            int damages=damage(this,secondaryWeapon,u);
            secondaryWeapon.shoot();
            u.getPlayer().getCommander().powerBar.addValue(damages);
            u.removeLife(damages);
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
        AbstractBuilding building = Universe.get().getBuilding(defender.getX(), defender.getY());
        int dTR     = Universe.get().getTerrain(defender.getX(),defender.getY()).getDefense(defender) + (building == null ? 0 : building.getDefense(defender));
        int dHP     = defender.getLife();

        return Math.max(0,(b*aCO+r)*aHP*(2000-10*dCO-dTR*dHP)/10000000);
    }

}
