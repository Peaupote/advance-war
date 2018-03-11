package fr.main.model.units.weapons;

import java.util.Map;

import fr.main.model.units.AbstractUnit;

/*
* Primary weapons are weapons with limited ammunitions that can be range weapons. 
*/
public class PrimaryWeapon extends Weapon{

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
        this.ammo=this.maximumAmmo;
    }

    public void shoot(){
        if (ammo==0)
            throw new RuntimeException("No ammunition left ! Impossible to shoot.");
        else
            ammo--;
    }

    public boolean isInRange(AbstractUnit unit, AbstractUnit target){
        int i=Math.abs(unit.getX()-target.getX())+Math.abs(unit.getY()-target.getY());
        return i <= getMaximumRange(unit) && i >= getMinimumRange(unit);
    }

    public void renderTarget(boolean[][] map, AbstractUnit u){
        int[][] t = {
            {1,1},{1,-1},{-1,-1},{-1,1}
        };
        int x = u.getX(), y = u.getY(), maxRange = getMaximumRange(u);
        if (canAttackAfterMove ? u.getMoveQuantity() != 0 : u.getMoveQuantity() == u.getMaxMoveQuantity())
            for (int i=getMinimumRange(u);i<=maxRange;i++)
                for (int j=0;j<=i;j++)
                    for (int[] d : t){
                        int xx = x+d[0]*(i-j), yy = y+d[1]*j;
                        if (yy >= 0 && yy < map.length && xx >= 0 && xx < map[0].length)
                            map[yy][xx]=true;
                    }
    }

    public boolean canAttack(AbstractUnit shooter, AbstractUnit target){
        return ammo!=0 && (canAttackAfterMove?shooter.isEnabled():shooter.getMoveQuantity()==shooter.getMaxMoveQuantity()) && canShoot(target) && isInRange(shooter,target);
    }

    public int getBaseMinimumRange(){
        return minimumRange;
    }

    public int getMinimumRange(AbstractUnit u){
        return u.getPlayer().getCommander().getMinimumRange(u, this);
    }

    public int getBaseMaximumRange(){
        return maximumRange;
    }

    public int getMaximumRange(AbstractUnit u){
        return u.getPlayer().getCommander().getMaximumRange(u, this);
    }

    public final boolean isContactWeapon(){
        return getBaseMinimumRange()==1 && getBaseMaximumRange()==1;
    }
}
