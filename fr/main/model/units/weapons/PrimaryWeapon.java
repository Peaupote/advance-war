package fr.main.model.units.weapons;

import java.util.Map;

import fr.main.model.units.AbstractUnit;
import fr.main.model.Direction;

/**
 * Primary weapons are weapons with limited ammunitions. They may be range weapons. 
 */
public class PrimaryWeapon extends Weapon{

    /**
	 * Add PrimaryWeapon UID
	 */
	private static final long serialVersionUID = 2204854764828219000L;
	private int ammo;
    public final int minimumRange, maximumRange, maximumAmmo;

    public PrimaryWeapon (String name, int maximumAmmo, int minimumRange, int maximumRange, Map<Class<? extends AbstractUnit>,Integer> damages, boolean canAttackAfterMove){
        super(name,damages,canAttackAfterMove);
        this.maximumAmmo  = maximumAmmo;
        this.ammo         = maximumAmmo;
        this.maximumRange = maximumRange;
        this.minimumRange = minimumRange;
    }

    public PrimaryWeapon(String name, int maximumAmmo, Map<Class<? extends AbstractUnit>,Integer> damages, boolean canAttackAfterMove){
        this(name,maximumAmmo,1,1,damages,canAttackAfterMove);
    }

    public int getAmmunition(){
        return ammo;
    }

    public int getMaximumAmmunition(){
        return maximumAmmo;
    }

    public void replenish(){
        this.ammo = this.maximumAmmo;
    }

    /**
     * One ammunition is removed when shooting
     */
    public void shoot(){
        ammo--;
    }

    @Override
    public boolean isInRange(AbstractUnit unit, AbstractUnit target){
        int i = Math.abs(unit.getX()-target.getX())+Math.abs(unit.getY()-target.getY());
        return i <= getMaximumRange(unit) && i >= getMinimumRange(unit);
    }

    @Override
    public void renderTarget(boolean[][] map, AbstractUnit u, int x, int y){
        int[][] t = Direction.getNonCardinalDirections();
        int maxRange = getMaximumRange(u);
        if (canAttackAfterMove ? u.getMoveQuantity() != 0 : u.getMoveQuantity() == u.getMaxMoveQuantity() && u.getX() == x && u.getY() == y)
            for (int i = getMinimumRange(u); i <= maxRange; i ++)
                for (int j = 0; j <= i; j ++)
                    for (int[] d : t){
                        int xx = x + d[0] * (i - j), yy = y + d[1] * j;
                        if (yy >= 0 && yy < map.length && xx >= 0 && xx < map[0].length)
                            map[yy][xx]=true;
                    }
    }

    @Override
    public boolean canAttack(AbstractUnit shooter, AbstractUnit target){
        return target != null && shooter.getPlayer() != target.getPlayer() && ammo > 0 && (canAttackAfterMove ? shooter.isEnabled() : shooter.getMoveQuantity() == shooter.getMaxMoveQuantity()) && canShoot(target) && isInRange(shooter,target);
    }

    /**
     * @return the basic minimum range of the weapon
     */
    public int getBaseMinimumRange(){
        return minimumRange;
    }

    /**
     * @param u is the unit using the weapon
     * @return the real minimum range of the weapon (used by the unit)
     */
    public int getMinimumRange(AbstractUnit u){
        return u.getPlayer().getCommander().getMinimumRange(u, this);
    }

    /**
     * @return the basic maximum range of the weapon
     */
    public int getBaseMaximumRange(){
        return maximumRange;
    }

    /**
     * @param u is the unit using the weapon
     * @return the real maximum range of the weapon (used by the unit)
     */
    public int getMaximumRange(AbstractUnit u){
        return u.getPlayer().getCommander().getMaximumRange(u, this);
    }

    /**
     * @return true if and only if the weapon is a contact weapon (it attacks only the adjacent units)
     */
    public final boolean isContactWeapon(){
        return getBaseMinimumRange() == 1 && getBaseMaximumRange() == 1;
    }
}
