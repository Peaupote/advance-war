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

    public boolean isInRange(int actualX, int actualY, int targetX, int targetY){
        int i=Math.abs(actualX-targetX)+Math.abs(actualY-targetY);
        return i<=getMaximumRange() && i>=getMinimumRange();
    }

    public void renderTarget(boolean[][] map, int x, int y, boolean enabled, boolean fullMove){
        int[][] t = {
            {1,1},{1,-1},{-1,-1},{-1,1}
        };
        if (canAttackAfterMove?enabled:fullMove)
            for (int i=getMinimumRange();i<=getMaximumRange();i++)
                for (int j=0;j<=i;j++)
                    for (int[] d : t)
                        if (x+d[0]*(i-j)>=0 && x+d[0]*(i-j)<map[0].length && y+d[1]*j>=0 && y+d[1]*j<map.length)
                            map[y+d[1]*j][x+d[0]*(i-j)]=true;
    }

    public boolean canAttack(AbstractUnit shooter, AbstractUnit target){
        return ammo!=0 && (canAttackAfterMove?shooter.isEnabled():shooter.getMoveQuantity()==shooter.getMaxMoveQuantity()) && canShoot(target) && isInRange(shooter.getX(),shooter.getY(),target.getX(),target.getY());
    }

    public int getMinimumRange(){
        return minimumRange;
    }

    public int getMaximumRange(){
        return maximumRange;
    }

    public final boolean isContactWeapon(){
        return getMinimumRange()==1 && getMaximumRange()==1;
    }
}
