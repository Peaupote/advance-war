package fr.main.model.units;

import java.awt.Point;

import fr.main.model.Direction;
import fr.main.model.Universe;
import fr.main.model.players.Player;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.units.air.AirUnit;
import fr.main.model.units.weapons.PrimaryWeapon;
import fr.main.model.units.weapons.SecondaryWeapon;
import fr.main.model.units.weapons.Weapon;

/**
 * Represents an unit on the board
 */
public abstract class Unit implements AbstractUnit {

    /**
     * Add Unit UID
     */
    private static final long serialVersionUID = 5902107581192600020L;
    /**
     * Life in percentage
     */
    protected Point location;
    protected Player player;
    protected int life, moveQuantity;

    public final Fuel fuel;
    public final MoveType moveType;
    public final int vision, maxMoveQuantity, cost;
    public final String name;
    public final PrimaryWeapon primaryWeapon;
    public final SecondaryWeapon secondaryWeapon;

    public class Fuel implements java.io.Serializable{
        /**
         * 
         */
        private static final long serialVersionUID = 5637502351862309798L;
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

        /**
          * @return true if the unit has still has fuel.
          */
        public boolean consume(int quantity){
            quantity *= Universe.get().getWeather().malusFuel;
            this.quantity = Math.max(0,this.quantity-quantity);
            if (fuel.quantity <= 0 && diesIfNoFuel) dies();
            return quantity != 0;
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
        if (player != null) player.add(this);
        Universe.get().updateVision();
    }
    
    @Override
    public void dies(){
        life = 0;
        Universe.get().setUnit(getX(), getY(), null);
        player.remove(this);
    }

    @Override
    public int getBaseVision(){
        return vision;
    }

    @Override
    public int getMoveQuantity(){
        return moveQuantity;
    }

    @Override
    public int getMaxMoveQuantity(){
        return maxMoveQuantity;
    }

    @Override
    public void setMoveQuantity(int m){
        this.moveQuantity = Math.min(m,getMaxMoveQuantity());
    }

    @Override
    public Fuel getFuel(){
        return fuel;
    }

    /**
     * @return true if and only if the unit is still alive
     */
    @Override
    public final boolean setLife (int life) {
        this.life = Math.max(0, Math.min(100, life));
        if (this.life == 0){
            dies();
            return false;
        } else return true;
    }

    @Override
    public final int getLife () {
        return life;
    }

    @Override
    public final void setPlayer (Player p) {
        this.player = p;
    }

    @Override
    public final Player getPlayer(){
        return this.player;
    }

    @Override
    public final int getX () {
        return location.x;
    }

    @Override
    public final int getY () {
        return location.y;
    }

    @Override
    public final void setLocation(int x, int y){
        location.move(x, y);  
    }

    /**
     * @return true if and only if the move was done.
     */
    @Override
    public final boolean move(int x, int y) {
        Universe u = Universe.get();
        if (u.isValidPosition(x, y)) {
            AbstractUnit unit = u.getUnit(x, y);
            if (unit != null && unit.getPlayer() != getPlayer()) {
                // if there is an unit on the tile we want to move to, and if it is an opponent,
                // it attacks this unit, and there is no counter attack possible
                if (unit.canAttack(this)) unit.attack(this, false);
                setMoveQuantity(0);
                return false;
            }

            Integer q = moveCost(x,y);
            if (q == null || q > getMoveQuantity()){ // the cost to move on the tile is too high
                setMoveQuantity(0);
                return false;
            }
            removeMoveQuantity(q);

            // it should be 1 but in case we teleport the unit, we spend the right amount
            int fuelQuantity = Math.abs(x-location.x)+Math.abs(y-location.y);

            u.setUnit(location.x, location.y, null);
            location.x = x;
            location.y = y;
            u.setUnit(x, y, this);
            fuel.consume(fuelQuantity);
            u.updateVision();
            return life > 0;
        }

        return false;
    }

    @Override
    public MoveType getMoveType() {
        return moveType;
    }

    @Override
    public final void renderVision (boolean[][] fog, boolean linearRegression) {
        int x = location.x, y = location.y, visionT = getVision(),
            height = this instanceof AirUnit ? 2 : Universe.get().getTerrain(x,y).getHeight();

        fog[y][x] = true; // the current tile can always be seen
        if (visionT != 0){
            // we decompose the tiles in 2 categories according to the position of the unit (to avoid checking multiple times the same tile)
            // first we check the tiles that are in straight line
            for (int i = 1 ; i <= visionT ; i++)
                for (Direction d : Direction.cardinalDirections()){
                    int xx = x + i * d.x, yy = y + i * d.y;
                    if (xx >= 0 && yy >= 0 && yy < fog.length && xx < fog[yy].length && !fog[yy][xx] && (i == 1 || !Universe.get().getTerrain(xx, yy).hideFrom(this)) && (!linearRegression || height == 2 || linearRegression(x, y, xx, yy, height)))
                        fog[yy][xx]=true;
                }
            // then we check the other tiles, from the closest to the farthest
            int[][] t = Direction.getNonCardinalDirections();
            for (int i = 2 ; i <= visionT ; i++)
                for (int j = 1 ; j < i ; j ++)
                    for (int[] tab : t){
                        int xx = x + j * tab[0], yy = y + (i - j) * tab[1];
                        if (xx >= 0 && yy >= 0 && yy < fog.length && xx < fog[yy].length && !fog[yy][xx] && !Universe.get().getTerrain(xx, yy).hideFrom(this) && (height == 2 || linearRegression(x, y, xx, yy, height)))
                             fog[yy][xx]=true;
                    }
        }
    }

    /**
     * @param startX is the horizontal position of the unit
     * @param startY is the vertical position of the unit
     * @param x is the horizontal position of the target tile
     * @param y is the vertical position of the target tile
     * @param height is the height of the unit
     * @return true if and only if there is nothing blocking the vision of the unit between its position and the target tile 
     */
    private boolean linearRegression(int startX, int startY, int x, int y, int height){
    // this function is quite unclear so I'll write lots of comments...

        // change coordinates to make it easier
        x = 2 * (x - startX); y = 2 * (y - startY);
        if (Math.abs(x) + Math.abs(y) <= 2) return true; // the two tiles are adjacents

        // we use two int to know if the move is oriented to the right or the left and the top or the bottom
        int epsilonX = x > 0 ? 1 : -1, epsilonY = y > 0 ? 1 : -1;
        // we use two points (a,b) and (c,d) to represent the tile we are considering (from the beginning and from the end : the line is symmetrical)
        int a = 0, b = 0, c = x, d = y;

        // we look what direction is the most used : if the move is to the upper right for example,
        // if the move is mainly horizontal then the first move to do is to go to the right
        // else if the move is mainly vertical then the first move to do is to go to the top 
        // and otherwise it means that the move is as much vertical as horizontal and so it is a diagonal move
        // then we have to move diagonaly
            // we go to the next tile
        if (x * x >= y * y){ // mainly horizontal
            a = 2 * epsilonX; c = x - 2 * epsilonX;
        }
        if (x * x <= y * y){ // mainly vertical
            b = 2 * epsilonY; d = y - 2 * epsilonY;
        }


        // if the move was diagonal then we check if one of the two tiles possibles :
        // if we're going from the tile q to the tile i, we check if we can see through tiles l or r, tile m, and tile h or n
        /* a b c d e
           f g h i j
           k l m n o
           p q r s t
           u v w x y
        */
        if (x * x == y * y){ // diagonal path, check first and last tiles
                if (!canSeeThrough(startX, startY, 2 * epsilonX, 0, height) && !canSeeThrough(startX, startY, 0, 2 * epsilonY, height))
                        return false;
                if (!canSeeThrough(startX, startY, x - 2 * epsilonX, y, height) && !canSeeThrough(startX, startY, x, y - 2 * epsilonY, height))
                        return false;
        }

        // we check the whole straight line
        while (a * epsilonX < c * epsilonX || b * epsilonY < d * epsilonY){
            if (!canSeeThrough(startX, startY, a, b, height) || !canSeeThrough(startX, startY, c, d, height)) // check actual
                return false;

            if (y * (a + epsilonX) == x * (b + epsilonY)){ // diagonal
                if (!canSeeThrough(startX, startY, a + 2 * epsilonX, b, height) && !canSeeThrough(startX, startY, a, b + 2 * epsilonY, height))
                        return false;

                if (!canSeeThrough(startX, startY, c - 2 * epsilonX, d, height) && !canSeeThrough(startX, startY, c, d - 2 * epsilonY, height))
                        return false;
            }

            // we use these booleans to know if we move verticaly or horizontaly
            // we check if the corner a tile is above or below the straight line (the corner corresponding to the directions of the move)
            // if both are true then the move is diagonal
            boolean sup = epsilonX * epsilonY * y * (a + epsilonX) >= epsilonX * epsilonY * x * (b + epsilonY),
                    inf = epsilonX * epsilonY * y * (a + epsilonX) <= epsilonX * epsilonY * x * (b + epsilonY);
            if (sup){ // vertical 
                b += 2 * epsilonY; d -= 2 * epsilonY;
            }
            if (inf) { // horizontal
                a += 2 * epsilonX; c -= 2 * epsilonX;
            }
        }
        return a != c || b != d || canSeeThrough(startX, startY, a, b, height);
        // return if the middle tile is visible
    }

    /**
     * parameters are the same as in the precedent function
     * @return true if and only if the unit can see through the tile
     */
    private final boolean canSeeThrough(int startX, int startY, int x, int y, int height){ // especially written for linearRegression, should probably not be used in any other method
        AbstractTerrain t = Universe.get().getTerrain(x / 2 + startX, y / 2 + startY);
        return t.hideFrom(this) ? t.getHeight() < height : t.getHeight() <= height;
    }

    @Override
    public PrimaryWeapon getPrimaryWeapon(){
        return primaryWeapon;
    }

    @Override
    public SecondaryWeapon getSecondaryWeapon(){
        return secondaryWeapon;
    }

    @Override
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

    @Override
    public boolean canAttack(AbstractUnit u){
        return  (primaryWeapon   == null ? false : primaryWeapon.canAttack(this, u)) ||
                (secondaryWeapon == null ? false : secondaryWeapon.canAttack(this, u));
    }

    @Override
    public boolean canAttack () {
        return (primaryWeapon != null && (getMoveQuantity() == getMaxMoveQuantity() || primaryWeapon.canAttackAfterMove)) || secondaryWeapon != null;
    }

    @Override
    public void attack(AbstractUnit u, boolean counter){
        if (getMoveQuantity()==0) return;
        Weapon w = null;

        if (primaryWeapon != null && primaryWeapon.canAttack(this,u))
            w = primaryWeapon;
            // if possible we attack with the main weapon
        else if (secondaryWeapon != null && secondaryWeapon.canAttack(this,u))
            w = secondaryWeapon;
            // else we use the secondary weapon

        if (w != null){
            w.shoot();
            int damages = AbstractUnit.damage(this, w == primaryWeapon ,u);
            u.getPlayer().getCommander().powerBar.addValue(damages);
            u.removeLife(damages);
        }

        if (counter){
            // if there is a counter attack, we do it
            this.setMoveQuantity(0);
            if (u.getLife() != 0)
                u.attack(this,false);
        }
    }

    @Override
    public int getFuelTurnCost(){
        return 0;
    }

    @Override
    public int getCost(){
        return cost;
    }
}
